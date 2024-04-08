package com.dreamsol.helpers;

import com.dreamsol.dto.UserResponseDto;
import com.dreamsol.entities.Department;
import com.dreamsol.entities.User;
import com.dreamsol.entities.UserType;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.DepartmentRepository;
import com.dreamsol.repositories.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dreamsol.repositories.UserTypeRepository;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.DepartmentExcelUploadResponse;
import com.dreamsol.response.ExcelUploadResponse;
import com.dreamsol.response.UserExcelUploadResponse;
import com.dreamsol.response.UserTypeExcelUploadResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@SuppressWarnings("ALL")
@Component
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class GlobalHelper
{
    private DepartmentRepository departmentRepository;
    private UserTypeRepository userTypeRepository;
    private UserRepository userRepository;
    public static final String REGEX_NAME = "^[A-Za-z]{3,}(\\s[A-Za-z]{3,})*$";
    public static final String NAME_ERROR_MESSAGE = "name must contain [a-z, A-Z or space(' ')] and must start with at least 3 letters";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    public static final String EMAIL_ERROR_MESSAGE = "email should be in given format [user.name@example-domain.com] allowed characters are : [a-z,A-Z,0-9,_,+,&,*,-]";
    public static final String REGEX_MOBILE = "^[6-9]\\d{9}$";
    public static final String MOBILE_ERROR_MESSAGE = "mobile no. invalid, must start with digits [6-9] and must be 10 digits long";
    public static final String REGEX_CODE = "^[a-zA-Z]{2,7}\\d{0,3}$";
    public static final String CODE_ERROR_MESSAGE = "code can contain [a-z, A-Z, 0-9] and must start with at least 2 letters and followed by maximum 3 digits";
    private ApiResponse apiResponse;
    public boolean isValidName(String name)
    {
        Pattern pattern = Pattern.compile(REGEX_NAME);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public <T> ApiResponse isValidName(Field f,T t)
    {
        try
        {
            if(t instanceof DepartmentExcelUploadResponse) {
                Department d = departmentRepository.findByDepartmentName((String) f.get(t));
                if(!isValidName((String) f.get(t))) {
                    apiResponse.setMessage("invalid department name, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                }
                else if(!Objects.isNull(d)) {
                    apiResponse.setMessage("department name already exist, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                }
                apiResponse.setSuccess(true);
                apiResponse.setMessage("correct");
                return apiResponse;
            }
            else if(t instanceof UserTypeExcelUploadResponse) {
                UserType u = userTypeRepository.findByUserTypeName((String) f.get(t));
                if(!isValidName((String) f.get(t)))
                {
                    apiResponse.setMessage("invalid usertype name, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                } else if (!Objects.isNull(u)) {
                    apiResponse.setMessage("usertype name already exist, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                }
                apiResponse.setSuccess(true);
                apiResponse.setMessage("correct");
                return apiResponse;
            }
            else if(t instanceof UserExcelUploadResponse)
            {
                if(f.getName().toLowerCase().contains("department"))
                {
                    Department d = departmentRepository.findByDepartmentName((String) f.get(t));
                    if(Objects.isNull(d) || !d.isStatus())
                    {
                        apiResponse.setMessage("department name doesn't exist, ");
                        apiResponse.setSuccess(false);
                        return apiResponse;
                    }
                    apiResponse.setSuccess(true);
                    apiResponse.setMessage("correct");
                    return apiResponse;
                }
                else if(f.getName().toLowerCase().contains("usertype"))
                {
                    UserType ut = userTypeRepository.findByUserTypeName((String) f.get(t));
                    if(Objects.isNull(ut) || !ut.isStatus())
                    {
                        apiResponse.setMessage("usertype name doesn't exist, ");
                        apiResponse.setSuccess(false);
                        return apiResponse;
                    }
                    apiResponse.setSuccess(true);
                    apiResponse.setMessage("correct");
                    return apiResponse;
                }
                else if(f.getName().toLowerCase().contains("user")) {
                    if(!isValidName((String) f.get(t)))
                    {
                        apiResponse.setMessage("invalid user name, ");
                        apiResponse.setSuccess(false);
                        return apiResponse;
                    }
                    apiResponse.setSuccess(true);
                    apiResponse.setMessage("correct");
                    return apiResponse;
                }
            }
            apiResponse.setSuccess(false);
            return apiResponse;
        }catch(Exception e)
        {
            throw new InputMismatchException(e.getMessage());
        }
    }
    public boolean isValidCode(String code)
    {
        Pattern pattern = Pattern.compile(REGEX_CODE);
        Matcher matcher = pattern.matcher(code);
        return matcher.matches();
    }
    public <T> ApiResponse isValidCode(Field f,T t)
    {
        try
        {
            if(t instanceof DepartmentExcelUploadResponse) {
                Department d = departmentRepository.findByDepartmentCode((String) f.get(t));
                if(!isValidCode((String) f.get(t))) {
                    apiResponse.setMessage("invalid department code, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                }
                else if(!Objects.isNull(d)) {
                    apiResponse.setMessage("department code already exist, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                }
                apiResponse.setSuccess(true);
                apiResponse.setMessage("correct");
                return apiResponse;
            }
            else if(t instanceof UserTypeExcelUploadResponse) {
                UserType u = userTypeRepository.findByUserTypeCode((String) f.get(t));
                if(!isValidCode((String) f.get(t)))
                {
                    apiResponse.setMessage("invalid usertype code, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                } else if (!Objects.isNull(u)) {
                    apiResponse.setMessage("usertype code already exist, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                }
                apiResponse.setSuccess(true);
                apiResponse.setMessage("correct");
                return apiResponse;
            }
            else if(t instanceof UserExcelUploadResponse)
            {
                if(f.getName().toLowerCase().contains("department"))
                {
                    Department d = departmentRepository.findByDepartmentCode((String) f.get(t));
                    if(Objects.isNull(d) || !d.isStatus())
                    {
                        apiResponse.setMessage("department code doesn't exist, ");
                        apiResponse.setSuccess(false);
                        return apiResponse;
                    }
                    apiResponse.setSuccess(true);
                    apiResponse.setMessage("correct");
                    return apiResponse;
                }
                else if(f.getName().toLowerCase().contains("usertype"))
                {
                    UserType ut = userTypeRepository.findByUserTypeCode((String) f.get(t));
                    if(Objects.isNull(ut) || !ut.isStatus())
                    {
                        apiResponse.setMessage("usertype code doesn't exist, ");
                        apiResponse.setSuccess(false);
                        return apiResponse;
                    }
                    apiResponse.setSuccess(true);
                    apiResponse.setMessage("correct");
                    return apiResponse;
                }
            }
            apiResponse.setSuccess(false);
            return apiResponse;
        }catch(Exception e)
        {
            throw new InputMismatchException(e.getMessage());
        }
    }
    public boolean isValidMobile(Long mobile)
    {
        String mobileNo = Long.toString(mobile);
        Pattern pattern = Pattern.compile(REGEX_MOBILE);
        Matcher matcher = pattern.matcher(mobileNo);
        return matcher.matches();
    }
    public <T> ApiResponse isValidMobileNo(Field f,T t)
    {
        try
        {
            if(t instanceof UserExcelUploadResponse) {
                if (!isValidMobile((Long) f.get(t))) {
                    apiResponse.setMessage("invalid mobile no., ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                } else if (!Objects.isNull(userRepository.findByUserMobile((Long) f.get(t)))) {
                    apiResponse.setMessage("mobile no. already exist, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                }
                apiResponse.setSuccess(true);
                apiResponse.setMessage("correct");
                return apiResponse;
            }
            apiResponse.setSuccess(false);
            return apiResponse;
        }catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public boolean isValidEmail(String email)
    {
        Pattern pattern = Pattern.compile(REGEX_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public <T> ApiResponse isValidEmail(Field f, T t)
    {
        try
        {
            if(t instanceof UserExcelUploadResponse) {
                if(!isValidEmail((String) f.get(t)))
                {
                    apiResponse.setMessage("invalid email, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                } else if (!Objects.isNull(userRepository.findByUserEmail((String) f.get(t)))) {
                    apiResponse.setMessage("email already exist, ");
                    apiResponse.setSuccess(false);
                    return apiResponse;
                }
                apiResponse.setSuccess(true);
                apiResponse.setMessage("correct");
                return apiResponse;
            }
            apiResponse.setSuccess(false);
            return apiResponse;
        }catch(Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static byte[] getResource(String imageName,String imagePath){
        try {
            String fullPath = imagePath + File.separator + imageName;

            InputStream resource = new FileInputStream(fullPath);
            byte[] fileBytes = resource.readAllBytes();
            resource.close();
            return fileBytes;
        }catch(IOException e)
        {
            throw new ResourceNotFoundException("File ",imageName,0);
        }
    }
    public List<User> getUsers(UserType userType)
    {
        return userRepository.findAllByUserType(userType);
    }
    public List<User> getUsers(Department department)
    {
        return userRepository.findAllByDepartment(department);
    }
    public UserResponseDto userToUserResponseDto(User user)
    {
        UserResponseDto userResponseDto = new UserResponseDto();
        BeanUtils.copyProperties(user,userResponseDto);
        if(!Objects.isNull(user.getUserImage()))
        {
            String downloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/users/download-image-file/")
                    .path(user.getUserImage().getDuplicateImageName())
                    .toUriString();
            userResponseDto.setImageName(downloadLink);
        }
        return userResponseDto;
    }
    public <T> ExcelUploadResponse getCorrectAndIncorrectList(List<T> list, Class<T> entityName)
    {
        try
        {
            List<T> correctList = new ArrayList<>();
            List<T> incorrectList = new ArrayList<>();

            for (T t : list)
            {
                T entity = entityName.getDeclaredConstructor().newInstance();
                Field[] fields = entity.getClass().getDeclaredFields();
                boolean isValidEntity = true;
                StringBuilder errorMessage = new StringBuilder();
                for (Field field : fields)
                {
                    field.setAccessible(true);
                    if (field.getName().toLowerCase().contains("name"))
                    {
                        ApiResponse apiResponse1 = isValidName(field,t);
                        if (apiResponse1.isSuccess())
                        {
                            field.set(entity, field.get(t));
                        }
                        else {
                            isValidEntity = false;
                            field.set(entity, field.get(t));
                            errorMessage.append(apiResponse1.getMessage());
                        }
                    }
                    else if (field.getName().toLowerCase().contains("code"))
                    {
                        ApiResponse apiResponse1 = isValidCode(field, t);
                        if (apiResponse1.isSuccess())
                            field.set(entity, field.get(t));
                        else {
                            isValidEntity = false;
                            field.set(entity, field.get(t));
                            errorMessage.append(apiResponse1.getMessage());
                        }
                    }
                    else if (field.getName().toLowerCase().contains("status"))
                    {
                        field.set(entity, field.get(t));
                    }
                    else if (field.getName().toLowerCase().contains("mobile"))
                    {
                        ApiResponse apiResponse1 = isValidMobileNo(field, t);
                        if (apiResponse1.isSuccess())
                            field.set(entity, field.get(t));
                        else {
                            isValidEntity = false;
                            field.set(entity, field.get(t));
                            errorMessage.append(apiResponse1.getMessage());
                        }
                    }
                    else if (field.getName().toLowerCase().contains("email"))
                    {
                        ApiResponse apiResponse1 = isValidEmail(field, t);
                        if (apiResponse1.isSuccess())
                            field.set(entity, field.get(t));
                        else {
                            isValidEntity = false;
                            field.set(entity, field.get(t));
                            errorMessage.append(apiResponse1.getMessage());
                        }
                    }
                    else if (field.getName().toLowerCase().contains("message"))
                    {
                        if (isValidEntity) {
                            field.set(entity, "correct");
                        } else {
                            field.set(entity, errorMessage.toString());
                        }
                    }
                }
                if (isValidEntity)
                    correctList.add(entity);
                else
                    incorrectList.add(entity);
            }

            ExcelUploadResponse excelUploadResponse = new ExcelUploadResponse();
            excelUploadResponse.setCorrectList(correctList);
            excelUploadResponse.setIncorrectList(incorrectList);
            excelUploadResponse.setMessage("Process completed successfully, No. of correct items: "+correctList.size()+", No. of incorrect items: "+incorrectList.size());
            return excelUploadResponse;
        } catch (Exception e)
        {
            ExcelUploadResponse excelUploadResponse = new ExcelUploadResponse();
            excelUploadResponse.setMessage("Process failed!, Reason: "+e.getMessage());
            return excelUploadResponse;
        }
    }

}
