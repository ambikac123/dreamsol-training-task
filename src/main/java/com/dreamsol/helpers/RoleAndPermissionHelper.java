package com.dreamsol.helpers;

import com.dreamsol.entities.KeyEndpointMappings;
import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.repositories.KeyEndpointMappingsRepository;
import com.dreamsol.repositories.PermissionRepository;
import com.dreamsol.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoleAndPermissionHelper
{
    @Autowired private PermissionRepository permissionRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private KeyEndpointMappingsRepository keyEndpointMappingsRepository;
    private final Map<String,String[]> authorityPatterns;
    public RoleAndPermissionHelper(Map<String,String[]> authorityPatterns)
    {
        this.authorityPatterns = authorityPatterns;
    }
   public Map<String,String[]> getAuthorityPatterns()
    {
        authorityPatterns.put("ROLE_ALL",new String[]{"/api/**"});
        authorityPatterns.put("ROLE_DEMO",new String[]{"/api/**"});
        List<Role> roles = roleRepository.findAllByStatusTrue();
        List<Permission> permissions = permissionRepository.findAllByStatusTrue();
        for(Role role : roles)
        {
            authorityPatterns.put("ROLE_"+role.getRoleType(),role.getEndPoints().toArray(new String[0]));
        }
        for (Permission permission : permissions)
        {
            authorityPatterns.put("ROLE_"+permission.getPermissionType(),permission.getEndPoints().toArray(new String[0]));
        }
        return authorityPatterns;
    }
    public Map<String,String> getAllEndPoints()
    {
        // Keys
        String[] endPointsKeys = {
                // Security Controller
                "ACCESS_ALL",
                "ACCESS_LOGIN","ACCESS_LOGOUT","ACCESS_RE_GENERATE_TOKEN",
                "ACCESS_ROLE_ALL","ACCESS_ROLE_CREATE","ACCESS_ROLE_UPDATE","ACCESS_ROLE_DELETE","ACCESS_ROLE_GET","ACCESS_ROLE_GET_ALL",
                "ACCESS_PERMISSION_ALL","ACCESS_PERMISSION_CREATE","ACCESS_PERMISSION_UPDATE","ACCESS_PERMISSION_DELETE","ACCESS_PERMISSION_GET","ACCESS_PERMISSION_GET_ALL",

                //Department Controller
                "ACCESS_DEPARTMENT_ALL","ACCESS_DEPARTMENT_CREATE","ACCESS_DEPARTMENT_UPDATE","ACCESS_DEPARTMENT_DELETE","ACCESS_DEPARTMENT_GET", "ACCESS_DEPARTMENT_GET_ALL",
                "ACCESS_DEPARTMENT_VALIDATE_EXCEL_DATA","ACCESS_DEPARTMENT_SAVE_CORRECT_LIST","ACCESS_DEPARTMENT_DOWNLOAD_EXCEL","ACCESS_DEPARTMENT_DOWNLOAD_EXCEL_SAMPLE","ACCESS_DEPARTMENT_DOWNLOAD_EXCEL_DUMMY",

                // UserType Controller
                "ACCESS_USERTYPE_ALL","ACCESS_USERTYPE_CREATE","ACCESS_USERTYPE_UPDATE","ACCESS_USERTYPE_DELETE","ACCESS_USERTYPE_GET","ACCESS_USERTYPE_GET_ALL",
                "ACCESS_USERTYPE_VALIDATE_EXCEL_DATA","ACCESS_USERTYPE_SAVE_CORRECT_LIST","ACCESS_USERTYPE_DOWNLOAD_EXCEL","ACCESS_USERTYPE_DOWNLOAD_EXCEL_SAMPLE","ACCESS_USERTYPE_DOWNLOAD_EXCEL_DUMMY",

                //User Controller
                "ACCESS_USER_ALL","ACCESS_USER_CREATE","ACCESS_USER_UPDATE","ACCESS_USER_DELETE","ACCESS_USER_GET","ACCESS_USER_GET_ALL",
                "ACCESS_USER_DOWNLOAD_IMAGE_FILE","ACCESS_USER_UPLOAD_ATTACHMENT","ACCESS_USER_DOWNLOAD_ATTACHMENT",
                "ACCESS_USER_VALIDATE_EXCEL_DATA","ACCESS_USER_SAVE_CORRECT_LIST","ACCESS_USER_DOWNLOAD_EXCEL","ACCESS_USER_DOWNLOAD_EXCEL_SAMPLE","ACCESS_USER_","ACCESS_USER_DOWNLOAD_EXCEL_DUMMY",
        };

        // EndPoints
        String[] endPointsLinks = {
                // Security Controller
                "/api/**",
                "/api/login","/api/logout","/re-generate-token",
                "/api/roles/**","/api/roles/create","/api/roles/update/","/api/roles/delete/","/api/roles/get/","/api/roles/get-all",
                "/api/permissions/**","/api/permissions/create","/api/permissions/update/","/api/permissions/delete/","/api/permissions/get/","/api/permissions/get-all",

                // Department Controller
                "/api/departments/**","/api/departments/create","/api/departments/update/","/api/departments/delete/","/api/departments/get/","/api/departments/get-all",
                "/api/departments/validate-excel-data","/api/departments/save-correct-list","/api/departments/download-excel","/api/departments/download-excel-sample","/api/departments/download-excel-dummy",

                // UserType Controller
                "/api/usertypes/**","/api/usertypes/create","/api/usertypes/update/","/api/usertypes/delete/","/api/usertypes/get/","/api/usertypes/get-all",
                "/api/usertypes/","/api/usertypes/validate-excel-data","/api/usertypes/save-correct-list","/api/usertypes/download-excel","/api/usertypes/download-excel-sample","/api/usertypes/download-excel-dummy",

                // User Controller
                "/api/users/**","/api/users/create","/api/users/update/","/api/users/delete/","/api/users/get/","/api/users/get-all",
                "/api/users/download-image-file/","/api/users/upload-attachment/","/api/users/download-attachment/",
                "/api/users/validate-excel-data","/api/users/save-correct-list","/api/users/download-excel","/api/users/download-excel-sample","/api/users/download-excel-dummy",
        };
        Map<String,String> endPointsMappings = new HashMap<>();
        for(int i=0; i<endPointsKeys.length; i++)
        {
            endPointsMappings.put(endPointsKeys[i],endPointsLinks[i]);
            //keyEndpointMappingsRepository.save(new KeyEndpointMappings(endPointsKeys[i],endPointsLinks[i] ));
        }
        return endPointsMappings;
    }
}
