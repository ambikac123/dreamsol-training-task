package com.dreamsol.services.impl;

import com.dreamsol.dto.EndpointResponseDto;
import com.dreamsol.entities.Endpoint;
import com.dreamsol.entities.RefreshToken;
import com.dreamsol.helpers.EndpointMappingsHelper;
import com.dreamsol.repositories.EndpointRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.securities.JwtHelper;
import com.dreamsol.securities.LoginRequest;
import com.dreamsol.securities.LoginResponse;
import com.dreamsol.securities.SecurityConfig;
import com.dreamsol.services.RefreshTokenService;
import com.dreamsol.services.SecurityService;
import io.jsonwebtoken.Claims;
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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SecurityServiceImpl implements SecurityService
{
    @Autowired AuthenticationManager authenticationManager;
    @Autowired SecurityConfig securityConfig;
    @Autowired UserDetailsService userDetailsService;
    @Autowired RefreshTokenService refreshTokenService;
    @Autowired
    EndpointRepository endpointRepository;
    @Autowired JwtHelper jwtHelper;
    @Autowired private EndpointMappingsHelper endpointMappingsHelper;

    private Authentication authenticateUsernameAndPassword(String username, String password)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        try{
            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (BadCredentialsException e)
        {
            throw new BadCredentialsException("Invalid username or password !");
        }
    }
    @Override
    public ResponseEntity<?> login(LoginRequest request) {
            Authentication authentication = authenticateUsernameAndPassword(request.getUsername(), request.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtHelper.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
            securityConfig.updateSecurityConfig();
            LoginResponse response = LoginResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Override
    public ResponseEntity<?> logout()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Logged out successfully!",true));
    }

    @Override
    public ResponseEntity<?> getAllEndpoints() {
        List<EndpointResponseDto> endpointResponseDtoList = endpointRepository.findAll()
                .stream()
                .map((endpointMappings -> new EndpointResponseDto(endpointMappings.getEndPointKey(), endpointMappings.getEndPointLink())))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(endpointResponseDtoList);
    }

    @Override
    public ResponseEntity<?> updateEndpoints()
    {
        int count = 0;
        Map<String,String> endpointMap = endpointMappingsHelper.getEndpointMap();
        for(Map.Entry<String,String> endpoint : endpointMap.entrySet())
        {
            Endpoint endpointMappings = new Endpoint(endpoint.getKey(),endpoint.getValue());
            endpointRepository.save(endpointMappings);
            count++;
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(count+" endpoints updated!",true));
    }
}
