package com.dreamsol.services.impl;

import com.dreamsol.entities.User;
import com.dreamsol.repositories.UserRepository;
import com.dreamsol.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        if(username.equalsIgnoreCase("demo"))
        {
            return new UserDetailsImpl(userService.getDemoUser(),"");
        }
        User user = userRepository.findByUserEmail(username);
        if(Objects.isNull(user))
        {
            throw new UsernameNotFoundException("User with username: [" + username + "] not found!");
        }
        return new UserDetailsImpl(user,"");
    }
}
