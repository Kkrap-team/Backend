package com.Kkrap.ResponseDto;

import com.Kkrap.Exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    private String errorCode;
    private String message;


    private ErrorResponse(){}

    public static ErrorResponse from(ErrorCode errorCode){
        return new ErrorResponse(errorCode.getCode(), errorCode.getErrorCode(), errorCode.getMessage());
    }

    public static ErrorResponse from(ErrorCode errorCode, String customMessage) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getErrorCode(), customMessage);
    }

}
