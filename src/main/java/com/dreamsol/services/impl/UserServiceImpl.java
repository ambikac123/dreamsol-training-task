package com.dreamsol.services.impl;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.dreamsol.dto.DepartmentSingleDataResponseDto;
import com.dreamsol.dto.DocumentSingleDataResponseDto;
import com.dreamsol.dto.UserRequestDto;
import com.dreamsol.dto.UserSingleDataResponseDto;
import com.dreamsol.dto.UserTypeSingleDataResponseDto;
import com.dreamsol.entities.Department;
import com.dreamsol.entities.Document;
import com.dreamsol.entities.User;
import com.dreamsol.entities.UserImage;
import com.dreamsol.entities.UserType;
import com.dreamsol.helpers.ExcelHeadersInfo;
import com.dreamsol.helpers.ExcelHelper;
import com.dreamsol.helpers.GlobalHelper;
import com.dreamsol.helpers.ImageHelper;
import com.dreamsol.helpers.PageInfo;
import com.dreamsol.repositories.DepartmentRepository;
import com.dreamsol.repositories.DocumentRepository;
import com.dreamsol.repositories.UserImageRepository;
import com.dreamsol.repositories.UserRepository;
import com.dreamsol.repositories.UserTypeRepository;
import com.dreamsol.response.ExcelUploadResponse;
import com.dreamsol.response.UserExcelUploadResponse;
import com.dreamsol.services.DepartmentService;
import com.dreamsol.services.DocumentService;
import com.dreamsol.services.UserService;
import com.dreamsol.services.UserTypeService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.UserAllDataResponse;

@Service
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl implements UserService
{
    private UserRepository userRepository;
	private DepartmentRepository departmentRepository;
    private UserTypeRepository userTypeRepository;
    private DocumentRepository documentRepository;
    private UserImageRepository userImageRepository;
    private DocumentService documentService;
    private DepartmentService departmentService;
    private UserTypeService userTypeService;
    private ImageHelper imageHelper;

	public ResponseEntity<ApiResponse> createUser(UserRequestDto userRequestDto, MultipartFile file,String imagePath)
	{
		try
        {
            User user = getUser(userRequestDto);
            if(Objects.isNull(user))
            {
                UserType userType = userTypeService.getUserType(userRequestDto.getUserType());
                Department department = departmentService.getDepartment(userRequestDto.getDepartment());
                UserImage userImage = imageHelper.getImage(file,imagePath);
                userImage.setTimeStamp(LocalDateTime.now());
                user = getUser(userRequestDto,userType,department,userImage);
                user.setTimeStamp(LocalDateTime.now());
                try{
                    userRepository.save(user);
                }catch (DataAccessException e)
                {
                    imageHelper.deleteImage(userImage.getDuplicateImageName(),imagePath);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("user not created, Reason: "+e.getMessage(),false));
                }
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("New user created successfully!",true));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User already exist!",false));
		}
        catch(Exception e)
        {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),false));
        }
	}
    public ResponseEntity<ApiResponse> updateUser(UserRequestDto userRequestDto,MultipartFile file,String imagePath,Long userId)
    {
        try
        {
                User user = getUser(userId);
                if(!Objects.isNull(user))
                {
                    UserType userType = userTypeService.getUserType(userRequestDto.getUserType());
                    Department department = departmentService.getDepartment(userRequestDto.getDepartment());
                    UserImage userImage = user.getUserImage();
                    if(!Objects.equals(file.getOriginalFilename(), ""))
                    {
                        if(!Objects.isNull(userImage))
                        {
                            imageHelper.deleteImage(userImage.getDuplicateImageName(),imagePath);
                        }
                        userImage = imageHelper.getImage(file,imagePath);
                        userImage.setTimeStamp(LocalDateTime.now());
                        userImage.setStatus(true);

                    }

                    user = getUser(userRequestDto,userType,department,userImage);
                    user.setUserId(userId);
                    user.setTimeStamp(LocalDateTime.now());
                    try{
                        userRepository.save(user);
                    }catch (DataAccessException e)
                    {
                        imageHelper.deleteImage(userImage.getDuplicateImageName(),imagePath);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("user with id "+userId+" not updated, Reason: "+e.getMessage(),false));
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user with id "+userId+" updated successfully!",true));
                }
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("No user found!",false));
        }
        catch(Exception e)
        {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),false));
        }
    }
	@Override
	public ResponseEntity<ApiResponse> deleteUser(String imagePath,Long userId)
    {
        try
        {
            User user = getUser(userId);
            if(!Objects.isNull(user.getUserImage())) {
                String duplicateImageName = user.getUserImage().getDuplicateImageName();
                imageHelper.deleteImage(duplicateImageName, imagePath);
            }
            userRepository.delete(user);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully!!", true));
        }catch(Exception e) {
            return ResponseEntity.ok(new ApiResponse("User not deleted !!", false));
        }
	}
    public ResponseEntity<?> getSingleUser(Long userId)
    {
        User user = getUser(userId);
        UserSingleDataResponseDto userSingleDataResponseDto = userToUserSingleDataResponseDto(user);
        return ResponseEntity.ok(userSingleDataResponseDto);
    }

	@Override
	public ResponseEntity<?> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy,String sortDirection,String keywords)
    {
        try{

            Sort sort = sortDirection.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

            Page<User> page = userRepository.findByUserNameLikeOrUserEmailLike("%" + keywords + "%", "%" + keywords + "%",pageable);
            if(page.getSize()==0)
            {
                page = userRepository.findByUserMobileLike("%"+keywords+"%",pageable);
                if(page.getSize()==0)
                {
                    List<UserType> userTypeList = userTypeRepository.findByUserTypeNameLikeOrUserTypeCodeLike("%" + keywords + "%", "%" + keywords + "%");
                    page = userRepository.findAllByUserTypeIn(userTypeList,pageable);
                    if(page.getSize()==0)
                    {
                        List<Department> departmentList = departmentRepository.findByDepartmentNameLikeOrDepartmentCodeLike("%" + keywords + "%", "%" + keywords + "%");
                        page = userRepository.findAllByDepartmentIn(departmentList,pageable);
                    }
                }
            }
            List<User> userList = page.getContent();
            if(!userList.isEmpty())
            {
                UserAllDataResponse  userAllDataResponse = new UserAllDataResponse();
                List<UserSingleDataResponseDto> userSingleDataResponseDtoList = userList.stream().map(this::userToUserSingleDataResponseDto).collect(Collectors.toList());
                userAllDataResponse.setContents(userSingleDataResponseDtoList);
                PageInfo pageInfo = new PageInfo(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isFirst(),page.isLast());
                userAllDataResponse.setPageInfo(pageInfo);
                return ResponseEntity.status(HttpStatus.OK).body(userAllDataResponse);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("No users list found! ",false));

        }catch(RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),false));
        }
	}

    /*public ResponseEntity<ApiResponse> saveUsers(List<UserRequestDto> userRequestDtoList)
    {
        try
        {
            List<User> userList = userRequestDtoList.stream().map(this::userRequestDtoToUser).toList();
            userRepository.saveAll(userList);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("All users saved successfully!",true));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error Occurred: users not saved!, Reason: "+e.getMessage(),false));
        }
    }*/

    public ResponseEntity<String> getUserImageAsBase64(String imageName,String imagePath)
    {
        byte[] fileBytes = GlobalHelper.getResource(imageName,imagePath);
        String fileString = Base64.getEncoder().encodeToString(fileBytes);
        return ResponseEntity.status(HttpStatus.OK).body(fileString);
    }
    public ResponseEntity<Resource> downloadUserImageAsFile(String imageName, String imagePath)
    {
        try
        {
            UserImage userImage = userImageRepository.findByDuplicateImageName(imageName);
            byte[] fileBytes = GlobalHelper.getResource(imageName,imagePath);
            Resource imageResource = new ByteArrayResource(fileBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentDispositionFormData("attachment", userImage.getOriginalImageName());
            return ResponseEntity.ok().headers(headers).body(imageResource);
        }catch(Exception e)
        {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    public ResponseEntity<?> getCorrectAndIncorrectUserList(MultipartFile excelFile)
    {
        if(ExcelHelper.checkExcelFormat(excelFile))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Invalid file format!",false));
        }
        ExcelHelper<UserExcelUploadResponse> excelHelper = new ExcelHelper<>();
        Map<String,String> headersMap = ExcelHeadersInfo.getUserHeadersMap();
        List<UserExcelUploadResponse> list = excelHelper.convertExcelToList(excelFile,headersMap,UserExcelUploadResponse.class);
        ExcelUploadResponse excelUploadResponse = GlobalHelper.getCorrectAndIncorrectList(list,UserExcelUploadResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(excelUploadResponse);
    }
    public ResponseEntity<?> saveCorrectList(List<UserExcelUploadResponse> correctList)
    {
        try {
            List<User> userList = correctList.stream().map(this::userExcelUploadResponseToUser).toList();
            userRepository.saveAll(userList);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Data added successfully!",true));
        }catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),false));
        }
    }
    public ResponseEntity<Resource> downloadUserDocument(String fileName,String filePath)
    {
        try {
            Document document = documentRepository.findByDocumentDuplicateName(fileName);
            Path path = Paths.get(filePath, fileName);
            Resource fileResource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getDocumentOriginalName())
                    .body(fileResource);
        }catch(Exception e)
        {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    public ResponseEntity<?> uploadUserDocument(MultipartFile anyFile,String filePath,Long userId)
    {
        User user = getUser(userId);
        return documentService.uploadDocument(anyFile,user,filePath);
    }
    public ResponseEntity<Resource> downloadUserDataInExcel(String keywords)
    {
        Map<String,String> headersMap = ExcelHeadersInfo.getUserHeadersMap();

        String sheetName = "user_data";

        List<User> userList = userRepository.findByUserNameLikeOrUserEmailLike("%" + keywords + "%", "%" + keywords + "%");
        if(userList.isEmpty())
        {
            userList = userRepository.findByUserMobileLike("%" + keywords + "%");
            if(userList.isEmpty())
            {
                List<UserType> userTypeList = userTypeRepository.findByUserTypeNameLikeOrUserTypeCodeLike("%" + keywords + "%", "%" + keywords + "%");
                userList = userRepository.findAllByUserTypeIn(userTypeList);
                if (userList.isEmpty()) {
                    List<Department> departmentList = departmentRepository.findByDepartmentNameLikeOrDepartmentCodeLike("%" + keywords + "%", "%" + keywords + "%");
                    userList = userRepository.findAllByDepartmentIn(departmentList);
                }
            }
        }

        if(!userList.isEmpty())
        {
            List<UserExcelUploadResponse> list = userList.stream().map(this::userToUserExcelUploadResponse).toList();
            ByteArrayInputStream byteArrayInputStream = ExcelHelper.convertListToExcel(UserExcelUploadResponse.class,list,headersMap,sheetName);
            String fileName = "users.xlsx";
            InputStreamResource inputStreamResource = new InputStreamResource(byteArrayInputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + fileName)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(inputStreamResource);

        }
        throw new ResourceNotFoundException("users","keyword: "+keywords+", size: ",0);
    }

    public ResponseEntity<Resource> getUserExcelSample()
    {

        Map<String,String> headersMap = ExcelHeadersInfo.getUserHeadersMap();

        Map<String,String>  headersTypeMap = ExcelHeadersInfo.getUserHeaderTypeMap();

        Map<String,String> examples = ExcelHeadersInfo.getUserDataExampleMap();

        String sheetName = "user_sample";
        Resource resource = ExcelHelper.getExcelSample(headersMap,headersTypeMap,examples,sheetName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+sheetName+".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }
    /* ------------------------------ User Helper Methods ------------------------------- */

    public User getUser(Long userId)
    {
        return userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
    }
    public User getUser(UserRequestDto userRequestDto, UserType userType, Department department, UserImage userImage)
    {
        User user = new User();
        BeanUtils.copyProperties(userRequestDto,user);
        user.setUserType(userType);
        user.setDepartment(department);
        user.setUserImage(userImage);
        return user;
    }
    public User getUser(long userMobile,String userEmail)
    {
        return userRepository.findByUserMobileAndUserEmail(userMobile,userEmail);
    }
    public User getUser(UserRequestDto userRequestDto)
    {
        long userMobile = userRequestDto.getUserMobile();
        String userEmail = userRequestDto.getUserEmail();
        return getUser(userMobile,userEmail);
    }
    public UserSingleDataResponseDto userToUserSingleDataResponseDto(User user)
    {
        try
        {
            UserSingleDataResponseDto userSingleDataResponseDto = new UserSingleDataResponseDto();
            BeanUtils.copyProperties(user, userSingleDataResponseDto);
            if(!Objects.isNull(user.getUserImage()))
                userSingleDataResponseDto.setImageURI(user.getUserImage().getDuplicateImageName());

            if(!Objects.isNull(user.getUserType()))
            {
                UserTypeSingleDataResponseDto userTypeSingleDataResponseDto = new UserTypeSingleDataResponseDto();
                BeanUtils.copyProperties(user.getUserType(), userTypeSingleDataResponseDto, "users");
                userSingleDataResponseDto.setUserType(userTypeSingleDataResponseDto);
            }

            if(!Objects.isNull(user.getDepartment()))
            {
                DepartmentSingleDataResponseDto departmentSingleDataResponseDto = new DepartmentSingleDataResponseDto();
                BeanUtils.copyProperties(user.getDepartment(), departmentSingleDataResponseDto, "users");
                userSingleDataResponseDto.setDepartment(departmentSingleDataResponseDto);
            }

            if(!user.getDocumentList().isEmpty())
            {
                List<DocumentSingleDataResponseDto> documentList = user.getDocumentList().stream().map(documentService::documentToDocumentSingleDataResponseDto).toList();
                userSingleDataResponseDto.setAttachments(documentList);
            }

            return userSingleDataResponseDto;
        }catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
   /* public User userRequestDtoToUser(UserRequestDto userRequestDto)
    {
        User user = new User();
        BeanUtils.copyProperties(userRequestDto,user);

        user.setUserType(userTypeService.getUserType(userRequestDto.getUserType()));

        user.setDepartment(departmentService.getDepartment(userRequestDto.getDepartment()));

        return user;
    }*/
    public User userExcelUploadResponseToUser(UserExcelUploadResponse userExcelUploadResponse)
    {
        User user = new User();
        BeanUtils.copyProperties(userExcelUploadResponse,user);

        UserType userType = userTypeService.getUserType(userExcelUploadResponse.getUserTypeName(),userExcelUploadResponse.getUserTypeCode());
        user.setUserType(userType);

        Department department = departmentService.getDepartment(userExcelUploadResponse.getDepartmentName(),userExcelUploadResponse.getDepartmentCode());
        user.setDepartment(department);

        user.setTimeStamp(LocalDateTime.now());

        return user;
    }
    public UserExcelUploadResponse userToUserExcelUploadResponse(User user)
    {
        UserExcelUploadResponse userExcelUploadResponse = new UserExcelUploadResponse();
        BeanUtils.copyProperties(user,userExcelUploadResponse);
        if(!Objects.isNull(user.getUserType())) {
            userExcelUploadResponse.setUserTypeName(user.getUserType().getUserTypeName());
            userExcelUploadResponse.setUserTypeCode(user.getUserType().getUserTypeCode());
        }
        else {
            userExcelUploadResponse.setUserTypeName("N/A");
            userExcelUploadResponse.setUserTypeCode("N/A");
        }
        if(!Objects.isNull(user.getDepartment())) {
            userExcelUploadResponse.setDepartmentName(user.getDepartment().getDepartmentName());
            userExcelUploadResponse.setDepartmentCode(user.getDepartment().getDepartmentCode());
        }
        else{
            userExcelUploadResponse.setDepartmentName("N/A");
            userExcelUploadResponse.setDepartmentCode("N/A");
        }
        return userExcelUploadResponse;
    }
}