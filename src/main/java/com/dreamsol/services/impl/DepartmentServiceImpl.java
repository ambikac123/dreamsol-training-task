package com.dreamsol.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamsol.dto.DepartmentRequestDto;
import com.dreamsol.entities.Department;
import com.dreamsol.exceptions.ResourceAlreadyExistException;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.DepartmentRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.DepartmentAllDataResponse;
import com.dreamsol.services.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService
{
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public ResponseEntity<ApiResponse> createDepartment(DepartmentRequestDto departmentDto) {
		try {
				List<Department> departmentList = departmentRepository.findByName(departmentDto.getDepartmentName());
				if(departmentList.isEmpty())
				{
					departmentList = departmentRepository.findByCode(departmentDto.getDepartmentCode());
					if(departmentList.isEmpty())
					{
						Department department = this.dtoToEntity(departmentDto);
						departmentRepository.save(department);
						return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Department created successfully!!",true));
					}
					throw new ResourceAlreadyExistException("Department Code Matched");
				}
				throw new ResourceAlreadyExistException("Department Name Matched");
		}catch(ResourceAlreadyExistException ex)
		{
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ex.getMessage(),false));
		}
	}

	public ResponseEntity<DepartmentRequestDto> updateDepartment(DepartmentRequestDto departmentDto, Long departmentId) {
		Department department = departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("Department","Id",departmentId));
		modelMapper.map(departmentDto,department,"departmentId");
		
		Department updatedDepartment = departmentRepository.save(department);
		return ResponseEntity.status(HttpStatus.OK).body(entityToDto(updatedDepartment));
	}
	
	public ResponseEntity<DepartmentRequestDto> deleteDepartment(Long departmentId) {
		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(()->new ResourceNotFoundException("Department","Id",departmentId));
		this.departmentRepository.delete(department);
		return ResponseEntity.status(HttpStatus.OK).body(entityToDto(department));
	}

	public ResponseEntity<DepartmentRequestDto> getDepartmentById(Long departmentId) {
		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(()->new ResourceNotFoundException("Department","Id",departmentId));
		return ResponseEntity.status(HttpStatus.OK).body(entityToDto(department));
	}

	public ResponseEntity<DepartmentAllDataResponse> getAllDepartments(Integer pageNumber, Integer pageSize,String sortBy,String sortDirection) 
	{
		
		Sort sort = null;
		if(sortDirection.equalsIgnoreCase("asc"))
			sort = Sort.by(sortBy).ascending();
		else if(sortDirection.equalsIgnoreCase("desc"))
			sort = Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
		Page<Department> page = departmentRepository.findAll(pageable);
		
		List<Department> departmentList = page.getContent();
		List<DepartmentRequestDto> departmentDtoList = departmentList.stream().map((department)->this.entityToDto(department)).collect(Collectors.toList());
		DepartmentAllDataResponse departmentAllDataResponse = new DepartmentAllDataResponse();
		departmentAllDataResponse.setContents(departmentDtoList);
		departmentAllDataResponse.setPageNumber(page.getNumber());
		departmentAllDataResponse.setPageSize(page.getSize());
		departmentAllDataResponse.setTotalElements(page.getTotalElements());
		departmentAllDataResponse.setTotalPages(page.getTotalPages());
		departmentAllDataResponse.setFirstPage(page.isFirst());
		departmentAllDataResponse.setLastPage(page.isLast());
		return ResponseEntity.status(HttpStatus.OK).body(departmentAllDataResponse);
	}
	
	public ResponseEntity<List<DepartmentRequestDto>> searchDepartments(String keyword) {
		
		List<Department> departmentList = departmentRepository.findByName("%"+keyword+"%");
		 
		if(departmentList.isEmpty())
		{
			departmentList = departmentRepository.findByCode("%"+keyword+"%");
		}
		List<DepartmentRequestDto> departmentDtoList = departmentList.stream().map((department)-> entityToDto(department)).collect(Collectors.toList());
		return ResponseEntity.ok(departmentDtoList);
	}
	
	public Department dtoToEntity(DepartmentRequestDto departmentDto)
	{
		Department department = modelMapper.map(departmentDto, Department.class);	
		return department;
	}
	
	public DepartmentRequestDto entityToDto(Department department)
	{
		DepartmentRequestDto departmentDto = modelMapper.map(department, DepartmentRequestDto.class);
		return departmentDto;
	}
	
    
}
