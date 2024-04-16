package com.dreamsol.services;

import com.dreamsol.entities.RefreshToken;
import org.springframework.http.ResponseEntity;

public interface RefreshTokenService
{
    RefreshToken createRefreshToken(String username);
    boolean isValidRefreshToken(RefreshToken refreshToken);
    boolean isAlreadyUser(RefreshToken refreshToken);

    ResponseEntity<?> createTokenByRefreshToken(String refreshToken);
}
