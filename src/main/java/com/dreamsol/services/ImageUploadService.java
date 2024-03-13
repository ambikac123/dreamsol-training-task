package com.dreamsol.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.entities.User;

public interface ImageUploadService 
{
	String uploadImage(String path, MultipartFile file,User user);

	String getResource(String path, String fileName) throws FileNotFoundException,IOException;
	
	boolean deleteImage(String path,String fileName);
}
