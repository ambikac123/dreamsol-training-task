package com.dreamsol.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.dto.UserRequestDto;
import com.dreamsol.dto.UserResponseDto;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.ImageUploadService;
import com.dreamsol.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "This is user API")
public class UserController 
{
	@Autowired private UserService userService;
	
	@Autowired private ImageUploadService imageUploadService;
	
	@Value("${project.image}")
	private String path;
	
	@Operation(
		summary = "Create new user",
		description = "It is used to save data into database"
	)
	@PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> createUser(
			@Valid @RequestPart("userDto") UserRequestDto userRequestDto,
			@RequestParam("file") MultipartFile file
	)
	{
		return userService.createUser(userRequestDto,path,file);
	}
	
	@PutMapping("/update/{userId}")
	public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserRequestDto userRequestDto, @PathVariable("userId") Long userId)
	{
		return null;
	}
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId)
	{
		return null;
	}
	
	@GetMapping("/get/{userId}")
	public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId)
	{
		return userService.getUser(userId);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<List<UserResponseDto>> getAllUsers()
	{
		return null;
	}
	
	@GetMapping(value = "/download/{fileName}", produces = MediaType.ALL_VALUE)
	public void downloadFile(
			@PathVariable("fileName") String fileName, HttpServletResponse response
	) throws IOException, FileNotFoundException
	{
		InputStream resource = imageUploadService.getResource(path,fileName);
		response.setContentType(MediaType.ALL_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
