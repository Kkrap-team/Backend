package com.kkrap.Exception;

import lombok.Getter;

@Getter
public class UsersNotFoundException extends RuntimeException {

    public UsersNotFoundException(String message) {
        super(message);
    }

    public static UsersNotFoundException from(String message) {
        return new UsersNotFoundException(message);
    }
}