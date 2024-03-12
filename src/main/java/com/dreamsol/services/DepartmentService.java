package com.dreamsol.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.dreamsol.dto.DepartmentDto;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.DepartmentAllDataResponse;

public interface DepartmentService
{
	ResponseEntity<ApiResponse> createDepartment(DepartmentDto departmentDto);
	
	ResponseEntity<DepartmentDto> updateDepartment(DepartmentDto departmentDto,Long departmentId);
	
	ResponseEntity<DepartmentDto> getDepartmentById(Long departmentId);
	
	ResponseEntity<DepartmentAllDataResponse> getAllDepartments(Integer pageNumber,Integer pageSize, String sortBy,String sortDirection);
	
	ResponseEntity<DepartmentDto> deleteDepartment(Long departmentId);

	ResponseEntity<List<DepartmentDto>> searchDepartments(String keyword);
}
