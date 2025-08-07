package com.Kkrap.Service;

import com.Kkrap.Entity.RefreshToken;
import com.Kkrap.Exception.NotValidTokenException;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;

import com.Kkrap.ResponseDTO.TokenResponse;
import com.Kkrap.ResponseDTO.TokenUsersProfileResponse;
import com.Kkrap.Service.SocialLoginRefreshToken.ClientProvider;
import com.Kkrap.ResponseDTO.UsersProfileResponse;

import com.Kkrap.Service.SocialLoginRefreshToken.JwtUtil;
import com.Kkrap.Service.SocialLoginRefreshToken.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class AuthService {

    private final LoginUserPort loginUserHandler;
    private final ClientProvider clientProvider;

    private final JwtUtil jwtUtil;

    private final RefreshTokenService refreshTokenService;

    public AuthService(LoginUserHandler loginUserHandler,
                       ClientProvider clientProvider,
                       JwtUtil jwtUtil,
                       RefreshTokenService refreshTokenService
                       ) {
        this.loginUserHandler = loginUserHandler;
        this.clientProvider = clientProvider;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;

    }

    public TokenUsersProfileResponse prepare(KaKaoTokenRequest request, HttpServletResponse cookie) {
        String accessToken = request.getAccesstoken();

        if (!isAccessToken(accessToken)) {
            throw new NotValidTokenException("유효하지 않은 토큰입니다.");
        }

        // 1. 카카오 유저 정보 요청
        Map<String, Object> userInfo = clientProvider.getClient(accessToken);
        TokenUsersProfileResponse response = getUserProfile(userInfo, cookie);

        return response;
    }

    private boolean isAccessToken(String token) {

        if (token == null || token.isEmpty() || token.isBlank()) {
            return false;
        }
        
        if (token.length() < 10) {
            return false;
        }
        
        return true;
    }

    private TokenUsersProfileResponse getUserProfile(Map<String, Object> user, HttpServletResponse cookie) {
        Long kakaoId = Long.valueOf(user.get("id").toString());
        Map<String, Object> kakaoAccount = (Map<String, Object>) user.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String email = kakaoAccount.get("email").toString();
        String nickname = profile.get("nickname").toString();
        String profileImage = profile.get("profile_image_url").toString();

        TokenUsersProfileResponse response = loginUserHandler.validateUser(email, nickname, profileImage, kakaoId, cookie);
        return response;
    }

    public ResponseEntity<TokenResponse> refreshAccessToken(String refreshToken, HttpServletResponse response) {
        jwtUtil.validateToken(refreshToken);


        Long userId = jwtUtil.getUserIdFromToken(refreshToken);

        // DB에서 유효한 refreshToken인지 추가 검증 (선택적)
        refreshTokenService.validateStoredRefreshToken(userId, refreshToken);  // 유효성 검사 메서드 필요


        // 4. 새로운 refreshToken 생성 & DB 업데이트
        String newRefreshToken = jwtUtil.generateRefreshToken(userId);

        refreshTokenService.updateRefreshToken(userId, newRefreshToken);
        // 5. 새로운 accessToken 생성
        String newAccessToken = jwtUtil.generateAccessToken(userId);


        // 6. 응답 DTO로 감싸서 반환
        return ResponseEntity.ok(TokenResponse.of(newAccessToken, newRefreshToken));


    }

}
