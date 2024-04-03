package com.dreamsol.dto;

import com.dreamsol.helpers.GlobalHelper;
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
public class UserTypeRequestDto 
{
	
	@NotEmpty(message = "usertype name should not be empty")
	@Size(min = 3, max = 100, message = "usertype name should contain min of 3 and max of 100 characters")
	@Pattern(regexp = GlobalHelper.REGEX_NAME, message = "usertype name should contain [a-z, A-Z and space(' ')] and must start with at least 3 letter")
	private String userTypeName;
	
	@NotEmpty(message = "usertype code should not be empty")
	@Size(min = 2, max = 7, message = "usertype code must be of min of 2 and max of 7 characters long")
	@Pattern(regexp = GlobalHelper.REGEX_CODE, message = "usertype code can contain [a-z, A-Z, 0-9] and must start with at least 2 letters and followed by maximum 3 digits")
	private String userTypeCode;

	private boolean status;

}
