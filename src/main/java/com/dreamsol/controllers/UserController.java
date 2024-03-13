package com.dreamsol.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.dto.UserRequestDto;
import com.dreamsol.dto.UserResponseDto;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.UserAllDataResponse;
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
	
	@PutMapping(path = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> updateUser(
			@Valid 
			@RequestPart("userDto") UserRequestDto userRequestDto, 
			@RequestParam("file") MultipartFile file,
			@PathVariable("userId") Long userId
	)
	{
		return userService.updateUser(userRequestDto,path, file, userId);
	}
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId)
	{
		return userService.deleteUser(path,userId);
	}
	
	@GetMapping("/get/{userId}")
	public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId)
	{
		return userService.getUser(userId);
	}
	
	// To fetch all user details
	@GetMapping("/get-all")
	public ResponseEntity<UserAllDataResponse> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "userId", required = false) String sortBy,
			@RequestParam(value = "sortDirection",defaultValue = "asc", required = false) String sortDirection
	)
	{
		return userService.getAllUsers(pageNumber,pageSize,sortBy,sortDirection);
	}
	
	// To download image file
	@GetMapping(value = "/download/{fileName}" ,produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> downloadFile(
			@PathVariable("fileName") String fileName, HttpServletResponse response
	) throws IOException, FileNotFoundException
	{
		
		return ResponseEntity.ok(imageUploadService.getResource(path, fileName));
		
	}
	
	
}
