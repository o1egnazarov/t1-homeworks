package ru.noleg.authorisationservice.service.auth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.authorisationservice.dto.JwtTokens;
import ru.noleg.authorisationservice.entity.Role;
import ru.noleg.authorisationservice.entity.User;
import ru.noleg.authorisationservice.exception.BusinessLogicException;
import ru.noleg.authorisationservice.jwt.TokenProvider;
import ru.noleg.authorisationservice.repository.UserRepository;
import ru.noleg.authorisationservice.service.token.RefreshTokenStore;
import ru.noleg.authorisationservice.service.user.UserDetailsImpl;

import java.util.Set;
import java.util.UUID;


@Service
@Transactional
public class AuthenticationServiceJwtImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceJwtImpl.class);

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenStore refreshTokenStore;

    public AuthenticationServiceJwtImpl(UserRepository userRepository,
                                        UserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder,
                                        TokenProvider jwtTokenProvider,
                                        AuthenticationManager authenticationManager,
                                        RefreshTokenStore refreshTokenStore) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.refreshTokenStore = refreshTokenStore;
    }

    @Override
    public Long signUp(User user) {
        logger.debug("Signing up user: {}.", user.getUsername());

        this.validateUserData(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_GUEST));

        Long userId = userRepository.save(user).getId();

        logger.debug("User successfully signUp with id: {}.", userId);
        return userId;
    }

    private void validateUserData(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.error("User with username {} already exists.", user.getUsername());
            throw new BusinessLogicException("User with username: " + user.getUsername() + " already exists.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("User with email {} already exists.", user.getEmail());
            throw new BusinessLogicException("User with email: " + user.getEmail() + " already exists.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JwtTokens signIn(String username, String password) {
        logger.debug("Signing up user: {}.", username);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, password
        ));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);

        String jti = UUID.randomUUID().toString();
        refreshTokenStore.store(username, jti);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails, jti);

        logger.debug("User: {}, successfully signIn.", username);
        return new JwtTokens(accessToken, refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public String refresh(String refreshToken) {
        String username = jwtTokenProvider.extractUsername(refreshToken);
        String jti = jwtTokenProvider.extractJti(refreshToken);

        if (!refreshTokenStore.isValid(username, jti)) {
            throw new SecurityException("Invalid or expired refresh token");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        return jwtTokenProvider.generateAccessToken(userDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public void logout(String refreshToken) {
        String username = jwtTokenProvider.extractUsername(refreshToken);
        refreshTokenStore.invalidate(username);
    }
}