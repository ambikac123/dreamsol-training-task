package com.dreamsol.services.impl;

import com.dreamsol.entities.RefreshToken;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.securities.JwtHelper;
import com.dreamsol.securities.JwtRequest;
import com.dreamsol.securities.JwtResponse;
import com.dreamsol.securities.SecurityConfig;
import com.dreamsol.services.RefreshTokenService;
import com.dreamsol.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService
{

    @Autowired UserDetailsService userDetailsService;
    @Autowired JwtHelper jwtHelper;
    @Autowired AuthenticationManager authenticationManager;
    @Autowired RefreshTokenService refreshTokenService;
    @Autowired SecurityConfig securityConfig;
    @Override
    public ResponseEntity<JwtResponse> login(JwtRequest request) {
            doAuthenticate(request.getUsername(), request.getPassword());
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtHelper.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
            JwtResponse response = JwtResponse.builder()
                    .accessToken(token)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            securityConfig.updateSecurityConfig();
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Override
    public ResponseEntity<?> logout() {

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Logged out successfully!",true));
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
