package com.dreamsol.helpers;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class RoleAndPermissionHelper
{
    private final Map<String,String[]> authorityPatterns;
    public RoleAndPermissionHelper(Map<String,String[]> authorityPatterns)
    {
        this.authorityPatterns = authorityPatterns;
    }
    public Map<String,String[]> getAuthorityPatterns()
    {
        String[] adminPattern = {"/api/**"};
        authorityPatterns.put("ROLE_ADMIN",adminPattern);

        String[] userPattern = {"/api/users/get/*",
                "/api/users/get-all",
                "/api/users/update/*",
                "/api/users/delete/*",
                "/api/users/download-*",
                "/api/users/upload-file",
                "/api/users/download-file/*"
            };
        authorityPatterns.put("ROLE_USER",userPattern);

        String[] guestPattern = {"**/download-excel-*"};
        authorityPatterns.put("ROLE_GUEST",guestPattern);

        String[] readOnlyPattern = {"/api/**/get/",
                "/api/**/get-all",
                "/api/**/download-excel-*"
            };
        authorityPatterns.put("ROLE_READ_ONLY",readOnlyPattern);

        String[] writeOnlyPattern = {"/api/**/create","/api/**/update"};
        authorityPatterns.put("ROLE_WRITE_ONLY",writeOnlyPattern);

        String[] deletePattern = {"/api/**/delete/*"};
        authorityPatterns.put("ROLE_DELETE",deletePattern);
        return authorityPatterns;
    }
}
