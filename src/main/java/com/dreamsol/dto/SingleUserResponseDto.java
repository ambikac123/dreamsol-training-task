package com.dreamsol.dto;

import com.dreamsol.response.ApiResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SingleUserResponseDto
{
    private String userName;
    private long userMobile;
    private String userEmail;
    private boolean status;
    private UserTypeSingleDataResponseDto userType;
    private DepartmentSingleDataResponseDto department;
    private ApiResponse responseMessage;
}
