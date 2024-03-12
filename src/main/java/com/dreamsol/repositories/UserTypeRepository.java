package com.dreamsol.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dreamsol.entities.UserType;

public interface UserTypeRepository extends JpaRepository<UserType,Long>
{

	@Query("select ut from UserType ut where ut.userTypeName like :key")
	List<UserType> findByName(@Param("key") String keywords);

	@Query("select ut from UserType ut where ut.userTypeCode like :key")
	List<UserType> findByCode(@Param("key") String keywords);
	
}
