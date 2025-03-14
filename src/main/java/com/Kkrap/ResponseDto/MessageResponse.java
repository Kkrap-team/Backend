package com.Kkrap.ResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    private Integer code;
    private String message;

    public MessageResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
