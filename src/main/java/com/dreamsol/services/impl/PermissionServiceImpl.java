package com.dreamsol.services.impl;

import com.dreamsol.dto.PermissionRequestDto;
import com.dreamsol.dto.PermissionResponseDto;
import com.dreamsol.entities.EndpointMappings;
import com.dreamsol.entities.Permission;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.helpers.EndpointMappingsHelper;
import com.dreamsol.repositories.PermissionRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class PermissionServiceImpl implements PermissionService
{
    private PermissionRepository permissionRepository;
    private EndpointMappingsHelper endpointMappingsHelper;
    @Override
    public ResponseEntity<?> createPermission(PermissionRequestDto permissionRequestDto) {
        try{
            Permission permission = permissionRepository.findByPermissionType(permissionRequestDto.getPermissionType());
            if(Objects.isNull(permission))
            {
                Map<String,String> endPointmap = endpointMappingsHelper.getEndpointMappings();
                permission = new Permission();
                BeanUtils.copyProperties(permissionRequestDto,permission);
                permission.setEndPoints(
                        permissionRequestDto.getEndPoints()
                                .stream()
                                .map((endPointKey)->new EndpointMappings(endPointKey,endPointmap.get(endPointKey)))
                                .toList()
                );
                permission.setTimeStamp(LocalDateTime.now());
                permissionRepository.save(permission);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("New permission created successfully!",true));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(permissionRequestDto.getPermissionType() + " Role already exist!", false));
            }
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
            if(!Objects.isNull(permission))
            {
                Map<String,String> endPointmap = endpointMappingsHelper.getEndpointMappings();
                BeanUtils.copyProperties(permissionRequestDto,permission);
                permission.setEndPoints(
                        permissionRequestDto.getEndPoints()
                                .stream()
                                .map((endPointKey)->new EndpointMappings(endPointKey,endPointmap.get(endPointKey)))
                                .toList()
                );
                permission.setTimeStamp(LocalDateTime.now());
                permissionRepository.save(permission);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("permission updated successfully!",true));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(permissionRequestDto.getPermissionType() + " permission already exist!", false));
            }
        }catch(Exception e)
        {
            throw new RuntimeException("Permission with id: "+permissionId+" not updated, Reason: "+e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getPermission(Long permissionId) {
            Permission permission = permissionRepository.findByPermissionIdAndStatusIsTrue(permissionId).orElseThrow(()->new ResourceNotFoundException("Permission","permissionId",permissionId));
            PermissionResponseDto permissionResponseDto = new PermissionResponseDto();
            BeanUtils.copyProperties(permission,permissionResponseDto);
            permissionResponseDto.setEndPoints(
                    permission.getEndPoints()
                            .stream()
                            .map((EndpointMappings::getEndPointKey))
                            .toList()
            );
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
        permissionResponseDto.setEndPoints(
                permission.getEndPoints().stream()
                        .map(EndpointMappings::getEndPointKey)
                        .toList()
        );
        return permissionResponseDto;
    }
}
