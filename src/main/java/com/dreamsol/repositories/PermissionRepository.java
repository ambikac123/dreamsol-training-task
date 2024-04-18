package com.dreamsol.repositories;

import com.dreamsol.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission,Long>
{

    Permission findByPermissionType(String permissionType);

    List<Permission> findAllByStatusTrue();
}
