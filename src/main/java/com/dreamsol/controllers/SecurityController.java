package com.dreamsol.controllers;

import com.dreamsol.securities.LoginRequest;
import com.dreamsol.services.RefreshTokenService;
import com.dreamsol.services.RoleService;
import com.dreamsol.services.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "SECURITY API", description = "This api has access to login, re-generate token, ")
@SecurityRequirement(name = "bearerAuth")
public class SecurityController
{
    @Autowired private RoleService roleService;
    @Autowired private SecurityService securityService;
    @Autowired private RefreshTokenService refreshTokenService;
    @Operation(
            summary = "Login API",
            description = "This api helps to generate JWT token and login"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request)
    {
        return securityService.login(request);
    }
    @Operation(
            summary = "Regenerate token API",
            description = "This api helps to logout"
    )

    @GetMapping("/re-generate-token")
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
}
