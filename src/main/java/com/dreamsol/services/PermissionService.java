package com.dreamsol.services;

import com.dreamsol.dto.PermissionRequestDto;
import com.dreamsol.dto.PermissionResponseDto;
import com.dreamsol.entities.Permission;
import org.springframework.http.ResponseEntity;

public interface PermissionService
{

    ResponseEntity<?> createPermission(PermissionRequestDto permissionRequestDto);
    ResponseEntity<?> deletePermission(Long permissionId);
    ResponseEntity<?> updatePermission(PermissionRequestDto permissionRequestDto, Long permissionId);
    ResponseEntity<?> getPermission(Long permissionId);
    ResponseEntity<?> getAllPermission();

    Permission permissionRequestDtoToPermission(PermissionRequestDto permissionRequestDto);

    PermissionResponseDto permissionToPermissionResponseDto(Permission permission);
}
