package com.dreamsol.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dreamsol.dto.UserTypeDto;
import com.dreamsol.entities.UserType;
import com.dreamsol.exceptions.ResourceAlreadyExistException;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.UserTypeRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.UserTypeAllDataResponse;
import com.dreamsol.services.UserTypeService;

@Service
public class UserTypeServiceImpl implements UserTypeService
{
	@Autowired private UserTypeRepository userTypeRepository;
	
	@Autowired private ModelMapper modelMapper;
	
	public ResponseEntity<ApiResponse> saveUserType(UserTypeDto userTypeDto) 
	{
		try {
			List<UserType> userTypeList = userTypeRepository.findByName(userTypeDto.getUserTypeName());
			if(userTypeList.isEmpty())
			{
				userTypeList = userTypeRepository.findByCode(userTypeDto.getUserTypeCode());
				if(userTypeList.isEmpty())
				{
					UserType userType = dtoToEntity(userTypeDto);
					userTypeRepository.save(userType);
					return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("UserType created successfully!!",true));
				}
				throw new ResourceAlreadyExistException("UserType name");
			}
			throw new ResourceAlreadyExistException("UserType code");
		}catch(ResourceAlreadyExistException ex)
		{
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ex.getMessage(),false));
		}
	}

	public ResponseEntity<UserTypeDto> updateUserType(UserTypeDto userTypeDto,Long userTypeId) 
	{
		UserType userType = userTypeRepository.findById(userTypeId).orElseThrow(()->new ResourceNotFoundException("UserType","userTypeId",userTypeId));
		BeanUtils.copyProperties(userTypeDto, userType, "userTypeId");
		UserType savedUserType = userTypeRepository.save(userType);
		return ResponseEntity.status(HttpStatus.OK).body(entityToDto(savedUserType));
	}
	
	public ResponseEntity<UserTypeDto> deleteUserType(Long userTypeId) 
	{
		UserType userType = userTypeRepository.findById(userTypeId).orElseThrow(()->new ResourceNotFoundException("UserType","userTypeId",userTypeId));
		userTypeRepository.delete(userType);
		return ResponseEntity.status(HttpStatus.OK).body(entityToDto(userType));
	}
	
	public ResponseEntity<UserTypeDto> getUserType(Long userTypeId) 
	{
		UserType userType = userTypeRepository.findById(userTypeId).orElseThrow(()->new ResourceNotFoundException("UserType","userTypeId",userTypeId));
		return ResponseEntity.status(HttpStatus.OK).body(entityToDto(userType));
	}

	public ResponseEntity<UserTypeAllDataResponse> getAllUserTypes(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) 
	{
		Sort sort = null;
		if(sortDirection.equalsIgnoreCase("asc"))
			sort = Sort.by(sortBy).ascending();
		else
			sort = Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
		Page<UserType> page = userTypeRepository.findAll(pageable);
		
		List<UserType> userTypeList = page.getContent();
		List<UserTypeDto> userTypeDtoList = userTypeList.stream().map((userType)->entityToDto(userType)).collect(Collectors.toList());
		UserTypeAllDataResponse userTypeAllDataResponse = new UserTypeAllDataResponse();
		userTypeAllDataResponse.setContents(userTypeDtoList);
		userTypeAllDataResponse.setPageNumber(page.getNumber());
		userTypeAllDataResponse.setPageSize(page.getSize());
		userTypeAllDataResponse.setTotalElements(page.getTotalElements());
		userTypeAllDataResponse.setTotalPages(page.getTotalPages());
		userTypeAllDataResponse.setFirstPage(page.isFirst());
		userTypeAllDataResponse.setLastPage(page.isLast());
		return ResponseEntity.status(HttpStatus.OK).body(userTypeAllDataResponse);
	}

	public ResponseEntity<List<UserTypeDto>> searchUserTypes(String keywords) {
		List<UserType> userTypeList = userTypeRepository.findByName("%"+keywords+"%");
		if(userTypeList.isEmpty())
		{
			userTypeList = userTypeRepository.findByCode("%"+keywords+"%");
		}
		List<UserTypeDto> userTypeDtoList = userTypeList.stream().map((userType)->entityToDto(userType)).collect(Collectors.toList());
		return ResponseEntity.ok(userTypeDtoList);
	}
	public UserTypeDto entityToDto(UserType userType) 
	{
        UserTypeDto userTypeDto = modelMapper.map(userType, UserTypeDto.class);
        return userTypeDto;
    }

    public UserType dtoToEntity(UserTypeDto userTypeDto) 
    {
        UserType userType = modelMapper.map(userTypeDto, UserType.class);
        return userType;
    }


}
