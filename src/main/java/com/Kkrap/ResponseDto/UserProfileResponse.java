package com.Kkrap.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long userId;

    private String email;

    private String nickname;

    private String profile;

    private Long KakaoId;


//    public UserProfileResponse(Long userId, String email, String nickname, String profile) {
//    }
}
