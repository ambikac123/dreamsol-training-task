package com.dreamsol.dto;


import com.dreamsol.helpers.GlobalHelper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto 
{
	@NotEmpty(message = "user name should not be empty!!")
	@Size(min = 3, max = 100, message = "user name should contain min of 3 and max of 100 characters")
	@Pattern(regexp = GlobalHelper.REGEX_NAME, message = "user name should contain [a-z, A-Z and space(' ')] and must start with at least 3 letter")
	private String userName;
	
	@NotEmpty(message = "Email should not be empty!!")
	@Size(min = 3, max = 100, message = "email must be 10 to 100 characters long")
	@Email(regexp = GlobalHelper.REGEX_EMAIL, message = "email can contain [a-z,A-Z,0-9,%,.,_,-] eg. example123@xyz.com")
	private String userEmail;
	
	@Min(value = 6000000000L, message = "Mobile number must start with [6-9] and must have 10 digits")
    @Max(value = 9999999999L, message = "Mobile number must contain 10 digits")
	private long userMobile;

	private boolean status;

	@NotNull(message = "usertype should not be null")
	private UserTypeRequestDto userType;

	@NotNull(message = "department should not be null")
	private DepartmentRequestDto department;
	
	
}
