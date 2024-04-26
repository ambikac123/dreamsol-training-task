package com.dreamsol.services.impl;

import com.dreamsol.dto.EndpointResponseDto;
import com.dreamsol.entities.LoginUser;
import com.dreamsol.entities.Endpoint;
import com.dreamsol.entities.RefreshToken;
import com.dreamsol.helpers.EndpointMappingsHelper;
import com.dreamsol.repositories.LoginUserRepository;
import com.dreamsol.repositories.EndpointRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.securities.JwtHelper;
import com.dreamsol.securities.LoginRequest;
import com.dreamsol.securities.LoginResponse;
import com.dreamsol.securities.SecurityConfig;
import com.dreamsol.services.RefreshTokenService;
import com.dreamsol.services.SecurityService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SecurityServiceImpl implements SecurityService
{
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private SecurityConfig securityConfig;
    @Autowired private SecurityFilterChain securityFilterChain;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private RefreshTokenService refreshTokenService;
    @Autowired private EndpointRepository endpointRepository;
    @Autowired private LoginUserRepository loginUserRepository;
    @Autowired private JwtHelper jwtHelper;
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
    public ResponseEntity<?> login(LoginRequest request)
    {
        LoginUser loginUser = getLoginUser();
        Authentication authentication = authenticateUsernameAndPassword(request.getUsername(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        userDetailsImpl.setIpAddress(loginUser.getIpAddress());
        try {
            loginUser.setUsername(userDetails.getUsername());
            loginUser.setLoginAt(LocalDateTime.now());
            loginUserRepository.save(loginUser);
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Login Failed!",false));
        }
        String token = jwtHelper.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        securityConfig.updateSecurityConfig();
        LoginResponse response = LoginResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    private LoginUser getLoginUser()
    {
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String clientIP = webAuthenticationDetails.getRemoteAddress();
        LoginUser loginUser = new LoginUser();
        loginUser.setIpAddress(clientIP);
        return loginUser;
    }
    @Override
    public ResponseEntity<?> logout()
    {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            LoginUser loginUser = loginUserRepository.findByUsername(userDetails.getUsername());
            if(loginUser!=null)
            {
                loginUserRepository.delete(loginUser);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("You have logged out successfully", true));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("You have already logged out!", false));
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(e.getMessage(), false));
        }
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
