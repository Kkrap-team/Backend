package com.Kkrap.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileDefaultFolderResponse {
    private Long userId;

    private String email;

    private String nickname;

    private String profile;

    private Long KakaoId;

    private UserProfileDefaultFolderResponse(){ } // 외부에서 생성하지 못하게 함

    public static UserProfileDefaultFolderResponse of(Long userId, String email, String nickname, String profile, Long kakaoId){
        return new UserProfileDefaultFolderResponse(userId, email, nickname, profile, kakaoId);
    }
}
