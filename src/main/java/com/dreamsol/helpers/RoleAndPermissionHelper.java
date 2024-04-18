package com.dreamsol.helpers;

import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.repositories.PermissionRepository;
import com.dreamsol.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RoleAndPermissionHelper
{
    @Autowired private PermissionRepository permissionRepository;
    @Autowired private RoleRepository roleRepository;
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
}
