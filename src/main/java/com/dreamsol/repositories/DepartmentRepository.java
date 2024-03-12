package com.dreamsol.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dreamsol.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department,Long>
{

	@Query("select d from Department d where d.departmentName like :key")
	List<Department> findByName(@Param("key") String keyword);
	
	@Query("select d from Department d where d.departmentCode like :key")
	List<Department> findByCode(@Param("key") String keywords);
	
}
