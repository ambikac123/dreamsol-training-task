package com.dreamsol.dto;

import com.dreamsol.entities.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentResponseDto 
{
	private String departmentName;
	private String departmentCode;
	private List<UserResponseDto> users;
}
