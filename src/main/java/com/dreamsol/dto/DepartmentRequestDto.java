package com.dreamsol.dto;

import com.dreamsol.helpers.GlobalHelper;
import jakarta.validation.constraints.NotEmpty;
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
	
	@NotEmpty(message = "department name should not be empty")
	@Size(min = 3, max=100, message = "department name should contain min of 3 and max of 100 characters")
	@Pattern(regexp = GlobalHelper.REGEX_NAME, message = "department"+GlobalHelper.NAME_ERROR_MESSAGE)
	private String departmentName;
	
	@NotEmpty(message = "department code should not be empty")
	@Size(min = 2, max = 7, message = "department code must be of min of 2 and max of 7 characters long")
	@Pattern(regexp = GlobalHelper.REGEX_CODE, message = "department"+GlobalHelper.CODE_ERROR_MESSAGE)
	private String departmentCode;

	private boolean status;
	
}

