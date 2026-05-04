package io.backend.blogproject.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    NO_COMMENT("댓글 내용이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    NO_PARENT_COMMENT("대댓글을 작성할 댓글이 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
    NO_POST("댓글을 작성할 게시글이 없습니다.", HttpStatus.BAD_REQUEST),
    UNABLE_TO_FIND_COMMENT("해당 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CAN_NOT_CREATE_COMMENT("댓글을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CAN_NOT_DELETE_COMMENT("댓글을 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_DELETED("이미 삭제된 댓글입니다.", HttpStatus.BAD_REQUEST);
    public String message;
    public HttpStatusCode code;
    ErrorCode(String message, HttpStatus httpStatus){
        this.message = message;
        this.code = httpStatus;
    }
}
