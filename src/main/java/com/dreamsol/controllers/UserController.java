package com.dreamsol.controllers;

import java.util.List;

import com.dreamsol.response.UserExcelUploadResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "This API handles all required operation related to user entity")
@Validated
public class UserController 
{
	@Autowired private UserService userService;

	@Value("${project.image}")
	private String imagePath;

	@Value("${project.file}")
	private String filePath;

    @Operation(
		summary = "Create a new user",
		description = "It is used to create new user data with image into database"
	)
	@PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> createUser(
			@Valid
			@RequestPart("userRequestDto") UserRequestDto userRequestDto,
			@RequestParam("file") @NotNull(message = "File must not be null")MultipartFile file
	)
	{
		return userService.createUser(userRequestDto,file,imagePath);
	}
	
	@Operation(
			summary = "Update existing user's data ",
			description = "This api helps to modify user's data into database"
		)
	@PutMapping(path = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse> updateUser(
			@Valid 
			@RequestPart("userRequestDto") UserRequestDto userRequestDto,
			@RequestParam("file") MultipartFile file,
			@PathVariable("userId") Long userId
	)
	{
		return userService.updateUser(userRequestDto,file,imagePath,userId);
	}
	
	@Operation(
			summary = "Delete existing user data",
			description = "It is used to delete user data from database"
		)
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId)
	{
		return userService.deleteUser(imagePath,userId);
	}
	
	@Operation(
			summary = "Get single user's data",
			description = "This api helps to get details of an existing user"
		)
	@GetMapping("/get/{userId}")
	public ResponseEntity<?> getSingleUser(@PathVariable Long userId)
	{
		return userService.getSingleUser(userId);
	}
	
	@Operation(
			summary = "Get all records of users from database.",
			description = "This api helps to get all users data from database as JSON object"
		)
	@GetMapping("/get-all")
	public ResponseEntity<?> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "userId", required = false) String sortBy,
			@RequestParam(value = "sortDirection",defaultValue = "asc", required = false) String sortDirection,
			@Pattern(regexp = "(name|email|mobile|usertype|department)", message = "Use any one value from [name/email/mobile/usertype/department]")
			@RequestParam(value = "searchBy", defaultValue = "name", required = false) String searchBy,
			@RequestParam(value ="keywords", defaultValue = "", required = false) String keywords
	)
	{
		return userService.getAllUsers(pageNumber,pageSize,sortBy,sortDirection,searchBy,keywords);
	}

	/*@Operation(
			summary = "Download user's image as string ",
			description = "This api helps to download an image as string in Base64 format"
		)
	@GetMapping(value = "/download-image-string/{imageName}" ,produces = MediaType.TEXT_PLAIN_VALUE)
	@Hidden
	public ResponseEntity<String> downloadImageFileAsBase64(
			@PathVariable("imageName") String imageName
	)
	{
		return userService.getUserImageAsBase64(imageName,imagePath);
	}*/

	@Operation(
			summary = "Download user image as file",
			description = "This api helps to download an image as file"
	)
	@GetMapping(value = "/download-image-file/{imageName}")
	@Hidden
	public ResponseEntity<Resource> downloadImageFile(@PathVariable("imageName") String imageName)
	{
		return userService.downloadUserImageAsFile(imageName,imagePath);
	}
	@Operation(
			summary = "Upload any type attachment",
			description = "This api helps to upload and save user's attachment"
	)
	@PostMapping(value = "/upload-file/{userId}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadFile(@PathVariable("userId") Long userId, @RequestParam("anyFile") MultipartFile anyFile)
	{
		return userService.uploadUserDocument(anyFile,filePath,userId);
	}
	@Operation(
			summary = "Download existing attachment",
			description = "This api helps to download user's existing attachment."
	)
	@GetMapping(value = "/download-file/{fileName}", produces = MediaType.ALL_VALUE)
	public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName)
	{
		return userService.downloadUserDocument(fileName,filePath);
	}
	@Operation(
			summary = "Validate excel data and get correct and incorrect data",
			description = "This api helps to upload an excel file containing user's data and returns an JSON object containing two separate lists (correct and incorrect data)"
		)
	@PostMapping(value = "/validate-excel-data", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> getCorrectAndIncorrectUsersFromExcel(@RequestParam MultipartFile excelFile)
	{
		return userService.getCorrectAndIncorrectUserList(excelFile);
	}
	@Operation(
			summary = "Save list of correct data",
			description = "This api will help to save the correct data into database"
	)
	@PostMapping(value = "/save-correct-list")
	public ResponseEntity<?> saveCorrectList(
			@Valid
			@RequestBody
			@Size(min = 1, message = "No users into the list")
			List<UserExcelUploadResponse> correctList)
	{
		return userService.saveCorrectList(correctList);
	}
	@Operation(
			summary = "download user data from database as excel ",
			description = "This api helps to download user's matching data with keywords as excel file ."
	)
	@GetMapping(value = "/download-excel")
	public ResponseEntity<Resource> downloadUserDataAsExcel(
			@Pattern(regexp = "name|email|mobile|usertype|department")
			@RequestParam(value ="searchBy", defaultValue = "name", required = false) String searchBy,
			@RequestParam(value = "keywords", defaultValue = "", required = false) String keywords)
	{
		return userService.downloadUserDataInExcel(searchBy,keywords);
	}
	@Operation(
			summary = "Download user excel sample ",
			description = "This api provides a sample for excel file to save bulk data"
	)
	@GetMapping(value = "/download-excel-sample")
	public ResponseEntity<Resource> downloadUserExcelSample()
	{
		return userService.getUserExcelSample();
	}

	@Operation(
			summary = "Download auto generated user dummy data as excel",
			description = "It is used to get dummy data"
	)
	@GetMapping(value = "/download-excel-dummy", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<?> downloadUserDummyDataAsExcel(@RequestParam(value = "noOfRecords", defaultValue = "10", required = false) int noOfRecords)
	{
		return userService.downloadDummyData(noOfRecords);
	}
}