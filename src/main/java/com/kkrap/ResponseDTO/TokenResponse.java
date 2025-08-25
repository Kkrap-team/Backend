package com.kkrap.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    private TokenResponse() {}

    public static TokenResponse of(String accessToken, String refreshToken){
        return new TokenResponse(accessToken, refreshToken);
    }


}
