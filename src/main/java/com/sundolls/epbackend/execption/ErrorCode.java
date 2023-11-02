package com.sundolls.epbackend.execption;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //401 UNAUTHORIZED
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "로그인에 실패하였습니다."),

    //403 FORBIDDEN
    QUESTION_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 질문의 작성자가 아닙니다."),
    ANSWER_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 답변의 작성자가 아닙니다."),
    FRIEND_FORBIDDEN(HttpStatus.FORBIDDEN, "잘못된 수락 요청입니다"),


    //404 NOT FOUND
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 질문이 존재하지 않습니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답변이 존재하지 않습니다."),
    CALENDAR_NOT_FOUND(HttpStatus.NOT_FOUND, "오늘의 캘린더가 존재하지 않습니다.\n캘린더 쓰기 기능을 이용해 주세요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 친구가 존재하지 않습니다."),
    STUDY_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "범위내의 학습기록이 존재하지 않습니다."),


    //409 CONFLICT
    CALENDAR_CONFLICT(HttpStatus.CONFLICT, "오늘의 캘린더가 이미 존재합니다.\n캘린더 수정기능을 이용해 주세요."),


    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류 발생.")
    ;




    private final HttpStatus code;
    private final String message;

}
