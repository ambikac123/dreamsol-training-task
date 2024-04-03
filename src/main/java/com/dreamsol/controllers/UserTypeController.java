package com.dreamsol.controllers;

import com.dreamsol.dto.UserTypeResponseDto;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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

import com.dreamsol.dto.UserTypeRequestDto;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.UserTypeAllDataResponse;
import com.dreamsol.services.UserTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
	public ResponseEntity<ApiResponse> createUserType(@Valid @RequestBody UserTypeRequestDto userTypeRequestDto)
	{
		return userTypeService.createUserType(userTypeRequestDto);
	}

	@Operation(
			summary = "delete existing usertype",
			description = "It is used to delete data from database"
	)
	@DeleteMapping("/delete/{userTypeId}")
	public ResponseEntity<ApiResponse> deleteUserType(@PathVariable Long userTypeId)
	{
		return userTypeService.deleteUserType(userTypeId);
	}
	
	@Operation(
			summary = "Update existing usertype",
			description = "It is used to modify data into database"
	)
	@PutMapping("/update/{userTypeId}")
	public ResponseEntity<ApiResponse> updateUserType(@Valid @RequestBody UserTypeRequestDto userTypeRequestDto,@PathVariable Long userTypeId)
	{
		return userTypeService.updateUserType(userTypeRequestDto,userTypeId);
	}
	
	@Operation(
			summary = "Get existing usertype",
			description = "It is used to retrieve single data from database"
	)
	@GetMapping("/get/{userTypeId}")
	public ResponseEntity<UserTypeResponseDto> getUserType(@PathVariable Long userTypeId)
	{
		 return userTypeService.getSingleUserType(userTypeId);
	}
	
	/*@Operation(
			summary = "Getting all usertype List",
			description = "It is used to retrieve all data from database"
	)
	@GetMapping("/get-all")
	public ResponseEntity<UserTypeAllDataResponse> getAllUserTypes(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) @Min(0) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) @Min(1) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "userTypeId", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection
	)
	{
		return userTypeService.getAllUserTypes(pageNumber,pageSize, sortBy, sortDirection);
	}*/
	
	@Operation(
			summary = "Search usertypes containing keywords",
			description = "It is used to search usertypes on the basis of usertype name/code containing given keyword"
			)
	@GetMapping("/search")
	public ResponseEntity<UserTypeAllDataResponse> searchUserTypes(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) @Min(0) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) @Min(1) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "userTypeId", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection,
			@RequestParam(value = "keywords", defaultValue = "", required = false) String keywords
	)
	{
		return userTypeService.searchUserTypes(pageNumber,pageSize,sortBy,sortDirection,keywords);
	}

	@Operation(
			summary = "Validate excel data and get correct and incorrect list",
			description = "It is used to upload an excel file for filtering correct and incorrect data"
	)
	@PostMapping(value = "/validate-excel-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> getCorrectAndIncorrectUserTypes(@RequestParam MultipartFile excelFile)
	{
		return userTypeService.getCorrectAndIncorrectUserTypeList(excelFile);
	}

	@Operation(
			summary = "Save list of correct data",
			description = "This api will help to save list of correct data into database"
	)
	@PostMapping(value = "/save-correct-list")
	public ResponseEntity<?> saveCorrectList(@RequestBody List<UserTypeRequestDto> correctList)
	{
		return userTypeService.saveCorrectList(correctList);
	}
	@Operation(
			summary = "Download usertype data from database as excel",
			description = "It is used to extract usertype data from database , store in an excel file and return."
	)
	@GetMapping(value = "/download-excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<?> downloadUserTypeDataAsExcel()
	{
		return userTypeService.downloadDataFromDB();
	}
	@Operation(
			summary = "Download excel sample ",
			description = "This api provides a sample for excel file to save bulk data"
	)
	@GetMapping(value = "/download-excel-sample")
	public ResponseEntity<Resource> downloadUserTypeExcelSample()
	{
		return userTypeService.getUserTypeExcelSample();
	}
}
