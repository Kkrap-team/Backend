package com.kkrap.Service;

import com.kkrap.ResponseDTO.TokenUsersProfileResponse;

public interface LoginUserPort {
//    UsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId);
    TokenUsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId);
//    TokenUsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId, HttpServletResponse response);


}
