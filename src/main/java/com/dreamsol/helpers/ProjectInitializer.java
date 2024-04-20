package com.dreamsol.helpers;

import com.dreamsol.repositories.EndpointMappingsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ProjectInitializer
{
    @Value("${project.image}")
    public String imagePath;

    @Value("${project.file}")
    public String filePath;
    @Autowired
    EndpointMappingsRepository keyEndpointMappingsRepository;

    @PostConstruct
    public void init()
    {
        String[] paths = {imagePath,filePath};

        for(String path : paths)
        {
            File directory = new File(path);
            if (!directory.exists())
            {
                if (!directory.mkdirs()) {
                    throw new RuntimeException("Unable to create directory: "+path);
                }
            }
        }
    }
}
