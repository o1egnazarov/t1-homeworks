package ru.noleg.authorisationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Size(max = 50) String username,
        @Email @Size(max = 50) String email
) {
}

