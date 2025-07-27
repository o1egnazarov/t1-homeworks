package ru.noleg.authorisationservice.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenProvider {

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails, String jti);

    boolean isTokenValid(String token, UserDetails userDetails);

    String extractUsername(String token);

    String extractJti(String token);
}
