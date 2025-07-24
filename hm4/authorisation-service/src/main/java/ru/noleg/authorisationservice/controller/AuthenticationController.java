package ru.noleg.authorisationservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.authorisationservice.dto.JwtResponse;
import ru.noleg.authorisationservice.dto.SignIn;
import ru.noleg.authorisationservice.dto.SignUp;
import ru.noleg.authorisationservice.entity.User;
import ru.noleg.authorisationservice.mapper.UserMapper;
import ru.noleg.authorisationservice.service.auth.AuthenticationService;

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
    public ResponseEntity<JwtResponse> signIn(@RequestBody @Valid SignIn signInRequest) {
        logger.info("Request: POST /signIn authentication user with username: {}.", signInRequest.username());

        String token = this.authenticationService.signIn(signInRequest.username(), signInRequest.password());

        logger.info("User with username: {} successfully authenticated.", signInRequest.username());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse(token));
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Обновление токена.",
            description = "Позволяет обновить токен."
    )
    public ResponseEntity<JwtResponse> refresh(@RequestBody String token) {
        logger.info("Request: POST /refresh refreshing token.");

        String refreshedToken = this.authenticationService.refresh(token);

        logger.info("Token successfully refreshed.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse(refreshedToken));
    }
}