package com.dreamsol.controllers;

import com.dreamsol.dto.RoleRequestDto;
import com.dreamsol.securities.JwtHelper;
import com.dreamsol.securities.JwtRequest;
import com.dreamsol.securities.JwtResponse;
import com.dreamsol.services.RoleService;
import com.dreamsol.services.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "Role", description = "This is role API")
public class SecurityController
{
    private RoleService roleService;
    private SecurityService securityService;
    @Operation(
            summary = "Login API",
            description = "This api helps to generate JWT token and login"
    )
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
    {
        return securityService.login(request);
    }
    @Operation(
            summary = "Logout API",
            description = "This api helps to logout"
    )
    @PostMapping("/logout")
    public ResponseEntity<JwtResponse> logout()
    {
        return securityService.logout();
    }
    @Operation(
            summary = "Create new role",
            description = "This api helps to create new role"
    )
    @PostMapping("/roles/create")
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequestDto roleRequestDto)
    {
        return roleService.createNewRole(roleRequestDto);
    }

}
