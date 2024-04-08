package com.dreamsol.repositories;

import java.util.List;

import com.dreamsol.entities.Department;
import com.dreamsol.entities.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamsol.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUserMobileAndUserEmail(long userMobile, String userEmail);

    @Query(value = "SELECT * FROM user WHERE user_mobile LIKE %:mobileNo%", nativeQuery = true)
    List<User> findByUserMobileLike(String mobileNo);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.userType " +
            "LEFT JOIN FETCH u.department " +
            "LEFT JOIN FETCH u.userImage " +
            "LEFT JOIN FETCH u.documentList d " +
            "WHERE CAST(u.userMobile AS string) LIKE CONCAT('%', :mobileNo, '%') " +
            "ORDER BY u.userId")
    Page<User> findByUserMobileLikeOrderByUserId(@Param("mobileNo") String mobileNo, Pageable pageable);
    List<User> findByUserNameLikeOrUserEmailLike(String userName, String userEmail);
    //Page<User> findByUserNameLikeOrUserEmailLike(String userName, String userEmail, Pageable pageable);
    List<User> findAllByUserTypeIn(List<UserType> userTypeList);
    Page<User> findAllByUserTypeIn(List<UserType> userTypeList,Pageable pageable);
    List<User> findAllByDepartmentIn(List<Department> departmentList);
    Page<User> findAllByDepartmentIn(List<Department> departmentList,Pageable pageable);
    List<User> findAllByUserType(UserType userType);
    List<User> findAllByDepartment(Department department);

    //User findByUserName(String s);

    User findByUserMobile(Long aLong);

    User findByUserEmail(String s);

    Page<User> findByStatusTrueAndUserNameLikeOrStatusTrueAndUserEmailLike(String s, String s1, Pageable pageable);

}
