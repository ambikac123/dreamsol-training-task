package com.dreamsol.services.impl;

import com.dreamsol.entities.RefreshToken;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.securities.JwtHelper;
import com.dreamsol.securities.LoginRequest;
import com.dreamsol.securities.LoginResponse;
import com.dreamsol.services.RefreshTokenService;
import com.dreamsol.services.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class SecurityServiceImpl implements SecurityService
{
    private AuthenticationManager authenticationManager;
    private JwtHelper jwtHelper;
    private RefreshTokenService refreshTokenService;
    private Authentication authenticateUsernameAndPassword(String username, String password)
    {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,password);
        try{
            return authenticationManager.authenticate(authentication);
        }catch (BadCredentialsException e)
        {
            throw new BadCredentialsException(" Invalid username or password !");
        }
    }
    @Override
    public ResponseEntity<?> login(LoginRequest request)
    {
        try {
            SecurityContextHolder.clearContext();
            Authentication authentication = authenticateUsernameAndPassword(request.getUsername(), request.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtHelper.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails);
            LoginResponse loginResponse = LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Login Error: " + e.getMessage(), false));
        }
    }
}
