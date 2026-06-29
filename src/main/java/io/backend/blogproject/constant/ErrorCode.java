package io.backend.blogproject.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    MEMBER_NOT_FOUND("해당 계정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AUTHENTICATION_INCORRECT("로그인 정보가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    USER_ID_ALREADY_EXISTS("이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_ID("아이디는 영문, 숫자, 밑줄, 하이픈으로 4~30자여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_NAME("이름은 1~10자여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_USER_PASSWORD("비밀번호는 8~72자여야 합니다.", HttpStatus.BAD_REQUEST),
    NO_COMMENT("댓글 내용이 비어 있습니다.", HttpStatus.BAD_REQUEST),
    NO_PARENT_COMMENT("부모 댓글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    NO_POST("댓글을 작성할 게시글이 없습니다.", HttpStatus.BAD_REQUEST),
    UNABLE_TO_FIND_COMMENT("해당 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CAN_NOT_CREATE_COMMENT("댓글을 생성할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CAN_NOT_DELETE_COMMENT("댓글을 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_DELETED("이미 삭제된 댓글입니다.", HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_ACCESS_DENIED("게시글 작성자만 수정하거나 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    COMMENT_ACCESS_DENIED("댓글 작성자만 수정하거나 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    COMMENT_POST_MISMATCH("해당 게시글에 작성된 댓글이 아닙니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN("만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    ABNORMAL_TOKEN("정상적이지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    ERROR_FROM_TOKEN("토큰 처리 중 문제가 발생했습니다.", HttpStatus.UNAUTHORIZED);

    public final String message;
    public final HttpStatusCode code;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.code = httpStatus;
    }
}
