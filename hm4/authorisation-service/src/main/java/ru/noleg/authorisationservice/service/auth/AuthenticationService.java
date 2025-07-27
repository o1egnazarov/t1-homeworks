package ru.noleg.authorisationservice.service.auth;

import ru.noleg.authorisationservice.dto.JwtTokens;
import ru.noleg.authorisationservice.entity.User;

public interface AuthenticationService {
    Long signUp(User user);

    JwtTokens signIn(String username, String password);

    String refresh(String token);

    void logout(String refreshToken);
}
