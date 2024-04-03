package com.dreamsol.helpers;

import com.dreamsol.entities.UserImage;
import com.dreamsol.exceptions.ImageNotUploadedException;
import com.dreamsol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@AllArgsConstructor(onConstructor_ = {@Autowired} )
public class ImageHelper
{
    public UserImage getImage(MultipartFile file,String imagePath)
    {
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName != null ? fileName.substring(fileName.lastIndexOf('.')) : "";
        if(fileExtension.equals(".jpg")||fileExtension.equals(".jpeg")||fileExtension.equals(".png"))
        {
            return saveImage(file,fileName,fileExtension,imagePath);
        }
        else{
            throw new InvalidFileNameException("File Extension: ","Image extension not supported!");
        }
    }
    public UserImage saveImage(MultipartFile imageFile, String fileName, String fileExtension,String destinationPath)
    {
        try {

            // file path
            String randomID = UUID.randomUUID().toString();
            String newFileName = randomID + fileExtension;
            String newFilePath = destinationPath + newFileName;

            Files.copy(imageFile.getInputStream(), Paths.get(newFilePath));

            UserImage userImage = new UserImage();
            userImage.setOriginalImageName(fileName);
            userImage.setDuplicateImageName(newFileName);
            userImage.setStatus(true);
            return userImage;
        }catch(IOException e){
            throw new ImageNotUploadedException(e.getMessage());
        }
    }
    public boolean deleteImage(String fileName,String destinationPath)
    {
        try {
            Path imagePath = Paths.get(destinationPath, fileName);
            Files.delete(imagePath);
            return true;
        }catch(IOException e)
        {
            throw new ResourceNotFoundException("Image",fileName,0);
        }
    }

}
