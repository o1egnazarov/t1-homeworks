package ru.noleg.authorisationservice.service.auth;

import ru.noleg.authorisationservice.entity.User;

public interface AuthenticationService {
    Long signUp(User user);

    String signIn(String username, String password);

    String refresh(String token);
}
