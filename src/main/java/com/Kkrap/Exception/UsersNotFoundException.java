package com.Kkrap.Exception;

import lombok.Getter;

@Getter
public class UsersNotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    public UsersNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public static UsersNotFoundException of(String message, ErrorCode errorCode) {
        return new UsersNotFoundException(message, errorCode);
    }
}