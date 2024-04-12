package com.dreamsol.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ApplicationUsers
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long appUserId;

    @Size(min = 6, max = 20, message = "the username must be 6 to 20 characters long")
    @Pattern(regexp = "^[a-zA-Z]+[0-9]+$", message = "the username must contains letters followed by digits")
    private String username;

    @Size(min = 6, max = 10, message = "The password must be 6 to 10 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@./+-])[A-Za-z\\\\d@./+-]+$", message = "The password must have atleast a lowercase, uppercase, digit and symbol character")
    private String password;

    @OneToOne
    private Role role;
}
