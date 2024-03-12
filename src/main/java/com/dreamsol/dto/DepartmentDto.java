package com.dreamsol.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DepartmentDto 
{
	
	@NotEmpty(message = "Department name should not be empty")
	@Size(min = 3, max=50, message = "Department name should contain min of 3 and max of 50 characters")
	@Pattern(regexp = "^[a-zA-Z- ]+$", message = "UserType name must contain [a-z, A-Z, hiphen(-) and space(' ')] only")
	private String departmentName;
	
	@NotEmpty(message = "Department code should not be empty")
	@Size(min = 2, max = 5, message = "Departmet code must be of min of 2 and max of 6 characters long")
	@Pattern(regexp = "^[a-zA-Z-]+$", message = "UserType name must contain [a-z, A-Z, hiphen(-) and space(' ')] only")
	private String departmentCode;
	
}

