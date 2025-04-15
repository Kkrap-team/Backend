package com.Kkrap.Exception;

import com.nimbusds.oauth2.sdk.ErrorResponse;
import lombok.Getter;

@Getter
public class UsersNotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    public UsersNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}