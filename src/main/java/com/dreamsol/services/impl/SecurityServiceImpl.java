package com.dreamsol.services.impl;

import com.dreamsol.entities.RefreshToken;
import com.dreamsol.securities.JwtHelper;
import com.dreamsol.securities.JwtRequest;
import com.dreamsol.securities.JwtResponse;
import com.dreamsol.services.RefreshTokenService;
import com.dreamsol.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService
{

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Override
    public ResponseEntity<JwtResponse> login(JwtRequest request) {
        doAuthenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtHelper.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
        JwtResponse response =JwtResponse.builder()
                .username(userDetails.getUsername())
                .accessToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Override
    public ResponseEntity<JwtResponse> logout() {
        return null;
    }

    private void doAuthenticate(String username, String password)
    {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e)
        {
            throw new BadCredentialsException("Invalid username or password !");
        }
    }
}
