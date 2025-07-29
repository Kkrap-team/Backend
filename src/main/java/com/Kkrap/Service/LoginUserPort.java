package com.Kkrap.Service;

import com.Kkrap.ResponseDto.UsersProfileResponse;

public interface LoginUserPort {
    UsersProfileResponse validateUser(String email, String nickname, String profileImage, Long kakaoId);
}
