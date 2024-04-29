package com.dreamsol.dto;


import com.dreamsol.entities.Role;
import com.dreamsol.helpers.GlobalHelper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto 
{
	@NotNull(message = "Missing field user name from input source")
	@Size(min = 3, max=100, message = "user name should contain min of 3 and max of 100 characters")
	@Pattern(regexp = GlobalHelper.REGEX_NAME, message = GlobalHelper.NAME_ERROR_MESSAGE)
	private String userName;

	@NotNull(message = "Missing field user email from input source")
	@Size(min = 8, max = 100, message = "email must be 8 to 100 characters long")
	@Email(regexp = GlobalHelper.REGEX_EMAIL, message = GlobalHelper.EMAIL_ERROR_MESSAGE)
	private String userEmail;

	@Min(value = 6000000000L, message = GlobalHelper.MOBILE_ERROR_MESSAGE)
	@Max(value = 9999999999L, message = GlobalHelper.MOBILE_ERROR_MESSAGE)
	private long userMobile;

	@Size(min = 8, max = 20, message = "Password must be 8 to 20 characters long")
	private String userPassword;

	private boolean status;

	@NotNull(message = "usertype must not be null")
	private UserTypeRequestDto userType;

	@NotNull(message = "department must not be null")
	private DepartmentRequestDto department;

	@Size(min = 1, message = "User role must be defined")
	private List<RoleRequestDto> roles;
}
