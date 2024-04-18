package com.dreamsol.services.impl;

import com.dreamsol.dto.PermissionRequestDto;
import com.dreamsol.dto.PermissionResponseDto;
import com.dreamsol.entities.Permission;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.PermissionRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PermissionServiceImpl implements PermissionService
{

    @Autowired private PermissionRepository permissionRepository;
    @Override
    public ResponseEntity<?> createPermission(PermissionRequestDto permissionRequestDto) {
        try{
            Permission permission = new Permission();
            BeanUtils.copyProperties(permissionRequestDto,permission);
            permission.setTimeStamp(LocalDateTime.now());
            permissionRepository.save(permission);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("New permission created successfully!",true));
        }catch (Exception e)
        {
            throw new RuntimeException("Error occurred while creating new permission, Reason: "+e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> deletePermission(Long permissionId)
    {
        try{
            Permission permission = permissionRepository.findById(permissionId).orElseThrow(()->new ResourceNotFoundException("Permission","permissionId",permissionId));
            permission.setStatus(false);
            //permissionRepository.delete(permission);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("permission with id: "+permissionId+" deleted successfully",true));
        }catch(Exception e)
        {
            throw new RuntimeException("Permission with id: "+permissionId+" not deleted, Reason: "+e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updatePermission(PermissionRequestDto permissionRequestDto, Long permissionId) {
        try{
            Permission permission = permissionRepository.findById(permissionId).orElseThrow(()->new ResourceNotFoundException("Permission","permissionId",permissionId));
            BeanUtils.copyProperties(permissionRequestDto,permission);
            permissionRepository.save(permission);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("permission with id: "+permissionId+" updated successfully!",true));
        }catch(Exception e)
        {
            throw new RuntimeException("Permission with id: "+permissionId+" not updated, Reason: "+e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getPermission(Long permissionId) {
            Permission permission = permissionRepository.findById(permissionId).orElseThrow(()->new ResourceNotFoundException("Permission","permissionId",permissionId));
            PermissionResponseDto permissionResponseDto = new PermissionResponseDto();
            BeanUtils.copyProperties(permission,permissionResponseDto);
            return ResponseEntity.status(HttpStatus.OK).body(permissionResponseDto);
    }

    @Override
    public ResponseEntity<?> getAllPermission() {
        try{
            List<Permission> permissionList = permissionRepository.findAll();
            if(permissionList.isEmpty())
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("No contents available!",true));

            List<PermissionResponseDto> permissionResponseDtoList = permissionList.stream()
                    .map(this::permissionToPermissionResponseDto).toList();
            return ResponseEntity.status(HttpStatus.OK).body(permissionResponseDtoList);
        }catch(Exception e)
        {
            throw new RuntimeException("Request not processed, Reason: "+e.getMessage());
        }
    }
    public Permission permissionRequestDtoToPermission(PermissionRequestDto permissionRequestDto)
    {
        try {
            Permission permission = permissionRepository.findByPermissionType(permissionRequestDto.getPermissionType());
            if (!Objects.isNull(permission))
                return permission;
            throw new ResourceNotFoundException("Permission", "permissionName:" + permissionRequestDto.getPermissionType(), 0);
        }catch(Exception e)
        {
            throw new RuntimeException("Error occurred while verifying permission, Reason: "+e.getMessage());
        }
    }
    public PermissionResponseDto permissionToPermissionResponseDto(Permission permission)
    {
        PermissionResponseDto permissionResponseDto = new PermissionResponseDto();
        BeanUtils.copyProperties(permission,permissionResponseDto);
        return permissionResponseDto;
    }
}
