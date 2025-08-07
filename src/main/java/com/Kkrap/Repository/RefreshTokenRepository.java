package com.Kkrap.Repository;

import com.Kkrap.Entity.RefreshToken;
import com.Kkrap.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByUserUserId(Long userId);  // 사용자 ID 기반 조회

    void deleteByUser(Users users);
}