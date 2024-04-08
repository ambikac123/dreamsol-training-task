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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor(onConstructor_ = {@Autowired} )
public class ImageHelper
{
    public UserImage getImage(MultipartFile file,String imagePath)
    {
        List<String> imageExtensionList = Arrays.asList(".jpg",".jpeg",".png",".gif",".bmp",".tiff",".svg");
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName != null ? fileName.substring(fileName.lastIndexOf('.')) : "";

        if(imageExtensionList.contains(fileExtension))
        {
            return saveImage(file,fileName,fileExtension,imagePath);
        }
        else{
            throw new InvalidFileNameException("File Extension: ","Uploaded file format not supported! allowed formats are: [jpg, jpeg, png, gif, bmp, tiff and svg]");
        }
    }
    public UserImage saveImage(MultipartFile imageFile, String fileName, String fileExtension,String destinationPath)
    {
        try
        {
            String randomID = UUID.randomUUID().toString();
            String newFileName = randomID + fileExtension;
            String newFilePath = destinationPath + newFileName;

            Files.copy(imageFile.getInputStream(), Paths.get(newFilePath));

            UserImage userImage = new UserImage();
            userImage.setOriginalImageName(fileName);
            userImage.setDuplicateImageName(newFileName);
            userImage.setStatus(true);
            return userImage;
        }
        catch(IOException e){
            throw new ImageNotUploadedException("Image not saved into the specified directory["+destinationPath+"], Error Reason: "+e.getMessage());
        }
    }
    public void deleteImage(String fileName,String destinationPath)
    {
        try {
            Path imagePath = Paths.get(destinationPath, fileName);
            Files.delete(imagePath);
        }catch(IOException e)
        {
            throw new ResourceNotFoundException("Image",fileName,0);
        }
    }
}
