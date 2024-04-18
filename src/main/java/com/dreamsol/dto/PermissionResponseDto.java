package com.dreamsol.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PermissionResponseDto
{
    private String permissionType;

    private List<String> endPoints;

    private boolean status;

    private LocalDateTime timeStamp;
}
