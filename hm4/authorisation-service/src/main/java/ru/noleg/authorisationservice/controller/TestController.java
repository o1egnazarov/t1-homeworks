package ru.noleg.authorisationservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Tag(
        name = "Контроллер для теста.",
        description = "Проверка прав при доступе к компонентам системы."
)
@SecurityRequirement(name = "JWT")
public class TestController {

    @GetMapping("/guests")
    @PreAuthorize("hasRole('GUEST')")
    public String guestEndpoint() {
        return "This is a guest endpoint. Requires GUEST role.";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('PREMIUM_USER')")
    public String userEndpoint() {
        return "This is a premium user endpoint. Requires PREMIUM_USER role.";
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEndpoint() {
        return "This is an admin endpoint. Requires ADMIN role.";
    }
}
