package com.Kkrap.Exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DuplicateNickNameException extends RuntimeException{

    public DuplicateNickNameException(String message){
        super(message);
    }

    public static DuplicateNickNameException from(String message){
        return new DuplicateNickNameException(message);
    }
}
