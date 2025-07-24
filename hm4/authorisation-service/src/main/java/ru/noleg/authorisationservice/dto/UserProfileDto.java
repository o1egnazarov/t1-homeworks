package ru.noleg.authorisationservice.dto;


import ru.noleg.authorisationservice.entity.Role;

import java.util.Set;

public record UserProfileDto(
        String username,
        String email,
        Set<Role> roles
) {
}
