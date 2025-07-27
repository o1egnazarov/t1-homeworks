package ru.noleg.authorisationservice.service.token;

public interface RefreshTokenStore {
    void store(String username, String jti);

    boolean isValid(String username, String jti);

    void invalidate(String username);
}
