package com.dreamsol.services;

import com.dreamsol.dto.UserSingleDataResponseDto;
import com.dreamsol.entities.Department;
import com.dreamsol.entities.User;
import com.dreamsol.entities.UserImage;
import com.dreamsol.entities.UserType;
import com.dreamsol.response.UserExcelUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.dto.UserRequestDto;
import com.dreamsol.response.ApiResponse;

import java.util.List;

public interface UserService 
{
	ResponseEntity<ApiResponse> createUser(UserRequestDto userRequestDto,MultipartFile file,String imagePath);
	ResponseEntity<?> getSingleUser(Long userId);
	ResponseEntity<ApiResponse> deleteUser(String imagePath,Long userId);
	ResponseEntity<ApiResponse> updateUser(UserRequestDto userRequestDto, MultipartFile file,String imagePath,Long userId);
	ResponseEntity<?> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy,String sortDirection,String keywords);
	ResponseEntity<?> getCorrectAndIncorrectUserList(MultipartFile excelFile);
	ResponseEntity<?> saveCorrectList(List<UserExcelUploadResponse> correctList);
	ResponseEntity<Resource> downloadUserDataInExcel(String keywords);
	ResponseEntity<String> getUserImageAsBase64(String imageName,String imagePath);
	ResponseEntity<Resource> downloadUserImageAsFile(String imageName, String imagePath);
	ResponseEntity<?> uploadUserDocument(MultipartFile anyFile,String filePath,Long userId);
	ResponseEntity<Resource> downloadUserDocument(String fileName,String filePath);
	ResponseEntity<Resource> getUserExcelSample();

	/* ---------------------- User Helper Methods ----------------------------- */

	User getUser(Long userId);
	User getUser(UserRequestDto userRequestDto, UserType userType, Department department, UserImage userImage);
	User getUser(UserRequestDto userRequestDto);
	UserSingleDataResponseDto userToUserSingleDataResponseDto(User user);
}
