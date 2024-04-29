package com.dreamsol.controllers;

import com.dreamsol.dto.PermissionRequestDto;
import com.dreamsol.services.PermissionService;
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
@RequestMapping("/api/permissions")
@Tag(name = "PERMISSION API", description = "This api helps to create, update, delete and get permissions")
@SecurityRequirement(name = "bearerAuth")
public class PermissionController
{

    @Autowired private PermissionService permissionService;
    @Operation(
            summary = "Create access permissions",
            description = "This api helps to create custom permissions."
    )
    @PostMapping("/create")
    public ResponseEntity<?> createPermission(@Valid @RequestBody PermissionRequestDto permissionRequestDto)
    {
        return permissionService.createPermission(permissionRequestDto);
    }
    @Operation(
            summary = "Update permission",
            description = "This api helps to modify permissions basis of permissionId"
    )
    @PutMapping("/update/{permissionId}")
    public ResponseEntity<?> updatePermission(@Valid @RequestBody PermissionRequestDto permissionRequestDto,@PathVariable Long permissionId)
    {
        return permissionService.updatePermission(permissionRequestDto,permissionId);
    }
    @Operation(
            summary = "Delete permission",
            description = "This api helps to delete permissions"
    )
    @DeleteMapping("/delete/{permissionId}")
    public ResponseEntity<?> deletePermission(@PathVariable Long permissionId)
    {
        return permissionService.deletePermission(permissionId);
    }
    @Operation(
            summary = "Get single permission",
            description = "This api helps to get all permissions"
    )
    @GetMapping("/get/{permissionId}")
    public ResponseEntity<?> getPermission(@PathVariable Long permissionId)
    {
        return permissionService.getPermission(permissionId);
    }
    @Operation(
            summary = "Get all permission",
            description = "This api helps to get all permissions"
    )
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllPermission()
    {
        return permissionService.getAllPermission();
    }
}
