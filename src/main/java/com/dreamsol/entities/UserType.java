package com.dreamsol.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "usertype")
public class UserType 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private long userTypeId;

	@Column(length = 100,nullable = false,unique = true)
	private String userTypeName;

	@Column(length = 100,nullable = false,unique = true)
	private String userTypeCode;

	private LocalDateTime timeStamp;

	@Column(nullable = false)
	private boolean status;

}
