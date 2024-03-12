package com.dreamsol.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.entities.User;
import com.dreamsol.repositories.ImageUploadRepository;
import com.dreamsol.services.ImageUploadService;

@Service
public class ImpageUploadServiceImpl implements ImageUploadService
{

	@Autowired
	private ImageUploadRepository imageUploadRepository; 
	
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
					imageUploadRepository.save(user);
			} 
			catch (IOException e) {
				
				e.printStackTrace();
			}
			return newFileName;
		
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String fullPath = path+File.separator+fileName;
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
	}

}
