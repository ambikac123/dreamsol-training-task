package com.dreamsol.services;

import com.dreamsol.securities.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface SecurityService
{
    ResponseEntity<?> login(LoginRequest request);
}
