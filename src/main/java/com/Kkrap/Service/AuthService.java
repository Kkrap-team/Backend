package com.Kkrap.Service;

import com.Kkrap.Entity.Users;
import com.Kkrap.Exception.NotValidTokenException;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.FoldersCreateRequest;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.RequestDTO.UsersCreateRequest;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.ResponseDto.UsersProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    public static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UsersService usersService;

    @Autowired
    FoldersService foldersService;

    public boolean isAccessToken(String token) {
        if(token.equals("")){
            throw NotValidTokenException.from("토큰이 없습니다.");
        }
        return false;
    }

    private Map<String, Object> getKakaoUserInfo(String accessToken){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = new RestTemplate().exchange(
                    KAKAO_USER_INFO_URL,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw NotValidTokenException.from("유효하지 않은 카카오 액세스 토큰입니다.");
        }
    }

    public UsersProfileResponse kakaoLogin(@RequestBody KaKaoTokenRequest request){
        String accessToken = request.getAccesstoken();

        if (accessToken.isEmpty()){
            isAccessToken(accessToken);
        }

        //1. 카카오 유저 정보 요청
        Map<String, Object> userInfo = getKakaoUserInfo(accessToken);
        Long kakaoId = Long.valueOf(userInfo.get("id").toString());
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = kakaoAccount.get("email").toString();
        String nickname = profile.get("nickname").toString();
        String profileImage = profile.get("profile_image_url").toString();

        Optional<Users> CheckUser = usersRepository.findByKaKaoId(Long.valueOf(kakaoId));
        if (CheckUser.isEmpty()){
            // 처음 로그인 한 사람 사용자 만들기
            UsersCreateRequest usersCreateRequest = UsersCreateRequest.of(email, nickname, profileImage, kakaoId, null);
            Users newUser = usersService.save(usersCreateRequest);
            // 처음 로그인 한 사람은 모든 링크 보기 폴더가 없음 만들어주어야함
            FoldersCreateRequest foldersCreateRequest = FoldersCreateRequest.of("모든 링크", "모든 링크가 저장된 폴더입니다.", false);
            foldersService.save(foldersCreateRequest, newUser);
            UsersProfileResponse usersProfileResponse = UsersProfileResponse.of(newUser.getUserId(), newUser.getEmail(), newUser.getNickname(), newUser.getProfile(), newUser.getKakaoId(), newUser.getBio());
            return usersProfileResponse;
        }
        else
        {
            Users existingUser = CheckUser.get();
            UsersProfileResponse usersProfileResponse = UsersProfileResponse.of(existingUser.getUserId(), existingUser.getEmail(), existingUser.getNickname(), existingUser.getProfile(), existingUser.getKakaoId(), existingUser.getBio());
            return usersProfileResponse;
        }
    }


}
