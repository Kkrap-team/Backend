package com.kkrap.Exception;

public class FoldersSameMoveException extends RuntimeException{
    public FoldersSameMoveException(String message) {
        super(message);
    }
    public static FoldersSameMoveException of(String message) {
        return new FoldersSameMoveException(message);
    }
}
