package com.dreamsol.response;

import java.util.List;

import com.dreamsol.dto.DepartmentResponseDto;
import com.dreamsol.helpers.PageInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DepartmentAllDataResponse
{
	private List<DepartmentResponseDto> contents;
	private PageInfo pageInfo;
}
