package com.Kkrap.Handler;

import com.Kkrap.Exception.SpecificNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MasterExceptionHandler {
    @ExceptionHandler(SpecificNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(SpecificNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(exception.getMessage()));
    }
}
