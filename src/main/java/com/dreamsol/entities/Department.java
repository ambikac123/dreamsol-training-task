package com.dreamsol.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Department 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long departmentId;
	
	@Column(length = 100,unique = true,nullable = false)
	private String departmentName;
	
	@Column(length = 7,unique = true,nullable = false)
	private String departmentCode;

	@Column(nullable = false)
	private LocalDateTime timeStamp;

	@Column(nullable = false)
	private boolean status;

}
