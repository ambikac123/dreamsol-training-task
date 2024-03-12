package com.dreamsol.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dreamsol.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	@Query("SELECT u FROM User u WHERE u.userMobile = :mobile")
	List<User> findByMobile(@Param("mobile") Long mobile);

	@Query("SELECT u FROM User u WHERE u.userEmail = :email")
	List<User> findByEmail(@Param("email") String email);
}
