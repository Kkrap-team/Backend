package com.Kkrap.ResponseDto;

import com.Kkrap.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

@Getter
@Setter
@AllArgsConstructor
public class UsersProfileResponse {
    private Long userId;

    private String email;

    private String nickname;

    private String profile;

    private Long KakaoId;

    private UsersProfileResponse(){ } // 외부에서 생성하지 못하게 함

    public static UsersProfileResponse of(Long userId, String email, String nickname, String profile, Long kakaoId){
        return new UsersProfileResponse(userId, email, nickname, profile, kakaoId);
    }

    public static UsersProfileResponse from(Users users){
        return new UsersProfileResponse(users.getUserId(), users.getEmail(), users.getNickname(), users.getProfile(), users.getKakaoId());
    }
}
