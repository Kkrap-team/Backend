package com.kkrap.Service;

import com.kkrap.ResponseDTO.TokenUsersProfileResponse;

public interface LoginUserPort {
    TokenUsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId);

}
