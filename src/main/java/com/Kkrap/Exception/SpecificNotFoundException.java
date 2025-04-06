package com.Kkrap.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found 상태 코드 반환
public class SpecificNotFoundException extends RuntimeException {
    public SpecificNotFoundException(String message) {
        super(message);
    }
}
