package com.dreamsol.services.impl;

import com.dreamsol.entities.Permission;
import com.dreamsol.entities.Role;
import com.dreamsol.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails
{
    private final User user;
    private String ipAddress;
    public UserDetailsImpl(User user, String ipAddress)
    {
        this.user = user;
        this.ipAddress = ipAddress;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for(Role role : user.getRoles())
        {
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getRoleType()));
        }
        for(Permission permission : user.getPermissions())
        {
            grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getPermissionType()));
        }
        return grantedAuthorityList;
    }

    @Override
    public String getUsername() {
        return user.getUserEmail();
    }
    @Override
    public String getPassword() {
        return user.getUserPassword();
    }
    @Override
    public boolean isAccountNonExpired() {
        return user.isStatus();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isStatus();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isStatus();
    }

    @Override
    public boolean isEnabled() {
        return user.isStatus();
    }
}
