package com.dreamsol.repositories;

import com.dreamsol.entities.CurrentUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentUserRepository extends JpaRepository<CurrentUser,Long>
{
    CurrentUser findByUsername(String username);
}
