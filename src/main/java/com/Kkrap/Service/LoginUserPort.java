package com.Kkrap.Service;

import com.Kkrap.ResponseDTO.TokenUsersProfileResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginUserPort {
//    UsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId);
//    TokenUsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId);
    TokenUsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId, HttpServletResponse response);


}
