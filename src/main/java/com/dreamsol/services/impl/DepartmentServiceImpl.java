package com.dreamsol.services.impl;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dreamsol.dto.UserResponseDto;
import com.dreamsol.entities.User;
import com.dreamsol.helpers.ExcelHeadersInfo;
import com.dreamsol.helpers.ExcelHelper;
import com.dreamsol.helpers.GlobalHelper;
import com.dreamsol.response.AllDataResponse;
import com.dreamsol.response.DepartmentExcelUploadResponse;
import com.dreamsol.response.ExcelUploadResponse;
import com.dreamsol.threads.DepartmentDataSaveThread;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamsol.dto.DepartmentRequestDto;
import com.dreamsol.dto.DepartmentResponseDto;
import com.dreamsol.exceptions.ResourceAlreadyExistException;
import com.dreamsol.helpers.PageInfo;
import com.dreamsol.entities.Department;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.DepartmentRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.DepartmentService;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("ALL")
@Service
@AllArgsConstructor(onConstructor_ ={@Autowired} )
public class DepartmentServiceImpl implements DepartmentService
{
	private DepartmentRepository departmentRepository;
	private GlobalHelper globalHelper;
	private ExcelHelper excelHelper;
	public ResponseEntity<ApiResponse> createDepartment(DepartmentRequestDto departmentRequestDto)
	{
		try {
			if (!isDepartmentExist(departmentRequestDto))
			{
				Department department = new Department();
				BeanUtils.copyProperties(departmentRequestDto,department);
				department.setTimeStamp(LocalDateTime.now());
				departmentRepository.save(department);
				return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("New department created successfully!", true));
			} else {
				throw new ResourceAlreadyExistException("department");
			}
		}catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error occurred while creating department : "+e.getMessage(),false));
		}
	}
	public ResponseEntity<ApiResponse> updateDepartment(DepartmentRequestDto departmentRequestDto, Long departmentId)
	{
		try{
			Department department = getDepartment(departmentId);
			BeanUtils.copyProperties(departmentRequestDto,department);
			department.setTimeStamp(LocalDateTime.now());
			departmentRepository.save(department);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("department with id "+departmentId+" updated successfully!",true));
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error occurred while updating department : "+e.getMessage(),false));
		}
	}
	public ResponseEntity<ApiResponse> deleteDepartment(Long departmentId)
    {
		try
		{
			Department department = getDepartment(departmentId);
			List<User> userList = globalHelper.getUsers(department);
			for(User user : userList)
			{
				user.setDepartment(null);
			}
			departmentRepository.delete(department);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("department with id "+departmentId+" deleted successfully!", true));
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error occurred while deleting department : "+e.getMessage(),false));
		}
	}
	public ResponseEntity<DepartmentResponseDto> getSingleDepartment(Long departmentId)
	{
		try
		{
			Department department = getDepartment(departmentId);
			DepartmentResponseDto departmentResponseDto = departmentToDepartmentResponseDto(department);
			return ResponseEntity.status(HttpStatus.OK).body(departmentResponseDto);
		}catch(Exception e)
		{
			throw new ResourceNotFoundException("department","departmentId",departmentId);
		}
	}
	public ResponseEntity<AllDataResponse> searchDepartments(Integer pageNumber,Integer pageSize,String sortBy,String sortDirection,String keywords)
	{
		Sort sort = sortDirection.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
		Page<Department> page = departmentRepository.findByDepartmentNameLikeOrDepartmentCodeLike("%"+keywords+"%","%"+keywords+"%",pageable);
		List<Department> departmentList = page.getContent();
		if(departmentList.isEmpty())
			throw new ResourceNotFoundException("No contents available!");
		List<DepartmentResponseDto> departmentResponseDtoList = departmentList.stream().map(this::departmentToDepartmentResponseDto).toList();
		AllDataResponse allDataResponse = new AllDataResponse();
		allDataResponse.setContents(departmentResponseDtoList);
		PageInfo pageInfo = new PageInfo(
				page.getNumber(),
				page.getSize(),
				page.getTotalElements(),
				page.getTotalPages(),
				page.isFirst(),page.isLast());
		allDataResponse.setPageInfo(pageInfo);
		return ResponseEntity.status(HttpStatus.OK).body(allDataResponse);
	}
	public ResponseEntity<?> getCorrectAndIncorrectDepartmentList(MultipartFile file)
	{
			if (ExcelHelper.checkExcelFormat(file))
			{
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Invalid file format", false));
			}
			ExcelHelper<DepartmentExcelUploadResponse> excelHelper = new ExcelHelper<>();
			Map<String,String> headersMap =  ExcelHeadersInfo.getDepartmentHeadersMap();
			List<DepartmentExcelUploadResponse> list = excelHelper.convertExcelToList(file,headersMap,DepartmentExcelUploadResponse.class);
			if(Objects.isNull(list))
				return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("invalid excel format",false));
			ExcelUploadResponse excelUploadResponse = globalHelper.getCorrectAndIncorrectList(list,DepartmentExcelUploadResponse.class);
			return ResponseEntity.ok(excelUploadResponse);
	}
	public ResponseEntity<?> saveCorrectList(List<DepartmentRequestDto> correctList)
	{
		try{
			if(correctList.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("No departments into the list",false));
			List<Department> departmentList = correctList.stream().map(this::departmentRequestDtoToDepartment).toList();
			departmentRepository.saveAll(departmentList);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Data added successfully!",true));
		}catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),false));
		}
	}
	public ResponseEntity<?> downloadDataFromDB()
	{
		Map<String,String> headersMap = ExcelHeadersInfo.getDepartmentHeadersMap();
		String sheetName = "department_data";
		List<Department> departmentList = departmentRepository.findAll();
		if(!departmentList.isEmpty())
		{
			ByteArrayInputStream byteArrayInputStream = excelHelper.convertListToExcel(Department.class,departmentList, headersMap, sheetName);
			Resource resource = new InputStreamResource(byteArrayInputStream);
			String fileName = "department_data.xlsx";
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + fileName)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(resource);
		} else {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return  ResponseEntity.ok()
					.headers(httpHeaders)
					.body(new ApiResponse("No contents available!",false));
		}
	}
	public ResponseEntity<Resource> downloadDummyData(int noOfRecords)
	{
		Map<String,String> headersMap = ExcelHeadersInfo.getDepartmentHeadersMap();
		Resource resource = excelHelper.getAutoGeneratedExcelFile(headersMap,DepartmentExcelUploadResponse.class,noOfRecords);
		String fileName = "department_data.xlsx";
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment;fileName="+fileName)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(resource);
	}
	public ResponseEntity<Resource> getDepartmentExcelSample()
	{
		Map<String,String> headers = ExcelHeadersInfo.getDepartmentHeadersMap();
		Map<String,String>  headersType = ExcelHeadersInfo.getDepartmentHeaderTypeMap();
		Map<String,String> examples = ExcelHeadersInfo.getDepartmentDataExampleMap();

		String sheetName = "department_sample";
		Resource resource = ExcelHelper.getExcelSample(headers,headersType,examples,sheetName);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+sheetName+".xlsx")
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(resource);

	}

	/* ----------------------------------- Department Helper Methods ---------------------------------*/

	public Department getDepartment(Long departmentId)
	{
		return departmentRepository.findById(departmentId).orElseThrow(()->new ResourceNotFoundException("department","departmentId",departmentId));
	}
	public Department getDepartment(DepartmentRequestDto departmentRequestDto)
	{
		String departmentName = departmentRequestDto.getDepartmentName();
		String departmentCode = departmentRequestDto.getDepartmentCode();
		return departmentRepository.findByDepartmentNameAndDepartmentCode(departmentName,departmentCode);
	}
	public void saveDataByBatchProcessing(List<Department> departmentList)
	{
		int totalSize = departmentList.size();
		int batchSize = 1000;

		int numThreads = (totalSize + batchSize - 1) / batchSize;

		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		for (int count = 0; count < totalSize; count += batchSize)
        {
			int endIndex = Math.min(count + batchSize, totalSize);
			List<Department> batch = departmentList.subList(count, endIndex);

			executor.submit(new DepartmentDataSaveThread(departmentRepository, batch));
		}
		executor.shutdown();
		try
		{
			executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	public boolean isDepartmentExist(DepartmentRequestDto departmentRequestDto)
	{
		String departmentName = departmentRequestDto.getDepartmentName();
		String departmentCode = departmentRequestDto.getDepartmentCode();
		Department department = getDepartment(departmentName,departmentCode);
		if(Objects.isNull(department))
			return false;
		return department.isStatus();
	}

	public Department departmentRequestDtoToDepartment(DepartmentRequestDto departmentRequestDto)
	{
		Department department = new Department();
		BeanUtils.copyProperties(departmentRequestDto,department);
		department.setTimeStamp(LocalDateTime.now());
		return department;
	}
	public DepartmentResponseDto departmentToDepartmentResponseDto(Department department)
	{
		DepartmentResponseDto departmentResponseDto = new DepartmentResponseDto();
		BeanUtils.copyProperties(department,departmentResponseDto);
		List<UserResponseDto> userResponseDtoList = globalHelper.getUsers(department)
				.stream()
				.map((user)->globalHelper.userToUserResponseDto(user))
				.toList();
		departmentResponseDto.setUsers(userResponseDtoList);
		return departmentResponseDto;
	}
	public Department getDepartment(String departmentName, String departmentCode)
	{
		return departmentRepository.findByDepartmentNameAndDepartmentCode(departmentName,departmentCode);
	}
}
/*public boolean isExistName(String name)
	{
		return Objects.isNull(departmentRepository.findByDepartmentName(name));
	}
	public boolean isExistCode(String code)
	{
		return Objects.isNull(departmentRepository.findByDepartmentCode(code));
	}
	public boolean isActive(boolean status)
	{
		return status;
	}*/