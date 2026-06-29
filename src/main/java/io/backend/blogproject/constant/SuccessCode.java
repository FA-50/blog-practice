package io.backend.blogproject.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    // 유저
    LOGIN_SUCCESS(HttpStatus.OK,"로그인이 성공되었습니다."),
    USER_CREATE_SUCCESS(HttpStatus.CREATED, "회원가입이 성공되었습니다."),

    // 웨이포인트
    WAY_POINT_CREATE_SUCCESS(HttpStatus.CREATED, "체크포인트가 생성되었습니다."),
    WAY_POINT_PASSED_SUCCESS(HttpStatus.OK, "체크포인트가 통과되었습니다."),
    WAY_POINT_RETRIEVE_SUCCESS(HttpStatus.OK, "체크포인트가 조회되었습니다."),

    // 주문
    ORDER_CREATE_SUCCESS(HttpStatus.CREATED, "주문이 생성되었습니다."),
    ORDER_RETRIEVE_SUCCESS(HttpStatus.OK, "주문이 조회되었습니다."),

    // 비행
    FLIGHT_START(HttpStatus.CREATED, "비행이 시작되었습니다."),
    FLIGHT_RECORD(HttpStatus.OK, "비행이 기록되었습니다."),
    FLIGHT_REPLAY_RETRIEVE(HttpStatus.OK, "비행 리플레이가 조회되었습니다."),
    FLIGHT_FINISHED(HttpStatus.OK, "비행이 종료되었습니다."),

    // 랭킹
    RANKING_RETRIEVE(HttpStatus.OK, "랭킹이 조회되었습니다."),
    ;


    private final HttpStatus status;
    private final String description;
}
