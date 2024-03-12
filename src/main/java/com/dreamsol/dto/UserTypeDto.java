package com.dreamsol.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeDto 
{
	
	@NotEmpty(message = "UserType name should not be empty")
	@Size(min = 3, max = 50, message = "UserType name should contain min of 3 and max of 50 characters")
	@Pattern(regexp = "^[a-zA-Z- ]+$", message = "UserType name must contain [a-z, A-Z, hiphen(-) and space(' ')] only")
	private String userTypeName;
	
	@NotEmpty(message = "UserType code should not be empty")
	@Size(min = 2, max = 5, message = "UserType code must be of min of 2 and max of 6 characters long")
	@Pattern(regexp = "^[a-zA-Z-]+$", message = "UserType name must contain [a-z, A-Z, hiphen(-) and space(' ')] only")
	private String userTypeCode;
	
}
