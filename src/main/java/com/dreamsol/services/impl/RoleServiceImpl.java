package com.dreamsol.services.impl;

import com.dreamsol.dto.RoleRequestDto;
import com.dreamsol.dto.RoleResponseDto;
import com.dreamsol.entities.Role;
import com.dreamsol.repositories.RoleRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class RoleServiceImpl implements RoleService
{
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> createNewRole(RoleRequestDto roleRequestDto) {
        try {
            Role role = roleRepository.findByRoleName(roleRequestDto.getRoleName());
            if (Objects.isNull(role)) {
                role = new Role();
                BeanUtils.copyProperties(roleRequestDto, role);
                role.setTimeStamp(LocalDateTime.now());
                roleRepository.save(role);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("New Role '" + roleRequestDto.getRoleName() + "' created successfully!", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(roleRequestDto.getRoleName() + " Role already exist!", false));
            }
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal Server Error, Reason: " + e.getMessage(), false));
        }
    }

    @Override
    public ResponseEntity<?> deleteRole(String roleName) {
        try{
            Role role = roleRepository.findByRoleName(roleName);
            if(!Objects.isNull(role))
            {
                role.setStatus(false);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Role "+role.getRoleName()+" deleted successfully",true));
            }
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal Server Error, Reason: " + e.getMessage(), false));
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getAllRoles() {
        try{
            List<Role> roleList = roleRepository.findAllByStatusTrue();
            List<RoleResponseDto> roleResponseDtoList = roleList.stream().map(this::roleToRoleResponseDto).toList();
            return ResponseEntity.status(HttpStatus.OK).body(roleResponseDtoList);
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Internal Server Error, Reason: "+e.getMessage(),false));
        }
    }
    /* ------------------------------- Role Helper Methods -------------------------------------- */
    public RoleResponseDto roleToRoleResponseDto(Role role)
    {
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        BeanUtils.copyProperties(role,roleResponseDto);
        roleResponseDto.setTimeStamp(role.getTimeStamp());
        return roleResponseDto;
    }
}
