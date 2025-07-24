package ru.noleg.authorisationservice.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.authorisationservice.entity.Role;
import ru.noleg.authorisationservice.entity.User;
import ru.noleg.authorisationservice.repository.UserRepository;

import java.util.Set;


@Component
@Transactional
public class PrivilegedUsersSetup implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegedUsersSetup.class);

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_EMAIL = "admin@gmail.com";


    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PrivilegedUsersSetup(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initUser(ADMIN_USERNAME, ADMIN_EMAIL, adminPassword, Role.ROLE_ADMIN);
    }

    private void initUser(String username, String email, String password, Role role) {

        if (this.userRepository.findByUsername(username).isPresent()) {
            logger.info("User '{}' already exists, skipping.", username);
            return;
        }

        if (this.userRepository.findByEmail(email).isPresent()) {
            logger.warn("Email '{}' already used, skipping user '{}'.", email, username);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setRoles(Set.of(role));

        this.userRepository.save(user);
        logger.info("Created default user: '{}'.", username);
    }
}