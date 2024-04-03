package com.dreamsol.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto 
{
	private String userName;
	private String userEmail;
	private long userMobile;
	private String imageURI;
	private UserTypeResponseDto userType;
	private DepartmentResponseDto department;
}
