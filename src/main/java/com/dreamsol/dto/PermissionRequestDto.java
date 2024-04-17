package com.dreamsol.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequestDto
{
    @NotEmpty
    @Size(min = 3, max = 20, message = "permission can be of 3 to 20 characters long")
    @Pattern(regexp = "^[A-Z]{3,}([A-Z_]{3,})*$", message ="Only capital letters and underscores are allowed" )
    private String permissionType;

}
