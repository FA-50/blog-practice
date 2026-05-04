package io.backend.blogproject.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    NO_COMMENT("댓글 내용이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    ALEADY_DELETED("이미 삭제된 내용입니다.", HttpStatus.BAD_REQUEST);
    public String message;
    public HttpStatusCode code;
    ErrorCode(String message, HttpStatus httpStatus){
        this.message = message;
        this.code = httpStatus;
    }
}
