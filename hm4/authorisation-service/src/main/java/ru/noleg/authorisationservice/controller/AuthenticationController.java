package ru.noleg.authorisationservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.authorisationservice.dto.JwtResponse;
import ru.noleg.authorisationservice.dto.JwtTokens;
import ru.noleg.authorisationservice.dto.SignIn;
import ru.noleg.authorisationservice.dto.SignUp;
import ru.noleg.authorisationservice.entity.User;
import ru.noleg.authorisationservice.mapper.UserMapper;
import ru.noleg.authorisationservice.service.auth.AuthenticationService;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@Tag(
        name = "Контроллер для регистрации/аутентификации.",
        description = "Позволяет зарегистрироваться новому пользователю или повторно войти в систему."
)
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public AuthenticationController(AuthenticationService authenticationService, UserMapper userMapper) {
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @PostMapping("/sign-up")
    @Operation(
            summary = "Регистрация пользователя.",
            description = "Позволяет зарегистрироваться новому пользователю."
    )
    public ResponseEntity<Long> signUp(@RequestBody @Valid SignUp signUpRequest) {
        logger.info("Request: POST /signUp registration user with username: {}.", signUpRequest.username());

        User user = this.userMapper.mapToRegisterEntityFromSignUp(signUpRequest);
        Long userId = this.authenticationService.signUp(user);

        logger.info("User with username: {} successfully registered.", signUpRequest.username());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userId);
    }

    @PostMapping("/sign-in")
    @Operation(
            summary = "Аутентификация пользователя.",
            description = "Позволяет повторно войти уже зарегистрированному пользователю."
    )
    public ResponseEntity<JwtResponse> signIn(
            @RequestBody @Valid SignIn signInRequest,
            HttpServletResponse response
    ) {
        logger.info("Request: POST /signIn authentication user with username: {}.", signInRequest.username());

        JwtTokens tokens = this.authenticationService.signIn(signInRequest.username(), signInRequest.password());

        ResponseCookie cookie = ResponseCookie.from("refresh_token", tokens.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        logger.info("User with username: {} successfully authenticated.", signInRequest.username());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse(tokens.accessToken()));
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Обновление токена.",
            description = "Позволяет обновить токен."
    )
    public ResponseEntity<JwtResponse> refresh(@CookieValue("refresh_token") String refreshToken) {
        logger.info("Request: POST /refresh refreshing token.");

        String newAccessToken = this.authenticationService.refresh(refreshToken);

        logger.info("Token successfully refreshed.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse(newAccessToken));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Выход из системы.",
            description = "Позволяет выйти из системы."
    )
    public ResponseEntity<Void> logout(
            @CookieValue("refresh_token") String refreshToken,
            HttpServletResponse response
    ) {
        logger.info("Request: POST /logout logout user.");
        authenticationService.logout(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(0)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}