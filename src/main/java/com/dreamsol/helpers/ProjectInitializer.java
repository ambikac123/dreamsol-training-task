package com.dreamsol.helpers;

import com.dreamsol.entities.Endpoint;
import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.entities.User;
import com.dreamsol.repositories.EndpointRepository;
import com.dreamsol.repositories.PermissionRepository;
import com.dreamsol.repositories.RoleRepository;
import com.dreamsol.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProjectInitializer
{
    @Value("${project.image}")
    public String imagePath;

    @Value("${project.file}")
    public String filePath;

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EndpointRepository endpointRepository;
    @Autowired private PermissionRepository permissionRepository;
    @Autowired private RoleRepository roleRepository;
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
    @PostConstruct
    public void createDemoUser()
    {
        try
        {
            User user = userRepository.findByUserEmail("demo");
            if(user!=null)
                return;
            Endpoint endpoint1 = endpointRepository.findById("ACCESS_ALL").orElse(new Endpoint("ACCESS_ALL", "/api/**"));
            Endpoint endpoint2 = endpointRepository.findById("ACCESS API").orElse(new Endpoint("ACCESS API","ACCESS"));
            Endpoint endpoint3 = endpointRepository.findById("ALL").orElse(new Endpoint("ALL","_ALL"));
            endpointRepository.save(endpoint1);
            endpointRepository.save(endpoint2);
            endpointRepository.save(endpoint3);
            Role role = roleRepository.findByRoleType("DEMO");
            if(role==null) {
                role = new Role();
                role.setRoleType("DEMO");
                role.setEndpoints(List.of(endpoint2));
                role.setTimeStamp(LocalDateTime.now());
                role.setStatus(true);
            }
            roleRepository.save(role);
            Permission permission = permissionRepository.findByPermissionType("ALL");
            if(permission==null){
                permission = new Permission();
                permission.setPermissionType("ALL");
                permission.setEndPoints(List.of(endpoint3));
                permission.setTimeStamp(LocalDateTime.now());
                permission.setStatus(true);
            }
            permissionRepository.save(permission);
                user = new User();
                user.setUserName("demo");
                user.setUserMobile(9999999999L);
                user.setUserEmail("demo");
                user.setUserPassword(passwordEncoder.encode("demo"));
                user.setStatus(true);
                user.setRoles(List.of(role));
                user.setPermissions(List.of(permission));
                user.setTimeStamp(LocalDateTime.now());
                userRepository.save(user);
        }catch(Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
}
