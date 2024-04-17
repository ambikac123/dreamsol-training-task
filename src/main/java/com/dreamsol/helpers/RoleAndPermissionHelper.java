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
        String[] adminPattern = new String[2];
        adminPattern[0] = "/api/departments/**";
        adminPattern[1] = "/api/usertypes/**";
        authorityPatterns.put("ROLE_ADMIN",adminPattern);

        String[] userPattern = new String[2];
        userPattern[0] = "/api/users/create";
        userPattern[1] = "/api/users/get-all";
        authorityPatterns.put("ROLE_USER",userPattern);
        return authorityPatterns;
    }
}
