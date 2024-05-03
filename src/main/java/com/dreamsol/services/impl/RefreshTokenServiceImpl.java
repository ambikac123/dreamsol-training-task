package com.dreamsol.services.impl;

import com.dreamsol.entities.RefreshToken;
import com.dreamsol.repositories.RefreshTokenRepository;
import com.dreamsol.repositories.UserRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.securities.JwtHelper;
import com.dreamsol.securities.LoginResponse;
import com.dreamsol.securities.SecurityConfig;
import com.dreamsol.services.RefreshTokenService;
import com.dreamsol.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService
{
    @Autowired private UserService userService;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtHelper jwtHelper;
    @Autowired private SecurityConfig securityConfig;

    @Value("${jwt.validity.refresh-token}")
    private long REFRESH_TOKEN_VALIDITY;
    @Override
    public RefreshToken createRefreshToken(UserDetails userDetails)
    {
        try
        {
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
            RefreshToken refreshToken = refreshTokenRepository.findByUser(userDetailsImpl.getUser());
            if(refreshToken != null && isValidRefreshToken(refreshToken))
                return refreshToken;
            if(refreshToken != null)
            {
                refreshToken.setRefreshToken(UUID.randomUUID()+"."+UUID.randomUUID()+"."+UUID.randomUUID());
                refreshToken.setExpiry(System.currentTimeMillis()+REFRESH_TOKEN_VALIDITY);
                refreshTokenRepository.save(refreshToken);
                return refreshToken;
            }
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID()+"."+UUID.randomUUID()+"."+UUID.randomUUID())
                    .expiry(System.currentTimeMillis()+REFRESH_TOKEN_VALIDITY)
                    .user(userDetailsImpl.getUser())
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
        try
        {
            RefreshToken refreshTokenDB = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (refreshTokenDB == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Refresh token doesn't exist. Please! login again to get new token and refresh token.", false));
            }
            if (isValidRefreshToken(refreshTokenDB))
            {
                UserDetails userDetails = userDetailsService.loadUserByUsername(refreshTokenDB.getUser().getUserEmail());
                UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
                String newToken = jwtHelper.generateToken(userDetailsImpl);
                LoginResponse loginResponse = LoginResponse.builder()
                        .accessToken(newToken)
                        .refreshToken(refreshToken)
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error Occurred while creating new access token, Reason: Refresh token has been expired!", false));
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error Occurred while creating new access token, Reason: "+e.getMessage(), false));
        }
    }
    public boolean isValidRefreshToken(RefreshToken refreshToken)
    {
        return refreshToken.getExpiry() > System.currentTimeMillis();
    }
}
