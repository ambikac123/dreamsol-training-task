package com.dreamsol.repositories;

import com.dreamsol.entities.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginUserRepository extends JpaRepository<LoginUser,Long>
{
    LoginUser findByUsername(String username);
    LoginUser findByIpAddress(String hostAddress);
}
