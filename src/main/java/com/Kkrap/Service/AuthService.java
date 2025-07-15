package com.Kkrap.Service;

import com.Kkrap.Exception.NotValidTokenException;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import com.Kkrap.Service.SocialLogin.ClientProvider;
import com.Kkrap.ResponseDTO.UsersProfileResponse;
import com.Kkrap.Service.SocialLogin.RestTemplateKakaoProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final LoginUserPort loginUserHandler;
    private final ClientProvider clientProvider;

    public AuthService(LoginUserHandler loginUserHandler, ClientProvider clientProvider) {
        this.loginUserHandler = loginUserHandler;
        this.clientProvider = clientProvider;
    }

    public UsersProfileResponse prepare(KaKaoTokenRequest request) {
        String accessToken = request.getAccesstoken();

        if (!isAccessToken(accessToken)) {
            throw new NotValidTokenException("유효하지 않은 토큰입니다.");
        }

        // 1. 카카오 유저 정보 요청
        Map<String, Object> userInfo = clientProvider.getClient(accessToken);
        UsersProfileResponse response = getUserProfile(userInfo);
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

    private UsersProfileResponse getUserProfile(Map<String, Object> user) {
        Long kakaoId = Long.valueOf(user.get("id").toString());
        Map<String, Object> kakaoAccount = (Map<String, Object>) user.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String email = kakaoAccount.get("email").toString();
        String nickname = profile.get("nickname").toString();
        String profileImage = profile.get("profile_image_url").toString();

        UsersProfileResponse response = loginUserHandler.validateUser(email, nickname, profileImage, kakaoId);
        return response;
    }


}
