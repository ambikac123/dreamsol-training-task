package com.dreamsol.services;

import com.dreamsol.dto.RoleRequestDto;
import org.springframework.http.ResponseEntity;

public interface RoleService
{
    ResponseEntity<?> createNewRole(RoleRequestDto roleRequestDto);
    ResponseEntity<?> updateRole(RoleRequestDto roleRequestDto, Long roleId);
    ResponseEntity<?> deleteRole(Long roleId);
    ResponseEntity<?> getRole(Long roleId);
    ResponseEntity<?> getAllRoles();

}
