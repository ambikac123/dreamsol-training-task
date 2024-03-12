package com.dreamsol.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User 
{
	@Id
	@GeneratedValue(generator = "user_seq")
	@SequenceGenerator(name = "user_seq", initialValue = 1001, allocationSize = 1)
	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "user_name", length = 50)
	private String userName;
	
	@Column(name = "user_email", length = 50)
	private String userEmail;
	
	@Column(name = "user_mobile", length = 10, unique = true)
	private long userMobile;
	
	@Column(name = "user_image",unique = true)
	private String imageURI;
	
}


