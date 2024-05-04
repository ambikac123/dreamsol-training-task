package com.dreamsol.controllers;

import com.dreamsol.services.EndpointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "ENDPOINT API", description = "This API is used to manage endpoint")
@SecurityRequirement(name = "bearerAuth")
public class EndpointController
{
    @Autowired private EndpointService endpointService;
    @Operation(
            summary = "Get all endpoints",
            description = "This will help to get all endpoints"
    )
    @GetMapping("/get-endpoints")
    public ResponseEntity<?> getAllEndpoints()
    {
        return endpointService.getAllEndpoints();
    }
    @Operation(
            summary = "Update endpoints",
            description = "This will help to update endpoints"
    )
    @PutMapping("/update-endpoints")
    public ResponseEntity<?> updateEndpoints()
    {
        return endpointService.updateEndpoints();
    }

    @Operation(
            summary = "Get all endpoints for role",
            description = "This api will help to get role related endpoints."
    )
    @GetMapping("/get-role-endpoints")
    public ResponseEntity<?> getRoleEndpoints()
    {
        return endpointService.getRoleRelatedEndpoints();
    }

    @Operation(
            summary = "Get all endpoints for permission",
            description = "This api will help to get permission related endpoints."
    )
    @GetMapping("/get-permission-endpoints")
    public ResponseEntity<?> getPermissionEndpoints()
    {
        return endpointService.getPermissionRelatedEndpoints();
    }
}
