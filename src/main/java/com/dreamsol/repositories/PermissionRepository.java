package com.dreamsol.repositories;

import com.dreamsol.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long>
{

    Permission findByPermissionType(String permissionType);
}
