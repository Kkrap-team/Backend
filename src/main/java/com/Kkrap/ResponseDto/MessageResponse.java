package com.Kkrap.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private Integer code;
    private String message;
    private MessageResponse() { }

    public static MessageResponse of(Integer code, String message){
        return new MessageResponse(code, message);
    }
}
