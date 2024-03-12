package com.dreamsol.response;

import java.util.List;

import com.dreamsol.dto.UserTypeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserTypeAllDataResponse 
{
	private List<UserTypeDto> contents;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean firstPage;
	private boolean lastPage;
}
