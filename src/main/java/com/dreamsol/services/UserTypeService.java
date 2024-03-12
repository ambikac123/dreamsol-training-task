package com.dreamsol.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.dreamsol.dto.UserTypeDto;
import com.dreamsol.entities.UserType;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.UserTypeAllDataResponse;

public interface UserTypeService 
{
	ResponseEntity<ApiResponse> saveUserType(UserTypeDto userTypeDto);
	ResponseEntity<UserTypeDto> updateUserType(UserTypeDto userTypeDto,Long userTypeId);
	ResponseEntity<UserTypeDto> deleteUserType(Long userTypeId);
	ResponseEntity<UserTypeDto> getUserType(Long userTypeId);
	ResponseEntity<UserTypeAllDataResponse> getAllUserTypes(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
	ResponseEntity<List<UserTypeDto>> searchUserTypes(String keywords);
	UserTypeDto entityToDto(UserType userType);
	UserType dtoToEntity(UserTypeDto userTypeDto);
}
