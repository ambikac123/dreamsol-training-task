package com.dreamsol.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RoleResponseDto
{
    private String roleType;

    private List<String> permissions;

    private boolean status;

    private LocalDateTime timeStamp;
}
