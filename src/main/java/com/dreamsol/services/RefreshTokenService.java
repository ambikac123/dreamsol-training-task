package com.dreamsol.services;

import com.dreamsol.entities.RefreshToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService
{
    RefreshToken createRefreshToken(UserDetails userDetails);
    boolean isValidRefreshToken(RefreshToken refreshToken);

    ResponseEntity<?> createTokenByRefreshToken(String refreshToken);
}
