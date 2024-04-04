package com.dreamsol.repositories;

import java.util.List;

import com.dreamsol.entities.Department;
import com.dreamsol.entities.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamsol.entities.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUserMobileAndUserEmail(long userMobile, String userEmail);

    @Query(value = "SELECT * FROM user WHERE user_mobile LIKE %:mobileNo%", nativeQuery = true)
    List<User> findByUserMobileLike(String mobileNo);

    @Query("SELECT u FROM User u WHERE u.userMobile LIKE %:mobileNo% ORDER BY u.userId")
    Page<User> findByUserMobileLikeOrderByUserId(String mobileNo, Pageable pageable);

    List<User> findByUserNameLikeOrUserEmailLike(String userName, String userEmail);
    Page<User> findByUserNameLikeOrUserEmailLike(String userName, String userEmail, Pageable pageable);
    List<User> findAllByUserTypeIn(List<UserType> userTypeList);
    Page<User> findAllByUserTypeIn(List<UserType> userTypeList,Pageable pageable);
    List<User> findAllByDepartmentIn(List<Department> departmentList);
    Page<User> findAllByDepartmentIn(List<Department> departmentList,Pageable pageable);
    List<User> findAllByUserType(UserType userType);
    List<User> findAllByDepartment(Department department);
}
