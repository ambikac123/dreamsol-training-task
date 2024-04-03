package com.dreamsol.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class ExcelUploadResponse
{
    private List<?> correctList;
    private List<?> incorrectList;
}
