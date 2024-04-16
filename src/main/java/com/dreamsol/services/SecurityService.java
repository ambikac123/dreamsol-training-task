package com.dreamsol.services;

import com.dreamsol.securities.JwtRequest;
import com.dreamsol.securities.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface SecurityService
{
    ResponseEntity<JwtResponse> login(JwtRequest request);
    ResponseEntity<JwtResponse> logout();
}
