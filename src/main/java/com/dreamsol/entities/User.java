package com.dreamsol.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class User 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	
	@Column(length = 100,nullable = false)
	private String userName;
	
	@Column(length = 10, unique = true,nullable = false)
	private long userMobile;

	@Column(length = 100,unique = true,nullable = false)
	private String userEmail;

	@Column(unique = true,nullable = false)
	private String userPassword;

	private LocalDateTime timeStamp; 

	@Column(nullable = false)
	private boolean status;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userimage_id", unique = true)
	private UserImage userImage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usertype_id")
	private UserType userType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	@OneToMany(mappedBy = "user")
	private List<Document> documentList;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
	private List<Role> roles;

}