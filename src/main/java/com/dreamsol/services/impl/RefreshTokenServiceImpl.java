package com.dreamsol.services.impl;

import com.dreamsol.entities.LoginUser;
import com.dreamsol.entities.RefreshToken;
import com.dreamsol.entities.User;
import com.dreamsol.repositories.LoginUserRepository;
import com.dreamsol.repositories.RefreshTokenRepository;
import com.dreamsol.repositories.UserRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.securities.JwtHelper;
import com.dreamsol.securities.LoginResponse;
import com.dreamsol.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService
{
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private LoginUserRepository loginUserRepository;
    @Autowired private JwtHelper jwtHelper;
    @Value("${jwt.validity.refresh-token}")
    private long REFRESH_TOKEN_VALIDITY;
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
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String clientIP = webAuthenticationDetails.getRemoteAddress();
        LoginUser loginUser = loginUserRepository.findByIpAddress(clientIP);
        if (loginUser==null)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("Please! Login, before accessing this resource",false));
        }
        RefreshToken refreshTokenDB = refreshTokenRepository.findByRefreshToken(refreshToken);
        if(refreshTokenDB==null)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Refresh token doesn't exist. Please! login again to get new token and refresh token.",false));
        }
        if(isValidRefreshToken(refreshTokenDB))
        {
            User user = refreshTokenDB.getUser();
            UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user,clientIP);
            String newToken = jwtHelper.generateToken(userDetailsImpl);
            LoginResponse loginResponse = LoginResponse.builder()
                    .accessToken(newToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
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
