package com.dreamsol.services;

import com.dreamsol.dto.DepartmentResponseDto;
import com.dreamsol.dto.DepartmentRequestDto;
import com.dreamsol.entities.Department;
import com.dreamsol.response.AllDataResponse;
import com.dreamsol.response.ApiResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DepartmentService
{
	ResponseEntity<ApiResponse> createDepartment(DepartmentRequestDto departmentDto);
	ResponseEntity<ApiResponse> updateDepartment(DepartmentRequestDto departmentDto,Long departmentId);
	ResponseEntity<ApiResponse> deleteDepartment(Long departmentId);
	ResponseEntity<DepartmentResponseDto> getSingleDepartment(Long departmentId);
	ResponseEntity<AllDataResponse> searchDepartments(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection, String keyword);
	ResponseEntity<?> getCorrectAndIncorrectDepartmentList(MultipartFile file);
	ResponseEntity<?> saveCorrectList(List<DepartmentRequestDto> correctList);
	ResponseEntity<?> downloadDataFromDB();
	ResponseEntity<?> downloadDummyData();
	ResponseEntity<Resource> getDepartmentExcelSample();

	/* ----------------------------- Department Helper Methods --------------------------- */

	Department getDepartment(Long departmentId);
	Department getDepartment(DepartmentRequestDto departmentRequestDto);
	boolean isDepartmentExist(DepartmentRequestDto departmentRequestDto);
	DepartmentResponseDto departmentToDepartmentResponseDto(Department department);

    Department getDepartment(String departmentName, String departmentCode);

	/*
	List<Department> getDepartments(String keywords);
	Department dtoToEntity(DepartmentRequestDto departmentRequestDto);
	DepartmentSingleDataResponseDto departmentToDepartmentSingleDataResponseDto(Department department);
	ResponseEntity<DepartmentAllDataResponse> getAllDepartments(int pageNumber,int pageSize, String sortBy,String sortDirection);
	*/

}
