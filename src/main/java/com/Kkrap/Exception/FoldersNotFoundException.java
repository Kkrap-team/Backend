package com.Kkrap.Exception;

import lombok.Getter;

@Getter
public class FoldersNotFoundException extends RuntimeException {

    public FoldersNotFoundException(String message) {
        super(message);
    }
    public static FoldersNotFoundException of(String message) {
        return new FoldersNotFoundException(message);
    }

}
