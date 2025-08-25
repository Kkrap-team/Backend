package com.kkrap.Exception;

import lombok.Getter;

@Getter
public class LinksNotFoundException extends RuntimeException{

    public LinksNotFoundException(String message){
        super(message);
    }

    public static FoldersNotFoundException of(String message){
        return new FoldersNotFoundException(message);
    }

}
