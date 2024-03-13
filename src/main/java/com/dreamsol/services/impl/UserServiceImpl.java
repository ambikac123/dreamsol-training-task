package com.dreamsol.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.dto.UserRequestDto;
import com.dreamsol.dto.UserResponseDto;
import com.dreamsol.entities.User;
import com.dreamsol.exceptions.ResourceAlreadyExistException;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.UserRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.UserAllDataResponse;
import com.dreamsol.services.ImageUploadService;
import com.dreamsol.services.UserService;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired private UserRepository userRepository;
	@Autowired private ModelMapper modelMapper;
	@Autowired private ImageUploadService imageUploadService;
	
	// To create new user
	public ResponseEntity<ApiResponse> createUser(UserRequestDto userRequestDto,String path,MultipartFile file) 
	{
		String fileName= file.getOriginalFilename();
		if(fileName==null)
			return ResponseEntity.internalServerError().body(new ApiResponse("image file not selected!!",false));
		try {
				User user = userRepository.findByUserMobile(userRequestDto.getUserMobile());
				if(Objects.isNull(user))
				{
					user = userRepository.findByUserEmail(userRequestDto.getUserEmail());
					if(Objects.isNull(user))
					{
						user = modelMapper.map(userRequestDto,User.class);
						
						String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
						if(fileExtension.equals(".jpg")||fileExtension.equals(".jpeg")||fileExtension.equals(".png"))
						{
							User savedUser = userRepository.save(user);
							imageUploadService.uploadImage(path,file,savedUser);

							return ResponseEntity.ok(new ApiResponse("User created successfully!!",true));
						}
						return ResponseEntity.ok(new ApiResponse("File format invalid !!",true));
					}
					throw new ResourceAlreadyExistException("Email");
				}
				throw new ResourceAlreadyExistException("Mobile No.");
		}catch(ResourceAlreadyExistException ex)
		{
			return ResponseEntity.ok(new ApiResponse(ex.getMessage(),false));
		}
	}
	
	// To fetch single user detail
	public ResponseEntity<UserResponseDto> getUser(Long userId)
	{
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
		UserResponseDto userResponseDto = new UserResponseDto();
		BeanUtils.copyProperties(user, userResponseDto);
		return ResponseEntity.ok(userResponseDto);
	}

	// To delete user details
	@Override
	public ResponseEntity<ApiResponse> deleteUser(String path,Long userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
		String fileName = user.getImageURI();
		if(fileName==null)
		{
			userRepository.delete(user);
			return ResponseEntity.ok(new ApiResponse("User deleted successfully!!",true));
		}
		else if(imageUploadService.deleteImage(path, fileName))
		{
			userRepository.delete(user);
			return ResponseEntity.ok(new ApiResponse("User deleted successfully!!",true));
		}
		return ResponseEntity.ok(new ApiResponse("User not deleted !!",false));
	}

	// To update user details
	@Override
	public ResponseEntity<ApiResponse> updateUser(UserRequestDto userRequestDto,String path, MultipartFile file,Long userId) 
	{
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
		user.setUserName(userRequestDto.getUserName());
		user.setUserMobile(userRequestDto.getUserMobile());
		user.setUserEmail(userRequestDto.getUserEmail());
		String fileName = file.getOriginalFilename();
		if(fileName!=null)
		{
			String oldFileName = user.getImageURI();
			if(imageUploadService.deleteImage(path, oldFileName))
			{
				imageUploadService.uploadImage(path, file, user);
				return ResponseEntity.ok(new ApiResponse("User updated successfully!!",true));
			}
		}
		userRepository.save(user);
		return ResponseEntity.ok(new ApiResponse("User updated successfully!!",true));
	}


	@Override
	public ResponseEntity<UserAllDataResponse> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy,String sortDirection) {
		Sort sort = null;
		if(sortDirection.equalsIgnoreCase("asc"))
		{
			sort = Sort.by(sortBy).ascending();
		}
		else if(sortDirection.equalsIgnoreCase("desc"))
		{
			sort = Sort.by(sortBy).descending();
		}
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> page = userRepository.findAll(pageable);
		List<User> userList = page.getContent();
		List<UserResponseDto> userResponseDtoList = userList.stream().map((user)->entityToUserResponseDto(user)).collect(Collectors.toList());
		
		UserAllDataResponse  userAllDataResponse = new UserAllDataResponse();
		userAllDataResponse.setContents(userResponseDtoList);
		userAllDataResponse.setPageNumber(page.getNumber());
		userAllDataResponse.setPageSize(page.getSize());
		userAllDataResponse.setTotalElements(page.getTotalElements());
		userAllDataResponse.setTotalPages(page.getTotalPages());
		userAllDataResponse.setFirstPage(page.isFirst());
		userAllDataResponse.setLastPage(page.isLast());
		return ResponseEntity.ok(userAllDataResponse);
	}
	
	public UserResponseDto entityToUserResponseDto(User user)
	{
		UserResponseDto  userResponseDto = new UserResponseDto();
		userResponseDto.setUserName(user.getUserName());
		userResponseDto.setUserEmail(user.getUserEmail());
		userResponseDto.setUserMobile(user.getUserMobile());
		userResponseDto.setImageURI(user.getImageURI());
		return userResponseDto;
	}

}
