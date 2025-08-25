package com.kkrap.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnauthorizedException extends RuntimeException  {
    public UnauthorizedException(String message){
        super(message);
    }

    public static UnauthorizedException of(String message){
        return new UnauthorizedException(message);
    }
}