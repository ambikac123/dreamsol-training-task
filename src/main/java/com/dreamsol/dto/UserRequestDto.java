package com.dreamsol.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
	@NotEmpty(message = "Name should not be empty!!")
	@Size(min = 3, max = 50, message = "name must be 3 to 50 characters long")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Invalid user name!!")
	private String userName;
	
	@NotEmpty(message = "Email should not be empty!!")
	@Size(min = 3, max = 100, message = "email must be 10 to 100 characters long")
	@Email(message = "invalid email!!")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", message = "email must be contain [a-z,A-Z,0-9,%,.,_,-]")
	private String userEmail;
	
	@Min(value = 6000000000L, message = "Mobile number must start with [6-9]")
    @Max(value = 9999999999L, message = "Mobile number must contain of 10 digits")
	private long userMobile;
	
	
}
