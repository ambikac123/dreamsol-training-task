package com.dreamsol.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RoleResponseDto
{
    private String roleName;
    private String description;
    private boolean status;
    private LocalDateTime timeStamp;
}