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
	private String userName;

	private String userEmail;

	private long userMobile;

	private boolean status;

	private UserTypeRequestDto userType;

	private DepartmentRequestDto department;

}
