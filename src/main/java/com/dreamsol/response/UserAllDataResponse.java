package com.dreamsol.response;

import java.util.List;

import com.dreamsol.dto.UserSingleDataResponseDto;
import com.dreamsol.helpers.PageInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserAllDataResponse
{
	private List<UserSingleDataResponseDto> contents;
	private PageInfo pageInfo;
}
