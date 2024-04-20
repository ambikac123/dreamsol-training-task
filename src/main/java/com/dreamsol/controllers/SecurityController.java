package com.dreamsol.controllers;

import com.dreamsol.dto.PermissionRequestDto;
import com.dreamsol.dto.RoleRequestDto;
import com.dreamsol.helpers.EndpointMappingsHelper;
import com.dreamsol.securities.JwtRequest;
import com.dreamsol.securities.JwtResponse;
import com.dreamsol.services.PermissionService;
import com.dreamsol.services.RefreshTokenService;
import com.dreamsol.services.RoleService;
import com.dreamsol.services.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Common API", description = "This api has access to login, logout, role and permission")
public class SecurityController
{
    @Autowired private RoleService roleService;
    @Autowired private SecurityService securityService;
    @Autowired private RefreshTokenService refreshTokenService;
    @Autowired private PermissionService permissionService;
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
    public ResponseEntity<?> logout()
    {
        return securityService.logout();
    }
    @Operation(
            summary = "Create new token",
            description = "Create new token using refresh token"
    )
    @PostMapping("/re-generate-token")
    public ResponseEntity<?> reGenerateToken(@RequestParam String refreshToken)
    {
        return refreshTokenService.createTokenByRefreshToken(refreshToken);
    }

    @Operation(
            summary = "Get all endpoints",
            description = "This will help to get all endpoints"
    )
    @GetMapping("/get-endpoints")
    public ResponseEntity<?> getAllEndpoints()
    {
        return securityService.getAllEndpoints();
    }
    @Operation(
            summary = "Update endpoints",
            description = "This will help to update endpoints"
    )
    @PutMapping("/update-endpoints")
    public ResponseEntity<?> updateEndpoints()
    {
        return securityService.updateEndpoints();
    }
    /* ---------------------------------- Role Controllers ------------------------------------------- */
    @Operation(
            summary = "Create new role",
            description = "This api helps to create new role"
    )
    @PostMapping("/roles/create")
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequestDto roleRequestDto)
    {
        return roleService.createNewRole(roleRequestDto);
    }
    @Operation(
            summary = "Update role",
            description = "This api helps to update roles"
    )
    @PutMapping("/roles/update/{roleId}")
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleRequestDto roleRequestDto,@PathVariable Long roleId)
    {
        return roleService.updateRole(roleRequestDto,roleId);
    }
    @Operation(
            summary = "Delete role",
            description = "This api helps to delete roles"
    )
    @DeleteMapping("/roles/delete/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId)
    {
        return roleService.deleteRole(roleId);
    }
    @Operation(
            summary = "Get role",
            description = "This api helps to get role"
    )
    @GetMapping("/roles/get/{roleId}")
    public ResponseEntity<?> getRole(@PathVariable Long roleId)
    {
        return roleService.getRole(roleId);
    }
    @Operation(
            summary = "Get all roles",
            description = "This api helps to get all role"
    )
    @GetMapping("/roles/get-all")
    public ResponseEntity<?> getAllRoles()
    {
        return roleService.getAllRoles();
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
    public ResponseEntity<?> getPermission(@PathVariable Long permissionId)
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
