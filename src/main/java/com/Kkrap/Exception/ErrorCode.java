package com.Kkrap.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
//    USER_NOT_FOUND(404,"USERS-ERR-404","사용자를 찾을 수 없습니다."),
//    FOLDERS_NOT_FOUND(404, "FOLDERS-ERR-404", "존재하지 않는 폴더입니다.");

    USER_NOT_FOUND(404,"USERS-ERR-404","USERS NOT FOUND"),
    FOLDERS_NOT_FOUND(404, "FOLDERS-ERR-404", "FOLDERS NOT FOUND"),
    LINKS_NOT_FOUND(404, "LINKS-ERR-404", "LINKS NOT FOUND"),
    TOKEN_NOT_VALID(404, "TOKEN_NOT_VALID", "TOKEN NOT VALID"),
    DUPLICATE_NICKNAME(409, "DUPLICATE_NICKNAME", "DUPLICATE NICKNAME"),
    SAVE_FILE_ERROR(500, "DUPLICATE_NICKNAME", "DUPLICATE NICKNAME")
    ;

    private int code;
    private String errorCode;
    private String message;
}
