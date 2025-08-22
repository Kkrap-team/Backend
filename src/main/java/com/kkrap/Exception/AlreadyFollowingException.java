package com.kkrap.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlreadyFollowingException extends RuntimeException  {
    public AlreadyFollowingException(String message){
        super(message);
    }

    public static AlreadyFollowingException from(String message){
        return new AlreadyFollowingException(message);
    }
}
