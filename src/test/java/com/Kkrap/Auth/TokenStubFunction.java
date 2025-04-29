package com.Kkrap.Auth;

import com.Kkrap.Exception.NotValidTokenException;

public class TokenStubFunction {
    public static boolean isAccessToken(String token) {
        if(token == null){
            throw new NotValidTokenException("토큰이 없습니다.");
        }

        if(token.equals("")){
            throw new NotValidTokenException("토큰이 없습니다.");
        }

        return false;
    }
}
