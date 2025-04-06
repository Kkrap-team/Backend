package com.Kkrap.Handler;

import com.Kkrap.Exception.SpecificNotFoundException;

import java.util.Optional;

public class ObjectStatusHandler {

    public static void objectStatusHandler(Optional<Object> object, String message) {
        if (object.isEmpty()){
            throw new SpecificNotFoundException(message);
        }
    }

}
