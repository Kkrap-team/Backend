package com.Kkrap.Exception;

import com.Kkrap.ResponseDto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //사용자 예외 처리
    @ExceptionHandler(UsersNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsersNotFound(UsersNotFoundException ex) {
        log.error("handleUsersNotFoundException", ex);

        ErrorResponse response =  ErrorResponse.from(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getCode()));
    }

    @ExceptionHandler(FoldersNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFoldersNotFound(FoldersNotFoundException ex){
        log.error("handleFoldersNotFoundException", ex);

        ErrorResponse response =  ErrorResponse.from(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getCode()));

    }

    @ExceptionHandler(NotValidTokenException.class)
    public ResponseEntity<ErrorResponse> notValidTokenException(NotValidTokenException ex) {
        ErrorResponse response = ErrorResponse.from(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
