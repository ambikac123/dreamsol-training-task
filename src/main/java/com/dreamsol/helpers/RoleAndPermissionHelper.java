package com.dreamsol.helpers;

import com.dreamsol.entities.Endpoint;
import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.entities.User;
import com.dreamsol.repositories.EndpointRepository;
import com.dreamsol.repositories.PermissionRepository;
import com.dreamsol.repositories.RoleRepository;
import com.dreamsol.services.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RoleAndPermissionHelper
{
    @Autowired private PermissionRepository permissionRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private EndpointRepository endpointRepository;
    @Autowired private EndpointMappingsHelper endpointMappingsHelper;
    private final Map<String,String[]> authorityNamesAndUrlsMap;
    public RoleAndPermissionHelper(Map<String,String[]> authorityNamesAndUrlsMap)
    {
        this.authorityNamesAndUrlsMap = authorityNamesAndUrlsMap;
    }
   public Map<String,String[]> getAuthorityNameAndUrlsMap()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetailsImpl.getUser();
        for(Role role : user.getRoles())
        {
            String roleType = role.getRoleType();
            String[] endPointUrls = role.getEndPoints()
                    .stream()
                    .map(Endpoint::getEndPointLink)
                    .toList()
                    .toArray(new String[0]);
            authorityNamesAndUrlsMap.put(roleType,endPointUrls);
        }
        for (Permission permission : user.getPermissions())
        {
            String permissionType = permission.getPermissionType();
            String[] endPointUrls = permission.getEndPoints()
                            .stream()
                            .map(Endpoint::getEndPointLink)
                            .toList()
                            .toArray(new String[0]);
            authorityNamesAndUrlsMap.put(permissionType,endPointUrls);
        }
        return authorityNamesAndUrlsMap;
    }
}
