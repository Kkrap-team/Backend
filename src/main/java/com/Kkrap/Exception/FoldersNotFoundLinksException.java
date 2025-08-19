package com.Kkrap.Exception;

public class FoldersNotFoundLinksException extends RuntimeException{
    public FoldersNotFoundLinksException(String message) {
        super(message);
    }
    public static FoldersNotFoundLinksException of(String message) {
        return new FoldersNotFoundLinksException(message);
    }

}
