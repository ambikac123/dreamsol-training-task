package com.dreamsol.services;

import org.springframework.http.ResponseEntity;

public interface EndpointService
{
    ResponseEntity<?> getAllEndpoints();
    ResponseEntity<?> updateEndpoints();
    ResponseEntity<?> getRoleRelatedEndpoints();
    ResponseEntity<?> getPermissionRelatedEndpoints();
}
