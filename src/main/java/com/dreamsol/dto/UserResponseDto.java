package com.dreamsol.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto 
{
	private String userName;
	private String userEmail;
	private long userMobile;
	private String imageName;
	private LocalDateTime timeStamp;
	private boolean status;
}
