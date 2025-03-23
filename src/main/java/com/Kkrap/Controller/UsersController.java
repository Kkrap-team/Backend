package com.Kkrap.Controller;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.Users;
import com.Kkrap.Repository.FoldersRepository;
import com.Kkrap.Repository.UsersRepository;
import com.Kkrap.RequestDTO.KaKaoTokenRequest;
import com.Kkrap.RequestDTO.NicknameRequest;
import com.Kkrap.ResponseDto.MessageResponse;
import com.Kkrap.ResponseDto.UserProfileResponse;
import com.Kkrap.Service.UsersService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/v2/users")
public class UsersController {
    @Autowired
    UsersService usersService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    FoldersRepository foldersRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    //users 프로필 조회
    @GetMapping("/{userId}")
    @ResponseBody
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable("userId") Long userId)
    {
        UserProfileResponse userProfile = usersService.getUserProfile(userId);
        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    //닉네임 변경
    @PostMapping("/{userId}/nickname")
    @ResponseBody
    public ResponseEntity<MessageResponse> updateNickname(@PathVariable("userId") Long userId, @RequestBody NicknameRequest request)
    {

        boolean updated = usersService.updateNickName(userId, request.getNickname());
        if (updated) {
            MessageResponse message = new MessageResponse(404,"닉네임 변경 완료");
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(404,"해당 ID의 사용자를 찾을 수 없습니다."));
        }
    }

    //토큰 관리 방식
    @PostMapping("/api/auth/kakao-login")
    public ResponseEntity<?> kakaoLogin(@RequestBody KaKaoTokenRequest request){
        String accessToken = request.getAccesstoken();



        //1. 카카오 유저 정보 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = new RestTemplate().exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                Map.class
        );


        Map<String, Object> userInfo = response.getBody();
        Long kakaoId = Long.valueOf(userInfo.get("id").toString());
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = kakaoAccount.get("email").toString();
        String nickname = profile.get("nickname").toString();
        String profileImage = profile.get("profile_image_url").toString();

//        Users user = usersRepository.findByKaKaoId(kakaoId).orElseGet(() -> {
//            Users newUser = new Users(email, nickname, profileImage, kakaoId);
//            usersRepository.save(newUser);
//            return newUser;
//        });
        String userId;
        Optional<Users> CheckUser = usersRepository.findByKaKaoId(Long.valueOf(kakaoId));
        System.out.println("CheckUser : " + CheckUser);
        if (CheckUser.isEmpty()){
            Users newUser = new Users(email, nickname, profileImage, Long.valueOf(kakaoId));
            usersRepository.save(newUser);

            // 처음 로그인 한 사람은 모든 링크 보기 폴더가 없음 만들어주어야함
            Folders folder = new Folders(newUser, "모든 링크", "모든 링크가 저장된 폴더입니다.", false);
            foldersRepository.save(folder);

            UserProfileResponse userProfileResponse = new UserProfileResponse(newUser.getUserId(), newUser.getEmail(), newUser.getNickname(), newUser.getProfile(), newUser.getKakaoId());
            return ResponseEntity.ok(userProfileResponse);
        }
        else
        {
            Users existingUser = CheckUser.get();
            UserProfileResponse userProfileResponse = new UserProfileResponse(existingUser.getUserId(), existingUser.getEmail(), existingUser.getNickname(), existingUser.getProfile(), existingUser.getKakaoId());
            return ResponseEntity.ok(userProfileResponse);
        }


//        return ResponseEntity.ok(Map.of(
//                "userId", user.getUserId(),
//                "nickname", user.getNickname(),
//                "email", user.getEmail(),
//                "profile", user.getProfile()
//        ));
    }

}
