package com.dreamsol.services;

import com.dreamsol.dto.RoleRequestDto;
import org.springframework.http.ResponseEntity;

public interface RoleService
{
    ResponseEntity<?> createNewRole(RoleRequestDto roleRequestDto);
    ResponseEntity<?> deleteRole(String roleName);
    ResponseEntity<?> getAllRoles();
}
