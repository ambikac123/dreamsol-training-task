package com.dreamsol.repositories;

import com.dreamsol.entities.LoginUser;
import com.dreamsol.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>
{
    RefreshToken findByRefreshToken(String refreshToken);

    RefreshToken findByLoginUser(LoginUser loginUser);
    // custom methods
}
