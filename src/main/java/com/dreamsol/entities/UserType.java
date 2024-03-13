package com.dreamsol.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
	@Schema(hidden = true)
	private long userTypeId;
	
	// UserTypeName : Normal User/Head of Department/Administrator
	@Column(name = "usertype_name", length = 50)
	private String userTypeName;
	
	// UserTypeCode : NOR/ HOD/ ADM
	@Column(name = "usertype_code", length = 10)
	private String userTypeCode;

}
