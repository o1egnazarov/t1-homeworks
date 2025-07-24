package ru.noleg.authorisationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUp(
        @NotBlank @Size(min = 5, max = 50) String username,
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
