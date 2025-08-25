package com.kkrap.Service.SocialLoginRefreshToken;


import com.kkrap.Entity.RefreshToken;
import com.kkrap.Entity.Users;
import com.kkrap.Exception.UnauthorizedException;
import com.kkrap.Repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository)
    {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void save(RefreshToken token){
        refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public void deleteByUser(Users users) {
        refreshTokenRepository.deleteByUser(users);
    }
    public void validateStoredRefreshToken(Long userId, String refreshToken) {
        RefreshToken stored = refreshTokenRepository.findByUserUserId(userId)
                .orElseThrow(() -> UnauthorizedException.of("해당 유저의 refresh token이 존재하지 않습니다."));

        if (!stored.getRefreshToken().equals(refreshToken)) {
            throw UnauthorizedException.of("Refresh token이 일치하지 않습니다.");
        }
    }

    public Optional<RefreshToken> findByUserId(Long userId){
        return refreshTokenRepository.findByUserUserId(userId);
    }


    public void updateRefreshToken(Long userId, String newToken) {
        RefreshToken stored = refreshTokenRepository.findByUserUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("refresh token not found"));

        stored.setRefreshToken(newToken);
        refreshTokenRepository.save(stored);
    }
}
