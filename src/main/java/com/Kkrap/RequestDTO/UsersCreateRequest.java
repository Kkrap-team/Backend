package com.Kkrap.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsersCreateRequest {
    private String email;
    private String nickname;
    private String profileImage;
    private Long kakaoId;


    public static UsersCreateRequest of(String email, String nickname, String profileImage, Long kakaoId){
        return new UsersCreateRequest(email, nickname, profileImage, kakaoId);
    }
}
