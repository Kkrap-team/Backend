package com.Kkrap.Exception;

import lombok.Getter;

@Getter
public class FoldersNotFoundException extends RuntimeException {
    private ErrorCode errorCode;

    public FoldersNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    public static FoldersNotFoundException of(String message, ErrorCode errorCode) {
        return new FoldersNotFoundException(message, errorCode);
    }

}
