package com.Kkrap.Exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DuplicateNickNameException extends RuntimeException{
    private ErrorCode errorCode;

    public DuplicateNickNameException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public static DuplicateNickNameException of(String message, ErrorCode errorCode){
        return new DuplicateNickNameException(message, errorCode);
    }
}
