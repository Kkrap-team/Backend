package com.Kkrap.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(404,"USER-ERR-404","사용자를 찾을 수 없습니다");

    private int code;
    private String errorCode;
    private String message;
}
