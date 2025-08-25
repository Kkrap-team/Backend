package com.kkrap.Exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FoldersPermissionNotFoundException extends RuntimeException {
    public FoldersPermissionNotFoundException(String message){
        super(message);
    }

    public static FoldersPermissionNotFoundException from(String message){
        return new FoldersPermissionNotFoundException(message);
    }
}
