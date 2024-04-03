package com.dreamsol.helpers;

import com.dreamsol.entities.Department;
import com.dreamsol.entities.User;
import com.dreamsol.entities.UserType;
import com.dreamsol.exceptions.ResourceNotFoundException;
import com.dreamsol.repositories.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dreamsol.response.ExcelUploadResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class GlobalHelper
{
    private UserRepository userRepository;
    public static final String REGEX_NAME = "[A-Za-z]{3,}(\\s[A-Za-z]{3,})*$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String REGEX_MOBILE = "^[6-9]\\d{9}$";
    public static final String REGEX_CODE = "^[a-zA-Z]{2,7}\\d{0,3}$";
    public static boolean isValidName(String name)
    {
        Pattern pattern = Pattern.compile(REGEX_NAME);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public static <T> boolean isValidName(Field f,T t)
    {
        try
        {
            return isValidName((String) f.get(t));
        }catch(Exception e)
        {
            throw new InputMismatchException(e.getMessage());
        }
    }
    public static boolean isValidCode(String code)
    {
        Pattern pattern = Pattern.compile(REGEX_CODE);
        Matcher matcher = pattern.matcher(code);
        return matcher.matches();
    }
    public static <T> boolean isValidCode(Field f,T t)
    {
        try
        {
            return isValidCode((String) f.get(t));
        }catch(Exception e)
        {
            return false;
        }
    }
    public static boolean isValidMobile(Long mobile)
    {
        String mobileNo = Long.toString(mobile);
        Pattern pattern = Pattern.compile(REGEX_MOBILE);
        Matcher matcher = pattern.matcher(mobileNo);
        return matcher.matches();
    }
    public static <T> boolean isValidMobileNo(Field f,T t)
    {
        try
        {
            return isValidMobile((long) f.get(t));
        }catch(Exception e)
        {
            return false;
        }
    }
    public static boolean isValidEmail(String email)
    {
        Pattern pattern = Pattern.compile(REGEX_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static <T> boolean isValidEmail(Field f,T t)
    {
        try
        {
            return isValidEmail((String) f.get(t));
        }catch(Exception e)
        {
            return false;
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
    public static <T> ExcelUploadResponse getCorrectAndIncorrectList(List<T> list, Class<T> entityName)
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
                        if (GlobalHelper.isValidName(field, t))
                            field.set(entity, field.get(t));
                        else {
                            isValidEntity = false;
                            field.set(entity, field.get(t));
                            errorMessage.append("invalid name format, ");
                        }
                    }
                    else if (field.getName().toLowerCase().contains("code"))
                    {
                        if (GlobalHelper.isValidCode(field, t))
                            field.set(entity, field.get(t));
                        else {
                            isValidEntity = false;
                            field.set(entity, field.get(t));
                            errorMessage.append("invalid code format, ");
                        }
                    }
                    else if (field.getName().toLowerCase().contains("status"))
                    {
                        field.set(entity, field.get(t));
                    }
                    else if (field.getName().toLowerCase().contains("mobile"))
                    {
                        if (GlobalHelper.isValidMobileNo(field, t))
                            field.set(entity, field.get(t));
                        else {
                            isValidEntity = false;
                            field.set(entity, field.get(t));
                            errorMessage.append("invalid mobile number, ");
                        }
                    }
                    else if (field.getName().toLowerCase().contains("email"))
                    {
                        if (GlobalHelper.isValidEmail(field, t))
                            field.set(entity, field.get(t));
                        else {
                            isValidEntity = false;
                            field.set(entity, field.get(t));
                            errorMessage.append("invalid email id, ");
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
            return excelUploadResponse;
        } catch (Exception e)
        {
            return null;
        }
    }
}
