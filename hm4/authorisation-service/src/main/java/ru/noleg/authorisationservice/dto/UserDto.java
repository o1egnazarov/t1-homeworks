package ru.noleg.authorisationservice.dto;

import ru.noleg.authorisationservice.entity.Role;

import java.util.Set;

public record UserDto(
        Long id,
        String username,
        String email,
        Set<Role> roles
) {
}
