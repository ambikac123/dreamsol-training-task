package com.dreamsol.helpers;

import com.dreamsol.entities.Endpoint;
import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleAndPermissionHelper
{
    public String[] getUserRelatedUrls(User user)
    {
        List<String> urlPatterns = new ArrayList<>();
        List<Role> roleList = user.getRoles();
        for(Role role : roleList)
        {
            List<Permission> permissionList = role.getPermissions();
            for(Permission permission : permissionList)
            {
                List<Endpoint> endpointList = permission.getEndPoints();
                for(Endpoint endpoint : endpointList)
                {
                    urlPatterns.add(endpoint.getEndPointLink());
                }
            }
        }
        return urlPatterns.toArray(new String[]{});
    }
}
