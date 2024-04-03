package com.dreamsol.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTypeExcelUploadResponse
{
    private String userTypeName;
    private String userTypeCode;
    private boolean status;
    private String errorMessage;
}
