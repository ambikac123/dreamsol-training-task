package com.dreamsol.services.impl;

import com.dreamsol.dto.RoleRequestDto;
import com.dreamsol.dto.RoleResponseDto;
import com.dreamsol.entities.Endpoint;
import com.dreamsol.entities.Role;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.EndpointRepository;
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
    private EndpointRepository endpointRepository;

    @Override
    public ResponseEntity<?> createNewRole(RoleRequestDto roleRequestDto) {
        try {
            Role role = roleRepository.findByRoleType(roleRequestDto.getRoleType());
            if (Objects.isNull(role))
            {
                role = new Role();
                BeanUtils.copyProperties(roleRequestDto, role);
                role.setEndpoints(
                        roleRequestDto.getEndpoints()
                                .stream()
                                .map((endPointKey)-> endpointRepository.findById(endPointKey).orElseThrow(()->new ResourceNotFoundException("Endpoints",endPointKey,0)))
                                .toList()
                );
                role.setTimeStamp(LocalDateTime.now());
                roleRepository.save(role);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("New Role '" + roleRequestDto.getRoleType() + "' created successfully!", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(roleRequestDto.getRoleType() + " Role already exist!", false));
            }
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal Server Error, Reason: " + e.getMessage(), false));
        }
    }

    @Override
    public ResponseEntity<?> updateRole(RoleRequestDto roleRequestDto, Long roleId) {
        try{
            Role role = roleRepository.findByRoleIdAndStatusIsTrue(roleId).orElseThrow(()->new ResourceNotFoundException("Role","roleId",roleId));
            BeanUtils.copyProperties(roleRequestDto,role);
            role.setEndpoints(
                    roleRequestDto.getEndpoints()
                            .stream()
                            .map((endPointKey)-> endpointRepository.findById(endPointKey).orElseThrow(()->new ResourceNotFoundException("Endpoints",endPointKey,0)))
                            .toList()
            );
            role.setTimeStamp(LocalDateTime.now());
            roleRepository.save(role);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Role " + roleRequestDto.getRoleType() + " updated successfully!", true));
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal Server Error, Reason: " + e.getMessage(), false));
        }
    }

    @Override
    public ResponseEntity<?> deleteRole(Long roleId) {
        try{
            Role role = roleRepository.findByRoleIdAndStatusIsTrue(roleId).orElseThrow(()->new ResourceNotFoundException("Role","roleId",roleId));
            roleRepository.delete(role);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Role "+role.getRoleType()+" deleted successfully",true));
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Internal Server Error, Reason: " + e.getMessage(), false));
        }
    }

    @Override
    public ResponseEntity<?> getRole(Long roleId)
    {
        Role role = roleRepository.findByRoleIdAndStatusIsTrue(roleId).orElseThrow(()->new ResourceNotFoundException("Role","roleId",roleId));
        return ResponseEntity.status(HttpStatus.OK).body(roleToRoleResponseDto(role));
    }

    @Override
    public ResponseEntity<?> getAllRoles() {
        try{
            List<Role> roleList = roleRepository.findAllByStatusTrue();
            if(roleList.isEmpty())
               return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("No contents available!",false));
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
        roleResponseDto.setEndpoints(
                role.getEndpoints().stream().map(Endpoint::getEndPointLink).toList()
        );
        roleResponseDto.setTimeStamp(role.getTimeStamp());
        return roleResponseDto;
    }
}
