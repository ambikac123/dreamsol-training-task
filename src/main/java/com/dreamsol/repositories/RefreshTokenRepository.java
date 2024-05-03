package com.dreamsol.repositories;

import com.dreamsol.entities.RefreshToken;
import com.dreamsol.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>
{
    RefreshToken findByRefreshToken(String refreshToken);

    RefreshToken findByUser(User user);
    // custom methods
}
