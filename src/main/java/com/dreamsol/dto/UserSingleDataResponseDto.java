package com.dreamsol.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserSingleDataResponseDto
{
	private String userName;
	private String userEmail;
	private long userMobile;
	private String imageName;
	private List<RoleResponseDto> roles;
	private LocalDateTime timeStamp;
	private boolean status;
	private UserTypeSingleDataResponseDto userType;
	private DepartmentSingleDataResponseDto department;
	private List<DocumentSingleDataResponseDto> attachments;
}
