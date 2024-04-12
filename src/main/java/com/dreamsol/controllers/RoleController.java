package com.dreamsol.controllers;

import com.dreamsol.dto.RoleRequestDto;
import com.dreamsol.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "Role", description = "This is role API")
public class RoleController
{
    private RoleService roleService;
    @Operation(
            summary = "Create new role",
            description = "This api helps to create new role"
    )
    @PostMapping("/create")
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequestDto roleRequestDto)
    {
        return roleService.createNewRole(roleRequestDto);
    }
}
