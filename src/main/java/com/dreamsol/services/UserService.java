package com.dreamsol.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.dto.UserRequestDto;
import com.dreamsol.dto.UserResponseDto;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.UserAllDataResponse;

import jakarta.validation.Valid;

public interface UserService 
{
	ResponseEntity<ApiResponse> createUser(UserRequestDto userRequestDto,String path,MultipartFile file);

	ResponseEntity<UserResponseDto> getUser(Long userId);

	ResponseEntity<ApiResponse> deleteUser(String path,Long userId);

	ResponseEntity<ApiResponse> updateUser(UserRequestDto userRequestDto, String path, MultipartFile file,Long userId);

	ResponseEntity<UserAllDataResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy,String sortDirection);
}
