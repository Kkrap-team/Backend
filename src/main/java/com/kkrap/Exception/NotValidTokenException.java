package com.kkrap.Exception;

import lombok.Getter;

@Getter
public class NotValidTokenException extends RuntimeException{

    public NotValidTokenException(String message) {
        super(message);
    }

    private NotValidTokenException() {}

    public static NotValidTokenException from(String message){
        return new NotValidTokenException(message);
    }

}
