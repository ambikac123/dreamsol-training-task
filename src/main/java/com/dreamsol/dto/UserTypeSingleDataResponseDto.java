package com.dreamsol.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserTypeSingleDataResponseDto
{
	private String userTypeName;
	private String userTypeCode;
	private LocalDateTime timeStamp;
	private boolean status;
}
