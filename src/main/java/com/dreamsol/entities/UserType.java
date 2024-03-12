package com.dreamsol.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "usertype")
public class UserType 
{
	@Id
	@GeneratedValue(generator = "usertype_seq")
	@SequenceGenerator(name = "usertype_seq", initialValue = 101, allocationSize = 1)
	@Column(name = "usertype_id")
	private long userTypeId;
	
	@Column(name = "usertype_name", length = 50)
	private String userTypeName;
	
	@Column(name = "usertype_code", length = 10)
	private String userTypeCode;
	
}
