package com.kkrap.Service;

import com.kkrap.Entity.RefreshToken;
import com.kkrap.Entity.Users;
import com.kkrap.RequestDTO.FoldersCreateRequest;
import com.kkrap.RequestDTO.UsersCreateRequest;
import com.kkrap.ResponseDTO.TokenResponse;
import com.kkrap.ResponseDTO.TokenUsersProfileResponse;
import com.kkrap.ResponseDTO.UsersProfileUserIdNickName;
import com.kkrap.Service.FolderLink.FoldersService;
import com.kkrap.Service.SocialLoginRefreshToken.JwtUtil;
import com.kkrap.Service.SocialLoginRefreshToken.RefreshTokenService;
import com.kkrap.Service.Users.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
                    "기본 폴더", "기본 폴더입니다.", false
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
