package com.dreamsol.helpers;

import com.dreamsol.entities.EndpointMappings;
import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.repositories.EndpointMappingsRepository;
import com.dreamsol.repositories.PermissionRepository;
import com.dreamsol.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class RoleAndPermissionHelper
{
    @Autowired private PermissionRepository permissionRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private EndpointMappingsRepository endpointMappingsRepository;
    @Autowired private EndpointMappingsHelper endpointMappingsHelper;
    private final Map<String,String[]> allRoleAndPermissionMap;
    public RoleAndPermissionHelper(Map<String,String[]> allRoleAndPermissionMap)
    {
        this.allRoleAndPermissionMap = allRoleAndPermissionMap;
        allRoleAndPermissionMap.put("ROLE_DEVELOPER",new String[]{"ACCESS_ALL"});
        allRoleAndPermissionMap.put("ROLE_ALL",new String[]{"ACCESS_ALL"});
    }
   public Map<String,String[]> getAllRoleAndPermissionMap()
    {
        List<Role> roles = roleRepository.findAllByStatusTrue();
        List<Permission> permissions = permissionRepository.findAllByStatusTrue();
        for(Role role : roles)
        {
            String roleType = role.getRoleType();
            String[] endPoints = role.getEndPoints()
                    .stream()
                    .map(EndpointMappings::getEndPointKey)
                    .toList()
                    .toArray(new String[0]);
            allRoleAndPermissionMap.put("ROLE_"+roleType,endPoints);
        }
        for (Permission permission : permissions)
        {
            String permissionType = permission.getPermissionType();
            String[] endPoints = permission.getEndPoints()
                            .stream()
                            .map(EndpointMappings::getEndPointKey)
                            .toList()
                            .toArray(new String[0]);
            allRoleAndPermissionMap.put(permissionType,endPoints);
        }
        return allRoleAndPermissionMap;
    }
    public void addMappingInDB()
    {
        Map<String,String> endpointMappings = endpointMappingsHelper.getEndpointMappings();
        System.out.println(endpointMappings);
    }
}
