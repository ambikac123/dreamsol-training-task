package com.dreamsol.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamsol.entities.UserType;

public interface UserTypeRepository extends JpaRepository<UserType,Long>
{
    UserType findByUserTypeNameAndUserTypeCode(String userTypeName, String userTypeCode);
    List<UserType> findByUserTypeNameLikeOrUserTypeCodeLike(String userTypeName,String userTypeCode);
    Page<UserType> findByUserTypeNameLikeOrUserTypeCodeLike(String keyword1,String keyword2,Pageable pageable);
}
