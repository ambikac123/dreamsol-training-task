package com.dreamsol.helpers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DirectoryInitializer
{
    @Value("${project.image}")
    public String imagePath;

    @Value("${project.file}")
    public String filePath;

    @PostConstruct
    public void init() {
        File imageDirectory = new File(imagePath);
        if (!imageDirectory.exists()) {
            if (imageDirectory.mkdirs()) {
                System.out.println("Directory created: " + imagePath);
            } else {
                System.err.println("Failed to create directory: " + imagePath);
            }
        }
        File fileDirectory = new File(filePath);
        if (!fileDirectory.exists()) {
            if (fileDirectory.mkdirs()) {
                System.out.println("Directory created: " + filePath);
            } else {
                System.err.println("Failed to create directory: " + filePath);
            }
        }
    }
}
