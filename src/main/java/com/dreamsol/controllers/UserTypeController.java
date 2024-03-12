package com.dreamsol.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dreamsol.dto.UserTypeDto;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.UserTypeAllDataResponse;
import com.dreamsol.services.UserTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usertypes")
@Tag(name = "UserType", description = "This is usertype API")
public class UserTypeController 
{
	@Autowired private UserTypeService userTypeService;
	
	@Operation(
			summary = "Create new usertype",
			description = "It is used to save data into database"
	)
	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createDepartment(@Valid @RequestBody UserTypeDto userTypeDto)
	{
		return userTypeService.saveUserType(userTypeDto);
	}

	@Operation(
			summary = "delete existing usertype",
			description = "It is used to delete data from database"
	)
	@DeleteMapping("/delete/{userTypeId}")
	public ResponseEntity<UserTypeDto> deleteUserType(@PathVariable Long userTypeId)
	{
		return userTypeService.deleteUserType(userTypeId);
	}
	
	@Operation(
			summary = "Update existing usertype",
			description = "It is used to modify data into database"
	)
	@PutMapping("/update/{userTypeId}")
	public ResponseEntity<UserTypeDto> updateUserType(@Valid @RequestBody UserTypeDto userTypeDto,@PathVariable Long userTypeId)
	{
		return userTypeService.updateUserType(userTypeDto,userTypeId);
	}
	
	@Operation(
			summary = "Getting existing usertype",
			description = "It is used to retrieve single data from database"
	)
	@GetMapping("/get/{userTypeId}")
	public ResponseEntity<UserTypeDto> getUserType(@PathVariable Long userTypeId)
	{
		 return userTypeService.getUserType(userTypeId);
	}
	
	@Operation(
			summary = "Getting all usertype List",
			description = "It is used to retrieve all data from database"
	)
	@GetMapping("/get-all")
	public ResponseEntity<UserTypeAllDataResponse> getAllUserTypes(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "userTypeId", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection
	)
	{
		return userTypeService.getAllUserTypes(pageNumber,pageSize, sortBy, sortDirection);
	}
	
	@Operation(
			summary = "Search usertypes containing keywords",
			description = "It is used to search usertypes on the basis of usertype name/code containing given keyword"
			)
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserTypeDto>> searchUserTypes(@PathVariable String keywords)
	{
		return userTypeService.searchUserTypes(keywords);
	}
}
