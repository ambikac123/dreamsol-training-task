package com.dreamsol.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserExcelResponseDto
{
    private List<SingleUserResponseDto> correctUsersList;
    private List<SingleUserResponseDto> incorrectUsersList;
}
