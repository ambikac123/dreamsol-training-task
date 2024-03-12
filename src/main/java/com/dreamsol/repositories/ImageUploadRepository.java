package com.dreamsol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamsol.entities.User;

public interface ImageUploadRepository extends JpaRepository<User,Long>{

}
