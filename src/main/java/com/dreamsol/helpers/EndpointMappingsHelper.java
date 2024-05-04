package com.dreamsol.helpers;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EndpointMappingsHelper
{
    private static final Map<String,String> endpointMappings = new HashMap<>();
    private static final Map<String,String> roleEndpointMap = new HashMap<>();
    private static final Map<String,String> permissionEndpointMap = new HashMap<>();
    static
    {
        // Public API
        endpointMappings.put("ACCESS_ALL","/api/**");
        endpointMappings.put("ACCESS_LOGIN","/api/login");
        endpointMappings.put("ACCESS_REGENERATE_TOKEN","/re-generate-token");
        endpointMappings.put("ACCESS_GET_ENDPOINTS","/api/get-endpoints");
        endpointMappings.put("ACCESS_UPDATE_ENDPOINTS","/api/update-endpoints");
        endpointMappings.put("ACCESS_GET_ROLE_ENDPOINTS","/api/get-role-endpoints");
        endpointMappings.put("ACCESS_GET_PERMISSION_ENDPOINTS","/api/get-permission-endpoints");

        // Common API
        endpointMappings.put("ACCESS_GET","/api/*/get/*");
        endpointMappings.put("ACCESS_GET_ALL","/api/*/get/*");
        endpointMappings.put("ACCESS_CREATE","/api/*/create");
        endpointMappings.put("ACCESS_UPDATE","/api/*/update/*");
        endpointMappings.put("ACCESS_DELETE","/api/*/delete/*");
        endpointMappings.put("ACCESS_DOWNLOAD_EXCEL","/api/*/download-excel");
        endpointMappings.put("ACCESS_DOWNLOAD_EXCEL_SAMPLE","/api/*/download-excel-sample");
        endpointMappings.put("ACCESS_DOWNLOAD_EXCEL_DUMMY","/api/*/download-excel-dummy");
        endpointMappings.put("ACCESS_VALIDATE_EXCEL_DATA","/api/*/validate-excel-data");
        endpointMappings.put("ACCESS_SAVE_CORRECT_LIST","/api/*/save-correct-list");

        // Role API
        endpointMappings.put("ACCESS_ROLE_ALL", "/api/roles/**");
        endpointMappings.put("ACCESS_ROLE_GET", "/api/roles/get/*");
        endpointMappings.put("ACCESS_ROLE_GET_ALL", "/api/roles/get-all");
        endpointMappings.put("ACCESS_ROLE_CREATE", "/api/roles/create");
        endpointMappings.put("ACCESS_ROLE_UPDATE", "/api/roles/update/*");
        endpointMappings.put("ACCESS_ROLE_DELETE", "/api/roles/delete/*");

        // Permission API
        endpointMappings.put("ACCESS_PERMISSION_ALL", "/api/permissions/**");
        endpointMappings.put("ACCESS_PERMISSION_GET", "/api/permissions/get/*");
        endpointMappings.put("ACCESS_PERMISSION_GET_ALL", "/api/permissions/get-all");
        endpointMappings.put("ACCESS_PERMISSION_CREATE", "/api/permissions/create");
        endpointMappings.put("ACCESS_PERMISSION_UPDATE", "/api/permissions/update/*");
        endpointMappings.put("ACCESS_PERMISSION_DELETE", "/api/permissions/delete/*");

        // Department API
        endpointMappings.put("ACCESS_DEPARTMENT_ALL","/api/departments/**");
        endpointMappings.put("ACCESS_DEPARTMENT_GET","/api/departments/get/*");
        endpointMappings.put("ACCESS_DEPARTMENT_GET_ALL","/api/departments/get-all");
        endpointMappings.put("ACCESS_DEPARTMENT_CREATE","/api/departments/create");
        endpointMappings.put("ACCESS_DEPARTMENT_UPDATE","/api/departments/update/*");
        endpointMappings.put("ACCESS_DEPARTMENT_DELETE","/api/departments/delete/*");
        endpointMappings.put("ACCESS_DEPARTMENT_DOWNLOAD_EXCEL","/api/departments/download-excel");
        endpointMappings.put("ACCESS_DEPARTMENT_DOWNLOAD_EXCEL_SAMPLE","/api/departments/download-excel-sample");
        endpointMappings.put("ACCESS_DEPARTMENT_DOWNLOAD_EXCEL_DUMMY","/api/departments/download-excel-dummy");
        endpointMappings.put("ACCESS_DEPARTMENT_VALIDATE_EXCEL_DATA","/api/departments/validate-excel-data");
        endpointMappings.put("ACCESS_DEPARTMENT_SAVE_CORRECT_LIST","/api/departments/save-correct-list");

        // UserType API
        endpointMappings.put("ACCESS_USERTYPE_ALL","/api/usertypes/**");
        endpointMappings.put("ACCESS_USERTYPE_GET","/api/usertypes/get/*");
        endpointMappings.put("ACCESS_USERTYPE_GET_ALL","/api/usertypes/get-all");
        endpointMappings.put("ACCESS_USERTYPE_CREATE","/api/usertypes/create");
        endpointMappings.put("ACCESS_USERTYPE_UPDATE","/api/usertypes/update/*");
        endpointMappings.put("ACCESS_USERTYPE_DELETE","/api/usertypes/delete/*");
        endpointMappings.put("ACCESS_USERTYPE_VALIDATE_EXCEL_DATA","/api/usertypes/validate-excel-data");
        endpointMappings.put("ACCESS_USERTYPE_SAVE_CORRECT_LIST","/api/usertypes/save-correct-list");
        endpointMappings.put("ACCESS_USERTYPE_DOWNLOAD_EXCEL","/api/usertypes/download-excel");
        endpointMappings.put("ACCESS_USERTYPE_DOWNLOAD_EXCEL_SAMPLE","/api/usertypes/download-excel-sample");
        endpointMappings.put("ACCESS_USERTYPE_DOWNLOAD_EXCEL_DUMMY","/api/usertypes/download-excel-dummy");

        // User API
        endpointMappings.put("ACCESS_USER_ALL","/api/users/**");
        endpointMappings.put("ACCESS_USER_GET","/api/users/get/*");
        endpointMappings.put("ACCESS_USER_GET_ALL","/api/users/get-all");
        endpointMappings.put("ACCESS_USER_CREATE","/api/users/create");
        endpointMappings.put("ACCESS_USER_UPDATE","/api/users/update/*");
        endpointMappings.put("ACCESS_USER_DELETE","/api/users/delete/*");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_IMAGE_FILE","/api/users/download-image-file/*");
        endpointMappings.put("ACCESS_USER_UPLOAD_ATTACHMENT","/api/users/upload-attachment/*");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_ATTACHMENT","/api/users/download-attachment/*");
        endpointMappings.put("ACCESS_USER_VALIDATE_EXCEL_DATA","/api/users/validate-excel-data");
        endpointMappings.put("ACCESS_USER_SAVE_CORRECT_LIST","/api/users/save-correct-list");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_EXCEL","/api/users/download-excel");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_EXCEL_SAMPLE","/api/users/download-excel-sample");
        endpointMappings.put("ACCESS_USER_DOWNLOAD_EXCEL_DUMMY","/api/users/download-excel-dummy");


    }
    static {
        roleEndpointMap.put("ACCESS API","ACCESS");
        roleEndpointMap.put("ACCESS USER","ACCESS_USER");
        roleEndpointMap.put("ACCESS DEPARTMENT","ACCESS_DEPARTMENT");
        roleEndpointMap.put("ACCESS USERTYPE","ACCESS_USERTYPE");
        roleEndpointMap.put("ACCESS ROLE","ACCESS_ROLE");
        roleEndpointMap.put("ACCESS PERMISSION","ACCESS_PERMISSION");
    }
    static {
        permissionEndpointMap.put("ALL","_ALL");
        permissionEndpointMap.put("READ","_GET");
        permissionEndpointMap.put("READ ALL","_GET_ALL");
        permissionEndpointMap.put("CREATE","_CREATE");
        permissionEndpointMap.put("UPDATE","_UPDATE");
        permissionEndpointMap.put("DELETE","_DELETE");
        permissionEndpointMap.put("DOWNLOAD IMAGE FILE","_DOWNLOAD_IMAGE_FILE");
        permissionEndpointMap.put("DOWNLOAD ATTACHMENT","_DOWNLOAD_ATTACHMENT");
        permissionEndpointMap.put("DOWNLOAD EXCEL","_DOWNLOAD_EXCEL");
        permissionEndpointMap.put("DOWNLOAD EXCEL SAMPLE","_DOWNLOAD_EXCEL_SAMPLE");
        permissionEndpointMap.put("DOWNLOAD EXCEL DUMMY","_DOWNLOAD_EXCEL_DUMMY");
        permissionEndpointMap.put("VALIDATE EXCEL DATA","_VALIDATE_EXCEL_DATA");
        permissionEndpointMap.put("SAVE CORRECT LIST","_SAVE_CORRECT_LIST");
    }
    public Map<String,String> getEndpointMap()
    {
        return endpointMappings;
    }
    public Map<String,String> getRoleEndpointMap(){
        return roleEndpointMap;
    }
    public Map<String,String> getPermissionEndpointMap()
    {
        return permissionEndpointMap;
    }
}
