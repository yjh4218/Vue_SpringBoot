package com.vue_spring.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    EMAIL_DUPLICATION(400,"MEMBER-ERR-400","EMAIL DUPLICATED"),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "COMMON-ERR-500","서버 에러입니다. 서버 팀에 연락주세요!");
    ;

    private int status;
    private String errorCode;
    private String message;

}