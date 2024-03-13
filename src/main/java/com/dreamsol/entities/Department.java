package com.dreamsol.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "department")
public class Department 
{
	@Id
	@GeneratedValue(generator = "department_seq")
	@SequenceGenerator(name = "department_seq", initialValue = 101, allocationSize = 1)
	@Column(name = "department_id")
	private long departmentId;
	
	@Column(name = "department_name", length = 50)
	private String departmentName;
	
	@Column(name = "department_code", length = 10)
	private String departmentCode;

}
