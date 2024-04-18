package com.dreamsol.controllers;

import com.dreamsol.dto.PermissionRequestDto;
import com.dreamsol.dto.RoleRequestDto;
import com.dreamsol.securities.JwtRequest;
import com.dreamsol.securities.JwtResponse;
import com.dreamsol.services.PermissionService;
import com.dreamsol.services.RefreshTokenService;
import com.dreamsol.services.RoleService;
import com.dreamsol.services.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "Role", description = "This is role API")
public class SecurityController
{
    private RoleService roleService;
    private SecurityService securityService;
    private RefreshTokenService refreshTokenService;
    private PermissionService permissionService;
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
            summary = "Create new token",
            description = "Create new token using refresh token"
    )
    @PostMapping("/create-token")
    public ResponseEntity<?> createTokenByRefreshToken(@RequestParam String refreshToken)
    {
        return refreshTokenService.createTokenByRefreshToken(refreshToken);
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
    /* ----------------------------------- Permission Controllers ----------------------------------- */
    @Operation(
            summary = "Create access permissions",
            description = "This api helps to create custom permissions."
    )
    @PostMapping("/permissions/create")
    public ResponseEntity<?> createPermission(@Valid @RequestBody PermissionRequestDto permissionRequestDto)
    {
        return permissionService.createPermission(permissionRequestDto);
    }
    @Operation(
            summary = "Update permission",
            description = "This api helps to modify permissions basis of permissionId"
    )
    @PutMapping("/permissions/update/{permissionId}")
    public ResponseEntity<?> updatePermission(@Valid @RequestBody PermissionRequestDto permissionRequestDto,@PathVariable Long permissionId)
    {
        return permissionService.updatePermission(permissionRequestDto,permissionId);
    }
    @Operation(
            summary = "Delete permission",
            description = "This api helps to delete permissions"
    )
    @DeleteMapping("/permissions/delete/{permissionId}")
    public ResponseEntity<?> deletePermission(@PathVariable Long permissionId)
    {
        return permissionService.deletePermission(permissionId);
    }
    @Operation(
            summary = "Get single permission",
            description = "This api helps to get all permissions"
    )
    @GetMapping("/permissions/get/{permissionId}")
    public ResponseEntity<?> getSinglePermission(@PathVariable Long permissionId)
    {
        return permissionService.getPermission(permissionId);
    }
    @Operation(
            summary = "Get all permission",
            description = "This api helps to get all permissions"
    )
    @GetMapping("/permissions/get-all")
    public ResponseEntity<?> getAllPermission()
    {
        return permissionService.getAllPermission();
    }
}
