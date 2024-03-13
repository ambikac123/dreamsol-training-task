package com.dreamsol.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.entities.User;
import com.dreamsol.repositories.UserRepository;
import com.dreamsol.services.ImageUploadService;

@Service
public class ImpageUploadServiceImpl implements ImageUploadService
{

	@Autowired
	private UserRepository userRepository; 
	
	@Override
	public String uploadImage(String destinationPath, MultipartFile sourceFile,User  user) 
	{
		// file name
		String sourceFileName = sourceFile.getOriginalFilename();
		String sourceFileExtension = sourceFileName.substring(sourceFileName.lastIndexOf('.'));
		
			// file path
			String randomID = UUID.randomUUID().toString();
			String newFileName = randomID+sourceFileExtension;
			String newFilePath = destinationPath +newFileName;
			
			// create folder not created
			File file = new File(destinationPath);
			if(!file.exists())
			{
				file.mkdir();
			}
			
			// file copy
			try 
			{
					Files.copy(sourceFile.getInputStream(), Paths.get(newFilePath));
					user.setImageURI(newFileName);
					userRepository.save(user);
			} 
			catch (IOException e) {
				
				e.printStackTrace();
			}
			return newFileName;
		
	}

	@Override
	public String getResource(String path, String fileName) throws FileNotFoundException,IOException{
		
		String fullPath = path+File.separator+fileName;
		
		InputStream resource =  new FileInputStream(fullPath);
		byte[] imageBytes = resource.readAllBytes();
		resource.close();
		String encoded = Base64.getEncoder().encodeToString(imageBytes);
		return encoded;
	}

	@Override
	public boolean deleteImage(String path, String fileName) {
		 Path imagePath = Paths.get(path, fileName);
		 try {
	            // Delete the file
	            Files.delete(imagePath);
	            return true;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return false;
	        }
	}
	
}
