package com.dreamsol.services.impl;

import com.dreamsol.entities.RefreshToken;
import com.dreamsol.entities.User;
import com.dreamsol.repositories.RefreshTokenRepository;
import com.dreamsol.repositories.UserRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.securities.JwtHelper;
import com.dreamsol.securities.JwtResponse;
import com.dreamsol.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService
{
    public static final long REFRESH_TOKEN_VALIDITY = 24*60*60*1000;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtHelper jwtHelper;
    @Override
    public RefreshToken createRefreshToken(String username)
    {
        try
        {
            User user = userRepository.findByUserEmail(username);
            RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
            if(!Objects.isNull(refreshToken) && isValidRefreshToken(refreshToken))
                return refreshToken;
            if(isAlreadyUser(refreshToken))
            {
                refreshToken.setRefreshToken(UUID.randomUUID()+"."+UUID.randomUUID()+"."+UUID.randomUUID());
                refreshToken.setExpiry(System.currentTimeMillis()+REFRESH_TOKEN_VALIDITY);
                refreshTokenRepository.save(refreshToken);
                return refreshToken;
            }
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID()+"."+UUID.randomUUID()+"."+UUID.randomUUID())
                    .expiry(System.currentTimeMillis()+REFRESH_TOKEN_VALIDITY)
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }catch (Exception e)
        {
            throw new RuntimeException("Error occurred, Refresh Token not created, Reason: "+e.getMessage());
        }
    }
    public ResponseEntity<?> createTokenByRefreshToken(String refreshToken)
    {
        RefreshToken refreshTokenDB = refreshTokenRepository.findByRefreshToken(refreshToken);
        if(isValidRefreshToken(refreshTokenDB)) {
            User user = refreshTokenDB.getUser();
            String newToken = jwtHelper.generateToken(new UserDetailsImpl(user));
            JwtResponse jwtResponse = JwtResponse.builder()
                    .refreshToken(refreshToken)
                    .accessToken(newToken)
                    .username(user.getUserEmail())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error Occurred while creating new access token, Reason: Refresh token has been expired!",false));
    }
    public boolean isValidRefreshToken(RefreshToken refreshToken)
    {
        return refreshToken.getExpiry() > System.currentTimeMillis();
    }
    public boolean isAlreadyUser(RefreshToken refreshToken)
    {
        return !Objects.isNull(refreshToken);
    }
}
