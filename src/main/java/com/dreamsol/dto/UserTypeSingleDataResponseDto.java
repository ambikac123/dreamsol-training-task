package com.dreamsol.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserTypeResponseDto 
{
	private String userTypeName;
	private String userTypeCode;
	private List<UserResponseDto> users;
}
