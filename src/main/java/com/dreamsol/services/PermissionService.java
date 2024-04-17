package com.dreamsol.services;

import com.dreamsol.dto.PermissionRequestDto;
import org.springframework.http.ResponseEntity;

public interface PermissionService
{

    ResponseEntity<?> createPermission(PermissionRequestDto permissionRequestDto);
    ResponseEntity<?> deletePermission(Long permissionId);
    ResponseEntity<?> updatePermission(PermissionRequestDto permissionRequestDto, Long permissionId);
    ResponseEntity<?> getPermission(Long permissionId);
    ResponseEntity<?> getAllPermission();
}
