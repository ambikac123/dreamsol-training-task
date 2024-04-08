package com.dreamsol.dto;

import com.dreamsol.helpers.GlobalHelper;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DepartmentRequestDto 
{
	
	@NotNull(message = "Missing field department name from input source")
	@Size(min = 3, max=100, message = "department name should contain min of 3 and max of 100 characters")
	@Pattern(regexp = GlobalHelper.REGEX_NAME, message = GlobalHelper.NAME_ERROR_MESSAGE)
	private String departmentName;
	
	@NotNull(message = "Missing field department code from input source")
	@Size(min = 2, max = 7, message = "department code must be of min of 2 and max of 7 characters long")
	@Pattern(regexp = GlobalHelper.REGEX_CODE, message = GlobalHelper.CODE_ERROR_MESSAGE)
	private String departmentCode;

	private boolean status;
	
}

