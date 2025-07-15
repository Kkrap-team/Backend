package com.Kkrap.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowNotFoundException extends RuntimeException {
    public FollowNotFoundException(String message){
        super(message);
    }

    public static FollowNotFoundException from(String message){
        return new FollowNotFoundException(message);
    }
}

