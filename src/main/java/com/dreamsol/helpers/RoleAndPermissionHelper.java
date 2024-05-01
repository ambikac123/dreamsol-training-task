package com.dreamsol.helpers;

import com.dreamsol.entities.Endpoint;
import com.dreamsol.entities.Permission;
import com.dreamsol.repositories.PermissionRepository;
import com.dreamsol.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoleAndPermissionHelper
{
    @Autowired private RoleRepository roleRepository;
    @Autowired private PermissionRepository permissionRepository;
   public Map<String,String[]> getAuthorityNameAndUrlsMap()
    {
        Map<String,String[]> authorityNameAndUrlsMap = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for(GrantedAuthority authority : authorities)
        {
            String roleType = authority.getAuthority();
            List<String> endpoints = new ArrayList<>();
            List<Permission> permissionList = getPermissionList(roleType);
            for (Permission permission : permissionList)
            {
                String permissionType = permission.getPermissionType();
                List<Endpoint> endpointList = getEndpointList(permissionType);
                for (Endpoint endpoint : endpointList)
                {
                    endpoints.add(endpoint.getEndPointLink());
                }
            }
            authorityNameAndUrlsMap.put(roleType,endpoints.toArray(new String[]{}));
        }
        return authorityNameAndUrlsMap;
    }
    private List<Permission> getPermissionList(String roleType)
    {
        return roleRepository.findByRoleType(roleType).getPermissions();
    }
    private List<Endpoint> getEndpointList(String permissionType)
    {
        return permissionRepository.findByPermissionType(permissionType).getEndPoints();
    }
}
