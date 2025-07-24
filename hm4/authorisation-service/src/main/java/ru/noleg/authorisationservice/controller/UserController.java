package ru.noleg.authorisationservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.authorisationservice.dto.UpdateUserDto;
import ru.noleg.authorisationservice.dto.UserDto;
import ru.noleg.authorisationservice.dto.UserProfileDto;
import ru.noleg.authorisationservice.entity.User;
import ru.noleg.authorisationservice.mapper.UserMapper;
import ru.noleg.authorisationservice.service.user.UserDetailsImpl;
import ru.noleg.authorisationservice.service.user.UserService;

@RestController
@RequestMapping("/api/users/premium")
@Validated
@Tag(
        name = "Контроллер для пользователя.",
        description = "Позволяет изменять профиль/получать историю аренды."
)
@SecurityRequirement(name = "JWT")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Обновление профиля.",
            description = "Позволяет изменить профиль пользователя."
    )
    public ResponseEntity<UserDto> editUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateUserDto updateUserDto
    ) {
        Long id = userDetails.getId();
        logger.info("Request: PUT /me update profile for user with id: {}.", id);

        User user = this.userService.getUser(id);
        this.userMapper.updateUserFromDto(updateUserDto, user);
        User updateUser = this.userService.save(user);

        logger.info("Profile for user with id: {} successfully updated.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userMapper.mapToDto(updateUser));
    }

    @GetMapping("/me")
    @Operation(
            summary = "Получение профиля пользователя.",
            description = "Позволяет получить текущую информацию о профиле пользователя."
    )
    public ResponseEntity<UserProfileDto> getUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long id = userDetails.getId();
        logger.info("Request: GET /me fetching profile for user with id: {}.", id);

        User user = this.userService.getUser(id);

        logger.info("Profile for user with id: {} successfully fetched.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userMapper.mapToProfileDto(user));
    }
}
