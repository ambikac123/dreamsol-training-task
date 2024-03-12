package com.dreamsol.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.dto.UserRequestDto;
import com.dreamsol.dto.UserResponseDto;
import com.dreamsol.entities.User;
import com.dreamsol.exceptions.ResourceAlreadyExistException;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.UserRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.ImageUploadService;
import com.dreamsol.services.UserService;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired private UserRepository userRepository;
	@Autowired private ModelMapper modelMapper;
	@Autowired private ImageUploadService imageUploadService;
	public ResponseEntity<ApiResponse> createUser(UserRequestDto userRequestDto,String path,MultipartFile file) 
	{
		try {
				List<User> userList = userRepository.findByMobile(userRequestDto.getUserMobile());
				if(userList.size()==0)
				{
					userList = userRepository.findByEmail(userRequestDto.getUserEmail());
					if(userList.size()==0)
					{
						User user = modelMapper.map(userRequestDto,User.class);
						String fileName= file.getOriginalFilename();
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
	
	public ResponseEntity<UserResponseDto> getUser(Long userId)
	{
		User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
		UserResponseDto userResponseDto = new UserResponseDto();
		BeanUtils.copyProperties(user, userResponseDto);
		return ResponseEntity.ok(userResponseDto);
	}

	@Override
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}
}
