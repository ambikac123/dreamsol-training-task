package com.dreamsol.helpers;

import com.dreamsol.entities.Endpoint;
import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RoleAndPermissionHelper
{
    @Autowired private EndpointMappingsHelper endpointMappingsHelper;
    public String[] getUserRelatedUrls(User user)
    {
        Map<String,String> roleAndPermissionMap = endpointMappingsHelper.getEndpointMap();
        Map<String,String> roleMap = endpointMappingsHelper.getRoleEndpointMap();
        Map<String,String> permissionMap = endpointMappingsHelper.getPermissionEndpointMap();
        List<String> urlPatterns = new ArrayList<>();
        for(Role role : user.getRoles())
        {
            for(Endpoint endpoint1 : role.getEndpoints())
            {
                String key1 = endpoint1.getEndPointKey();
                for(Permission permission : user.getPermissions())
                {
                    for(Endpoint endpoint2 : permission.getEndPoints())
                    {
                        String key2 = endpoint2.getEndPointKey();
                        String finalKey = roleMap.get(key1) + permissionMap.get(key2);
                        urlPatterns.add(roleAndPermissionMap.get(finalKey));
                    }
                }
            }
        }
        return urlPatterns.toArray(new String[]{});
    }
}
