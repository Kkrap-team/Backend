package com.kkrap.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    private String message;


    private ErrorResponse(){}

    public static ErrorResponse from(int code, String customMessage) {
        return new ErrorResponse(code, customMessage);
    }

}
