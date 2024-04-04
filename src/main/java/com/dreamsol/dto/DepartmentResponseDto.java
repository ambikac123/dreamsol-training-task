package com.dreamsol.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentResponseDto 
{
	private String departmentName;
	private String departmentCode;
	private LocalDateTime timeStamp;
	private boolean status;
	List<UserResponseDto> users;
}
