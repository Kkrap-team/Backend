package com.Kkrap.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenUsersProfileResponse {
    private TokenResponse token;
    private UsersProfileResponse profile;

    private TokenUsersProfileResponse() {}
    public static TokenUsersProfileResponse of(TokenResponse token, UsersProfileResponse profile){
        return new TokenUsersProfileResponse(token, profile);
    }
}