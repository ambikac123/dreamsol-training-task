package com.dreamsol.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.dto.UserRequestDto;
import com.dreamsol.dto.UserResponseDto;
import com.dreamsol.response.ApiResponse;

public interface UserService 
{
	ResponseEntity<ApiResponse> createUser(UserRequestDto userRequestDto,String path,MultipartFile file);

	ResponseEntity<UserResponseDto> getUser(Long userId);
	
	ResponseEntity<List<UserResponseDto>> getAllUsers();
}
