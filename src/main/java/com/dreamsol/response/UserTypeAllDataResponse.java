package com.dreamsol.response;

import java.util.List;

import com.dreamsol.dto.UserTypeResponseDto;
import com.dreamsol.helpers.PageInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserTypeAllDataResponse 
{
	private List<UserTypeResponseDto> contents;
	private PageInfo pageInfo;
}
