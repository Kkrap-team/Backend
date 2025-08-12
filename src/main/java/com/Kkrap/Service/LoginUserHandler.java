package com.Kkrap.Service;

import com.Kkrap.Entity.RefreshToken;
import com.Kkrap.Entity.Users;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.ResponseDTO.TokenResponse;
import com.Kkrap.ResponseDTO.TokenUsersProfileResponse;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.ResponseDTO.UsersProfileUserIdNickName;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.SocialLoginRefreshToken.JwtUtil;
import com.Kkrap.Service.SocialLoginRefreshToken.RefreshTokenService;
import com.Kkrap.Service.Users.UsersService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
 public class LoginUserHandler implements LoginUserPort {
    private final UsersService usersService;
    private final FoldersService foldersService;

    private final JwtUtil jwtUtil;

    private final RefreshTokenService refreshTokenService;

    public LoginUserHandler(UsersService usersService,
                            FoldersService foldersService,
                            JwtUtil jwtUtil,
                            RefreshTokenService refreshTokenService) {
        this.usersService = usersService;
        this.foldersService = foldersService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public TokenUsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId) {
        Optional<Users> checkUser = usersService.findByKakaoId(Long.valueOf(kakaoId));
        Users user;

        if (checkUser.isEmpty()) {
            // 1. 유저 생성
            UsersCreateRequest usersCreateRequest = UsersCreateRequest.of(email, nickname, profileImage, kakaoId, null);
            user = usersService.save(usersCreateRequest);

            // 2. 기본 폴더 생성
            FoldersCreateRequest foldersCreateRequest = FoldersCreateRequest.of(
                    "모든 링크", "모든 링크가 저장된 폴더입니다.", false, true
            );
            foldersService.save(foldersCreateRequest, user);
        } else {
            user = checkUser.get();
        }

        UsersProfileUserIdNickName profile = UsersProfileUserIdNickName.of(
                user.getUserId(), user.getNickname(), user.getProfile()
        );
        String accessToken = jwtUtil.generateAccessToken(user.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        Optional<RefreshToken> existing = refreshTokenService.findByUserId(user.getUserId());
        if (existing.isPresent()) {
            refreshTokenService.updateRefreshToken(user.getUserId(), refreshToken);  // 업데이트만
        } else {
            RefreshToken token = RefreshToken.of(refreshToken, user);  // 새로 생성
            refreshTokenService.save(token);
        }

        log.info("accesstoken : {}", accessToken);
        log.info("refreshtoken : {}", refreshToken);

        return TokenUsersProfileResponse.of(
                TokenResponse.of(accessToken, refreshToken),
                profile
        );
    }
}
