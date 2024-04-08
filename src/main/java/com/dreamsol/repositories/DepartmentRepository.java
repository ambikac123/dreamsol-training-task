package com.dreamsol.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamsol.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department,Long>
{
	Department findByDepartmentNameAndDepartmentCode(String departmentName, String departmentCode);
	List<Department> findByDepartmentNameLikeOrDepartmentCodeLike(String departmentName, String departmentCode);
	//Page<Department> findByDepartmentNameLikeOrDepartmentCodeLike(String departmentName, String departmentCode, Pageable pageable);

    Department findByDepartmentName(String departmentName);

	Department findByDepartmentCode(String code);

    Page<Department> findByStatusTrueAndDepartmentNameLikeOrStatusTrueAndDepartmentCodeLike(String s, String s1, Pageable pageable);
	List<Department> findByStatusTrueAndDepartmentNameLikeOrStatusTrueAndDepartmentCodeLike(String s, String s1);

	List<Department> findAllByStatusTrue();
}
