package ru.noleg.authorisationservice.service.token;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RefreshTokenStoreImpl implements RefreshTokenStore {

    private final Map<String, String> validTokens = new ConcurrentHashMap<>();

    public void store(String username, String jti) {
        validTokens.put(username, jti);
    }

    public boolean isValid(String username, String jti) {
        return jti.equals(validTokens.get(username));
    }

    public void invalidate(String username) {
        validTokens.remove(username);
    }
}
