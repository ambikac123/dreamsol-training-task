package com.dreamsol.services.impl;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.dreamsol.dto.UserResponseDto;
import com.dreamsol.dto.UserTypeResponseDto;
import com.dreamsol.entities.User;
import com.dreamsol.exceptions.ResourceAlreadyExistException;
import com.dreamsol.helpers.ExcelHeadersInfo;
import com.dreamsol.helpers.ExcelHelper;
import com.dreamsol.helpers.GlobalHelper;
import com.dreamsol.helpers.PageInfo;
import com.dreamsol.response.AllDataResponse;
import com.dreamsol.response.ExcelUploadResponse;
import com.dreamsol.response.UserTypeExcelUploadResponse;
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

import com.dreamsol.dto.UserTypeRequestDto;
import com.dreamsol.entities.UserType;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.UserTypeRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.UserTypeService;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserTypeServiceImpl implements UserTypeService
{
	private UserTypeRepository userTypeRepository;
	private GlobalHelper globalHelper;
	public ResponseEntity<ApiResponse> createUserType(UserTypeRequestDto userTypeRequestDto)
	{
		try{
			if(!isUserTypeExist(userTypeRequestDto))
			{
				UserType userType = userTypeRequestDtoToUserType(userTypeRequestDto);
				userType.setTimeStamp(LocalDateTime.now());
				userTypeRepository.save(userType);
				return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("New usertype created successfully!",true));
			}
			else {
				throw new ResourceAlreadyExistException("usertype");
			}
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error occurred while creating usertype: "+e.getMessage(),false));
		}
	}
	public ResponseEntity<ApiResponse> updateUserType(UserTypeRequestDto userTypeRequestDto,Long userTypeId)
	{
		try{
			UserType userType = getUserType(userTypeId);
			BeanUtils.copyProperties(userTypeRequestDto,userType);
			userType.setTimeStamp(LocalDateTime.now());
			userTypeRepository.save(userType);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("usertype with id "+userTypeId+" updated successfully!",true));
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error occurred while updating usertype : "+e.getMessage(),false));
		}

	}
	
	public ResponseEntity<ApiResponse> deleteUserType(Long userTypeId)
	{
		try{
			UserType userType = getUserType(userTypeId);
			List<User> userList = globalHelper.getUsers(userType);
			for(User user : userList)
			{
				user.setUserType(null);
			}
			userTypeRepository.delete(userType);
			//userType.setStatus(false);
			return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("usertype with id "+userTypeId+" deleted successfully!", true));
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error occurred while deleting usertype : "+e.getMessage(),false));
		}
	}
	
	public ResponseEntity<UserTypeResponseDto> getSingleUserType(Long userTypeId)
	{
		try{
			UserType userType = getUserType(userTypeId);
			UserTypeResponseDto userTypeResponseDto = userTypeToUserTypeResponseDto(userType);
			return ResponseEntity.status(HttpStatus.OK).body(userTypeResponseDto);
		}catch(Exception e){
			throw new ResourceNotFoundException("usertype","userTypeId",userTypeId);
		}
	}

	// Code to filter list of User Types using keywords
	public ResponseEntity<AllDataResponse> searchUserTypes(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection, String keywords)
	{
		Sort sort = sortDirection.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
		Page<UserType> page = userTypeRepository.findByUserTypeNameLikeOrUserTypeCodeLike("%"+keywords+"%","%"+keywords+"%",pageable);
		List<UserType> userTypeList = page.getContent();
		List<UserTypeResponseDto> userTypeResponseDtoList = userTypeList.stream()
				.map(this::userTypeToUserTypeResponseDto).toList();
		AllDataResponse allDataResponse = new AllDataResponse();
		allDataResponse.setContents(userTypeResponseDtoList);
		PageInfo pageInfo = new PageInfo(
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages(),
			page.isFirst(),page.isLast());
		allDataResponse.setPageInfo(pageInfo);
		return ResponseEntity.status(HttpStatus.OK).body(allDataResponse);
	}

	public ResponseEntity<?> getCorrectAndIncorrectUserTypeList(MultipartFile excelFile)
	{
		if (ExcelHelper.checkExcelFormat(excelFile))
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Invalid file format", false));
		}
		Map<String,String> headersMap = ExcelHeadersInfo.getUserTypeHeadersMap();
		ExcelHelper<UserTypeExcelUploadResponse> excelHelper = new ExcelHelper<>();
		List<UserTypeExcelUploadResponse> list = excelHelper.convertExcelToList(excelFile,headersMap,UserTypeExcelUploadResponse.class);
		ExcelUploadResponse excelUploadResponse = GlobalHelper.getCorrectAndIncorrectList(list,UserTypeExcelUploadResponse.class);
		return ResponseEntity.ok(excelUploadResponse);
	}

	public ResponseEntity<?> saveCorrectList(List<UserTypeRequestDto> correctList)
	{
		try {
			List<UserType> userTypeList = correctList.stream().map(this::userTypeRequestDtoToUserType).toList();
			userTypeRepository.saveAll(userTypeList);
			return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Data added successfully!",true));
		}catch(Exception e)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),false));
		}
	}

	public ResponseEntity<?> downloadDataFromDB()
	{
		Map<String,String> headersMap = ExcelHeadersInfo.getUserTypeHeadersMap();
		String sheetName = "usertype_data";
		List<UserType> userTypeList = userTypeRepository.findAll();
		if(!userTypeList.isEmpty())
		{
			ByteArrayInputStream byteArrayInputStream = ExcelHelper.convertListToExcel(UserType.class,userTypeList, headersMap, sheetName);
			Resource resource = new InputStreamResource(byteArrayInputStream);
			String fileName = "usertype_data.xlsx";
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + fileName)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(resource);
		}
		else {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return  ResponseEntity.ok()
					.headers(httpHeaders)
					.body(new ApiResponse("No contents available!",false));
		}
	}

	public ResponseEntity<Resource> getUserTypeExcelSample()
	{
		Map<String,String> headers = ExcelHeadersInfo.getUserTypeHeadersMap();

		Map<String,String>  headersType = ExcelHeadersInfo.getUserTypeHeaderTypeMap();

		Map<String,String> examples = ExcelHeadersInfo.getUserTypeDataExampleMap();

		String sheetName = "usertype_sample";
		Resource resource = ExcelHelper.getExcelSample(headers,headersType,examples,sheetName);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+sheetName+".xlsx")
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.body(resource);
	}
	/* ---------------------- UserType Helper Methods ---------------------- */

	public UserType getUserType(long userTypeId)
	{
		return userTypeRepository.findById(userTypeId).orElseThrow(()->new ResourceNotFoundException("usertype","userTypeId",userTypeId));
	}
	public UserType getUserType(UserTypeRequestDto userTypeRequestDto)
	{
		String userTypeName = userTypeRequestDto.getUserTypeName();
		String userTypeCode = userTypeRequestDto.getUserTypeCode();
		return getUserType(userTypeName,userTypeCode);
	}
	public UserType getUserType(String userTypeName,String userTypeCode)
	{
		return userTypeRepository.findByUserTypeNameAndUserTypeCode(userTypeName,userTypeCode);
	}
	public UserTypeResponseDto userTypeToUserTypeResponseDto(UserType userType)
	{
		UserTypeResponseDto userTypeResponseDto = new UserTypeResponseDto();
		BeanUtils.copyProperties(userType,userTypeResponseDto);
		List<UserResponseDto> userResponseDtoList = globalHelper.getUsers(userType).stream()
				.map(globalHelper::userToUserResponseDto)
				.toList();
		userTypeResponseDto.setUsers(userResponseDtoList);
		return userTypeResponseDto;
	}
	public UserType userTypeRequestDtoToUserType(UserTypeRequestDto userTypeRequestDto)
	{
		UserType userType = new UserType();
		BeanUtils.copyProperties(userTypeRequestDto,userType);
		return userType;
	}
	public boolean isUserTypeExist(UserTypeRequestDto userTypeRequestDto)
	{
		String userTypeName = userTypeRequestDto.getUserTypeName();
		String userTypeCode = userTypeRequestDto.getUserTypeCode();
		UserType userType = userTypeRepository.findByUserTypeNameAndUserTypeCode(userTypeName,userTypeCode);
		if(Objects.isNull(userType))
			return false;
		return userType.isStatus();
	}
}
