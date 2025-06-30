package com.Kkrap.Exception;

import lombok.Getter;

@Getter
public class FoldersVisibleException extends RuntimeException{
    public FoldersVisibleException(String message) {
        super(message);
    }
    public static FoldersVisibleException of(String message) {
        return new FoldersVisibleException(message);
    }
}
