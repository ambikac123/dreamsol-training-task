package com.dreamsol.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserTypeResponseDto 
{
	private String userTypeName;
	private String userTypeCode;
	private LocalDateTime timeStamp;
	private boolean status;
	List<UserResponseDto> users;
}
