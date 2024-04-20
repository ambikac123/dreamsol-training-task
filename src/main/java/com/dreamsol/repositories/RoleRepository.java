package com.dreamsol.repositories;

import com.dreamsol.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long>
{
    Role findByRoleType(String roleName);

    List<Role> findAllByStatusTrue();

    Optional<Role> findByRoleIdAndStatusIsTrue(Long roleId);
}
