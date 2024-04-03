package com.dreamsol.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserExcelUploadResponse
{
    private String userName;
    private String userEmail;
    private long userMobile;
    private String userTypeName;
    private String userTypeCode;
    private String departmentName;
    private String departmentCode;
    private boolean status;
    private String errorMessage;
}
