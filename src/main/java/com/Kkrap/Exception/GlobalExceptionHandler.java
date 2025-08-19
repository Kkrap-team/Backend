package com.Kkrap.Exception;

import com.Kkrap.ResponseDTO.ErrorResponse;
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
        ErrorResponse response = ErrorResponse.from(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FoldersNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFoldersNotFound(FoldersNotFoundException ex){
        log.error("handleFoldersNotFoundException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotValidTokenException.class)
    public ResponseEntity<ErrorResponse> notValidTokenException(NotValidTokenException ex) {
        log.error("notValidTokenException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LinksNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLinksNotFound(LinksNotFoundException ex){
        log.error("handleLinksNotFoundException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateNickNameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNickName(DuplicateNickNameException ex){
        log.error("handleDuplicateNickName", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SaveFileErrorException.class)
    public ResponseEntity<ErrorResponse> handleSaveFileErrorException(SaveFileErrorException ex){
        log.error("handleSaveFileErrorException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FollowNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFollowNotFoundException(FollowNotFoundException ex){
        log.error("handleFollowNotFoundException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyFollowingException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyFollowingException(AlreadyFollowingException ex){
        log.error("handleAlreadyFollowingException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FoldersPermissionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFoldersPermissionNotFoundException(FoldersPermissionNotFoundException ex){
        log.error("handleFoldersPermissionNotFoundException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FoldersVisibleException.class)
    public ResponseEntity<ErrorResponse> handleFoldersVisibleException(FoldersVisibleException ex){
        log.error("handleFoldersVisibleException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex){
        log.error("handleUnauthorizedException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FoldersNotFoundLinksException.class)
    public ResponseEntity<ErrorResponse> handleFoldersNotFoundLinksException(FoldersNotFoundLinksException ex){
        log.error("handleFoldersNotFoundLinksException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(FoldersSameMoveException.class)
    public ResponseEntity<ErrorResponse> handleFoldersSameMoveException(FoldersSameMoveException ex){
        log.error("handleFoldersSameMoveException", ex);
        ErrorResponse response = ErrorResponse.from(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
