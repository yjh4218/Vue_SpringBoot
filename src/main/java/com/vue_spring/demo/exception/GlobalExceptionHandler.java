package com.vue_spring.demo.exception;


import com.vue_spring.demo.DTO.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.vue_spring.demo.exception.ErrorCode.INTERNAL_SERVER_ERROR;

// 에러 발생 시 처리
// https://velog.io/@kiiiyeon/%EC%8A%A4%ED%94%84%EB%A7%81-ExceptionHandler%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC#restcontrolleradvice
// 위 주소 참조
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler({ CustomException.class })
//    protected ResponseEntity handleCustomException(CustomException ex) {
//        return new ResponseEntity(new ErrorDto(ex.getErrorCode().getStatus(), ex.getErrorCode().getMessage()), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
//    }
//
//    @ExceptionHandler({ Exception.class })
//    protected ResponseEntity handleServerException(Exception ex) {
//        return new ResponseEntity(new ErrorDto(INTERNAL_SERVER_ERROR.getStatus(), INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}