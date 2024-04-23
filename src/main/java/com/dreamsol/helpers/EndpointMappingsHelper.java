package com.dreamsol.helpers;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EndpointMappingsHelper
{
    private static final Map<String,String> endpointMappings = new HashMap<>();
    static
    {
        // Mixed API
        endpointMappings.put("ACCESS_ALL","/api/**");
        endpointMappings.put("ACCESS_LOGIN","/api/login");
        endpointMappings.put("ACCESS_LOGOUT","/api/logout");
        endpointMappings.put("ACCESS_REGENERATE_TOKEN","/re-generate-token");
        endpointMappings.put("ACCESS_GET_ENDPOINTS","/api/get-endpoints");
        endpointMappings.put("ACCESS_UPDATE_ENDPOINTS","/api/update-endpoints");

        // Role API
        endpointMappings.put("ACCESS_ROLE_ALL", "/api/roles/**");
        endpointMappings.put("ACCESS_ROLE_CREATE", "/api/roles/create");
        endpointMappings.put("ACCESS_ROLE_UPDATE", "/api/roles/update/");
        endpointMappings.put("ACCESS_ROLE_DELETE", "/api/roles/delete/");
        endpointMappings.put("ACCESS_ROLE_GET", "/api/roles/get/");
        endpointMappings.put("ACCESS_ROLE_GET_ALL", "/api/roles/get-all");

        // Permission API
        endpointMappings.put("ACCESS_PERMISSION_ALL", "/api/permissions/**");
        endpointMappings.put("ACCESS_PERMISSION_CREATE", "/api/permissions/create");
        endpointMappings.put("ACCESS_PERMISSION_UPDATE", "/api/permissions/update/");
        endpointMappings.put("ACCESS_PERMISSION_DELETE", "/api/permissions/delete/");
        endpointMappings.put("ACCESS_PERMISSION_GET", "/api/permissions/get/");
        endpointMappings.put("ACCESS_PERMISSION_GET_ALL", "/api/permissions/get-all");

        // Department API
        endpointMappings.put("ACCESS_DEPARTMENT_ALL","/api/departments/**");
        endpointMappings.put("ACCESS_DEPARTMENT_CREATE","/api/departments/create");
        endpointMappings.put("ACCESS_DEPARTMENT_UPDATE","/api/departments/update/");
        endpointMappings.put("ACCESS_DEPARTMENT_DELETE","/api/departments/delete/");
        endpointMappings.put("ACCESS_DEPARTMENT_GET","/api/departments/get/");
        endpointMappings.put("ACCESS_DEPARTMENT_GET_ALL","/api/departments/get-all");
        endpointMappings.put("ACCESS_DEPARTMENT_VALIDATE_EXCEL_DATA","/api/departments/validate-excel-data");
        endpointMappings.put("ACCESS_DEPARTMENT_SAVE_CORRECT_LIST","/api/departments/save-correct-list");
        endpointMappings.put("ACCESS_DEPARTMENT_DOWNLOAD_EXCEL","/api/departments/download-excel");
        endpointMappings.put("ACCESS_DEPARTMENT_DOWNLOAD_EXCEL_SAMPLE","/api/departments/download-excel-sample");
        endpointMappings.put("ACCESS_DEPARTMENT_DOWNLOAD_EXCEL_DUMMY","/api/departments/download-excel-dummy");

        // UserType API
        endpointMappings.put("ACCESS_USERTYPE_ALL","/api/usertypes/**");
        endpointMappings.put("ACCESS_USERTYPE_CREATE","/api/usertypes/create");
        endpointMappings.put("ACCESS_USERTYPE_UPDATE","/api/usertypes/update/");
        endpointMappings.put("ACCESS_USERTYPE_DELETE","/api/usertypes/delete/");
        endpointMappings.put("ACCESS_USERTYPE_GET","/api/usertypes/get/");
        endpointMappings.put("ACCESS_USERTYPE_GET_ALL","/api/usertypes/get-all");
        endpointMappings.put("ACCESS_USERTYPE_VALIDATE_EXCEL_DATA","/api/usertypes/validate-excel-data");
        endpointMappings.put("ACCESS_USERTYPE_SAVE_CORRECT_LIST","/api/usertypes/save-correct-list");
        endpointMappings.put("ACCESS_USERTYPE_DOWNLOAD_EXCEL","/api/usertypes/download-excel");
        endpointMappings.put("ACCESS_USERTYPE_DOWNLOAD_EXCEL_SAMPLE","/api/usertypes/download-excel-sample");
        endpointMappings.put("ACCESS_USERTYPE_DOWNLOAD_EXCEL_DUMMY","/api/usertypes/download-excel-dummy");

        // User API
        endpointMappings.put("ACCESS_USER_ALL","/api/users/**");
        endpointMappings.put("ACCESS_USER_CREATE","/api/users/create");
        endpointMappings.put("ACCESS_USER_UPDATE","/api/users/update/");
        endpointMappings.put("ACCESS_USER_DELETE","/api/users/delete");
        endpointMappings.put("ACCESS_USER_GET","/api/users/get/");
        endpointMappings.put("ACCESS_USER_GET_ALL","/api/users/get-all");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_IMAGE_FILE","/api/users/download-image-file");
        endpointMappings.put("ACCESS_USER_UPLOAD_ATTACHMENT","/api/users/upload-attachment");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_ATTACHMENT","/api/users/download-attachment");
        endpointMappings.put("ACCESS_USER_VALIDATE_EXCEL_DATA","/api/users/validate-excel-data");
        endpointMappings.put("ACCESS_USER_SAVE_CORRECT_LIST","/api/users/save-correct-list");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_EXCEL","/api/users/download-excel");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_EXCEL_SAMPLE","/api/users/download-excel-sample");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_EXCEL_DUMMY","/api/users/download-excel-dummy");
    }
    public Map<String,String> getEndpointMap()
    {
        return endpointMappings;
    }
}
