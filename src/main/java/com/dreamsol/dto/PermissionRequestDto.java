package com.dreamsol.dto;

import com.dreamsol.entities.EndpointMappings;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PermissionRequestDto
{
    @NotEmpty
    @Size(min = 3, max = 20, message = "permission can be of 3 to 20 characters long")
    @Pattern(regexp = "^[A-Z]{3,}([A-Z_]{3,})*$", message ="Only capital letters and underscores are allowed" )
    private String permissionType;

    @Size(min = 1, message = "endpoints must not be empty")
    private List<String> endPoints;

    private boolean status;
}
