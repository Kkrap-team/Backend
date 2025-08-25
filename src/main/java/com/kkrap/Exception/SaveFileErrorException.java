package com.kkrap.Exception;


import lombok.Getter;

@Getter
public class SaveFileErrorException extends RuntimeException{

    public SaveFileErrorException(String message) {
        super(message);
    }
    public static SaveFileErrorException of(String message) {
        return new SaveFileErrorException(message);
    }

}
