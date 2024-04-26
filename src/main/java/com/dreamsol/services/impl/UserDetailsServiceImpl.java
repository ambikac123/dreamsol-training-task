package com.dreamsol.services.impl;

import com.dreamsol.entities.Endpoint;
import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.entities.User;
import com.dreamsol.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired private UserRepository userRepository;
    // loadUserByUsername() method is called by spring security framework while attempting to login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        if(username.equalsIgnoreCase("demo"))
        {
            User user = new User();
            user.setUserEmail("demo");
            user.setUserPassword(new BCryptPasswordEncoder().encode("demo"));
            user.setStatus(true);
            Role role = new Role();
            role.setRoleType("DEMO");
            role.setStatus(true);
            role.setEndPoints(List.of(new Endpoint("ACCESS_ALL","/api/**")));
            user.setRoles(List.of(role));
            Permission permission = new Permission();
            permission.setPermissionType("ALL");
            permission.setStatus(true);
            permission.setEndPoints(List.of(new Endpoint("ACCESS_ALL","/api/**")));
            user.setPermissions(List.of(permission));
            return new UserDetailsImpl(user,"");
        }
        User user = userRepository.findByUserEmail(username);
        if(Objects.isNull(user)) {
            throw new UsernameNotFoundException("User with username: [" + username + "] not found!");
        }
        return new UserDetailsImpl(user,"");
    }
}
