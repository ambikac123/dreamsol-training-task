package com.dreamsol.repositories;

import com.dreamsol.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission,Long>
{

    Permission findByPermissionType(String permissionType);

    List<Permission> findAllByStatusTrue();

    Optional<Permission> findByPermissionIdAndStatusIsTrue(Long permissionId);
}
