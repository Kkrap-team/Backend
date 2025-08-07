package com.Kkrap.ResponseDTO;

import com.Kkrap.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsersProfileResponse {
    private Long userId;

    private String email;

    private String nickname;

    private String profile;

    private String bio;

    private UsersProfileResponse(){ } // 외부에서 생성하지 못하게 함

    public static UsersProfileResponse of(Long userId, String email, String nickname, String profile, String bio){
        return new UsersProfileResponse(userId, email, nickname, profile, bio);
    }

    public static UsersProfileResponse from(Users users){
        return new UsersProfileResponse(users.getUserId(), users.getEmail(), users.getNickname(), users.getProfile(), users.getBio());
    }
}
