package com.dreamsol.repositories;

import com.dreamsol.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long>
{
    Role findByRoleName(String roleName);

    List<Role> findAllByStatusTrue();
}
