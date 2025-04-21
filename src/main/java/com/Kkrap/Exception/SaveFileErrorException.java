package com.Kkrap.Exception;


import lombok.Getter;

@Getter
public class SaveFileErrorException extends RuntimeException{
    private ErrorCode errorCode;

    public SaveFileErrorException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public static SaveFileErrorException of(String message, ErrorCode errorCode) {
        return new SaveFileErrorException(message, errorCode);
    }

}
