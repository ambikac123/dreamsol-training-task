package com.dreamsol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dreamsol.response.ApiResponse;
import com.dreamsol.services.ImageUploadService;

@RestController
@RequestMapping("/file")
public class ImageUploadController 
{
	@Autowired
	private ImageUploadService service;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> uploadImage(
			@RequestParam("image") MultipartFile image
	)
	{
		//String newFileName = service.uploadImage(path, image);
		//return ResponseEntity.ok(new ApiResponse("Image uploaded!!"+newFileName,true));
		return null;
	}
	
	@GetMapping("/get")
	public ResponseEntity<ApiResponse> getImage(
			@RequestParam("imagePath") String imagePath
	)
	{
		String imageFile = service.getImage(imagePath);
		return ResponseEntity.ok(new ApiResponse(imageFile,true));
	}
}
