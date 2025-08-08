package com.Kkrap.Service.SocialLoginRefreshToken;

import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.UnauthorizedException;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private Key key;
    private final long accessTokenValidity = 1000 * 60 * 1; // 15분
//    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일
//    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일
    private final long refreshTokenValidity = 1000L * 60 * 1;

    public JwtUtil(@Value("${jwt.secret}") String secretKeyRaw) {
        this.key = Keys.hmacShaKeyFor(secretKeyRaw.getBytes());
    }

    public String generateAccessToken(Long userId) {
        return generateToken(userId, accessTokenValidity);
    }

    public String generateRefreshToken(Long userId) {
        return generateToken(userId, refreshTokenValidity);
    }

    private String generateToken(Long userId, long validityInMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateToken(String token) {
        try {
            log.info("token {}", token);
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw UnauthorizedException.of("유효하지 않은 refresh token입니다.!");
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public Date getExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }
}
