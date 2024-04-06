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
	@NotEmpty(message = "user name should not be empty")
	@Size(min = 3, max=100, message = "user name should contain min of 3 and max of 100 characters")
	@Pattern(regexp = GlobalHelper.REGEX_NAME, message = "user"+GlobalHelper.NAME_ERROR_MESSAGE)
	private String userName;

	@NotEmpty(message = "Email should not be empty!!")
	@Size(min = 3, max = 100, message = "email must be 10 to 100 characters long")
	@Email(regexp = GlobalHelper.REGEX_EMAIL, message = "user"+GlobalHelper.EMAIL_ERROR_MESSAGE)
	private String userEmail;

	@Min(value = 6000000000L, message = "user"+GlobalHelper.MOBILE_ERROR_MESSAGE)
	@Max(value = 9999999999L, message = "user"+GlobalHelper.MOBILE_ERROR_MESSAGE)
	private long userMobile;

	private boolean status;

	@NotNull(message = "usertype must not be null")
	private UserTypeRequestDto userType;

	@NotNull(message = "department must not be null")
	private DepartmentRequestDto department;

}
