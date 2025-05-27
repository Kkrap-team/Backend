package com.Kkrap.Service;

import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.NotValidTokenException;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import com.Kkrap.Service.FolderLink.FoldersService;
import com.Kkrap.Service.SocialLogin.RestTemplateKakaoProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final LoginUserHandler loginUserHandler;

    private final RestTemplateKakaoProvider restTemplateKakaoProvider;

    public AuthService(LoginUserHandler loginUserHandler) {
        this.loginUserHandler = loginUserHandler;
        this.restTemplateKakaoProvider = new RestTemplateKakaoProvider();
    }

    public UsersProfileResponse prepare(@RequestBody KaKaoTokenRequest request) {
        String accessToken = request.getAccesstoken();

        if (accessToken.isEmpty()){
            isAccessToken(accessToken);
        }

        //1. 카카오 유저 정보 요청
        Map<String, Object> userInfo = restTemplateKakaoProvider.getClient(accessToken);

        UsersProfileResponse response = getUserProfile(userInfo);

        return response;
    }

    private boolean isAccessToken(String token) {
        if(token == null){
            throw new NotValidTokenException("토큰이 없습니다.");
        }

        if(token.equals("")){
            throw new NotValidTokenException("토큰이 없습니다.");
        }

        return false;
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
