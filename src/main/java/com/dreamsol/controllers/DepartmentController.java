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

import com.dreamsol.dto.DepartmentDto;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.DepartmentAllDataResponse;
import com.dreamsol.services.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department", description = "This is department API")
public class DepartmentController 
{
	@Autowired
	private DepartmentService departmentService;
	

	@Operation(
			summary = "Create new department",
			description = "It is used to save data into database"
	)
	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createDepartment(@Valid @RequestBody DepartmentDto departmentDto)
	{
		return departmentService.createDepartment(departmentDto);
	}
	
	@Operation(
			summary = "Update existing department",
			description = "It is used to modify data into database"
	)
	@PutMapping("/update/{deptId}")
	public ResponseEntity<DepartmentDto> updateDepartment(@Valid @RequestBody DepartmentDto departmentDto,@PathVariable("deptId") Long departmentId)
	{
		return departmentService.updateDepartment(departmentDto,departmentId);
	}
	
	@Operation(
			summary = "delete existing department",
			description = "It is used to delete data from database"
	)
	@DeleteMapping("/delete/{deptId}")
	public ResponseEntity<DepartmentDto> deleteDepartment(@Valid @PathVariable("deptId") Long departmentId)
	{
		return departmentService.deleteDepartment(departmentId);
	}
	
	@Operation(
			summary = "Getting existing department",
			description = "It is used to retrieve single data from database"
	)
	@GetMapping("/get/{deptId}")
	public ResponseEntity<DepartmentDto> getDepartmentById(@Valid @PathVariable("deptId") Long departmentId)
	{
		return departmentService.getDepartmentById(departmentId);
	}
	
	@Operation(
			summary = "Getting all departments List",
			description = "It is used to retrieve all data from database"
	)
	@GetMapping("/get-all")
	public ResponseEntity<DepartmentAllDataResponse> getAllDepartments(
			@RequestParam(value = "pageNumber",defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize",defaultValue = "3", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "departmentId", required = false) String sortBy,
			@RequestParam(value = "sortDirection", defaultValue = "asc", required = false) String sortDirection
	){
		return departmentService.getAllDepartments(pageNumber,pageSize,sortBy,sortDirection);
	}
	
	@Operation(
			summary = "Search departments containing keywords",
			description = "It is used to search departments on the basis of department name/code containing given keyword"
			)
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<DepartmentDto>> searchDepartments(@PathVariable String keywords)
	{
		return departmentService.searchDepartments(keywords);
	}

}
