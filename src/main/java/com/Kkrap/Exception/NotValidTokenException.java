package com.Kkrap.Exception;

import lombok.Getter;

@Getter
public class NotValidTokenException extends RuntimeException{

    private ErrorCode errorCode;

    public NotValidTokenException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
