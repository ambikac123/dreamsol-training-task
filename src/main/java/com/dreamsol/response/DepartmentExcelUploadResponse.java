package com.dreamsol.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentExcelUploadResponse
{
    private String departmentName;
    private String departmentCode;
    private boolean status;
    private String errorMessage;
}
