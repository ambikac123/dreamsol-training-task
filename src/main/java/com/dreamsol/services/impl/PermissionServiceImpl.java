package com.dreamsol.services.impl;

import com.dreamsol.dto.PermissionRequestDto;
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

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService
{

    @Autowired private PermissionRepository permissionRepository;
    @Override
    public ResponseEntity<?> createPermission(PermissionRequestDto permissionRequestDto) {
        try{
            Permission permission = new Permission();
            BeanUtils.copyProperties(permissionRequestDto,permission);
            permissionRepository.save(permission);
            return ResponseEntity.status(HttpStatus.OK).body(permission);
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
            permissionRepository.delete(permission);
            return ResponseEntity.status(HttpStatus.OK).body(permission);
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
            return ResponseEntity.status(HttpStatus.OK).body(permission);
        }catch(Exception e)
        {
            throw new RuntimeException("Permission with id: "+permissionId+" not updated, Reason: "+e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getPermission(Long permissionId) {
            Permission permission = permissionRepository.findById(permissionId).orElseThrow(()->new ResourceNotFoundException("Permission","permissionId",permissionId));
            return ResponseEntity.status(HttpStatus.OK).body(permission);
    }

    @Override
    public ResponseEntity<?> getAllPermission() {
        try{
            List<Permission> permissionList = permissionRepository.findAll();
            if(permissionList.isEmpty())
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("No contents available!",true));
            return ResponseEntity.status(HttpStatus.OK).body(permissionList);
        }catch(Exception e)
        {
            throw new RuntimeException("Request not processed, Reason: "+e.getMessage());
        }
    }
}
