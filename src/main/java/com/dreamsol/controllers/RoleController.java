package com.dreamsol.controllers;

import com.dreamsol.dto.RoleRequestDto;
import com.dreamsol.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "ROLE API", description = "This api helps to create, update, delete and get roles")
@SecurityRequirement(name = "bearerAuth")
public class RoleController
{

    @Autowired private RoleService roleService;
    @Operation(
            summary = "Create new role",
            description = "This api helps to create new role"
    )
    @PostMapping("/create")
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequestDto roleRequestDto)
    {
        return roleService.createNewRole(roleRequestDto);
    }
    @Operation(
            summary = "Update role",
            description = "This api helps to update roles"
    )
    @PutMapping("/update/{roleId}")
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleRequestDto roleRequestDto,@PathVariable Long roleId)
    {
        return roleService.updateRole(roleRequestDto,roleId);
    }
    @Operation(
            summary = "Delete role",
            description = "This api helps to delete roles"
    )
    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId)
    {
        return roleService.deleteRole(roleId);
    }
    @Operation(
            summary = "Get role",
            description = "This api helps to get role"
    )
    @GetMapping("/get/{roleId}")
    public ResponseEntity<?> getRole(@PathVariable Long roleId)
    {
        return roleService.getRole(roleId);
    }
    @Operation(
            summary = "Get all roles",
            description = "This api helps to get all role"
    )
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllRoles()
    {
        return roleService.getAllRoles();
    }
}
