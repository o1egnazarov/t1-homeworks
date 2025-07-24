package ru.noleg.authorisationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignIn(
        @NotBlank @Size(min = 5, max = 50) String username,
        @NotBlank String password
) {
}
