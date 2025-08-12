package com.Kkrap.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenUsersProfileResponse {
    private TokenResponse token;
    private UsersProfileUserIdNickName profile;

    private TokenUsersProfileResponse() {}
    public static TokenUsersProfileResponse of(TokenResponse token, UsersProfileUserIdNickName profile){
        return new TokenUsersProfileResponse(token, profile);
    }
}