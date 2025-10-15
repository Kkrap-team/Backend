package com.kkrap.Repository;

import com.kkrap.Entity.RefreshToken;
import com.kkrap.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByUserUserId(Long userId);

    void deleteByUser(Users users);
}