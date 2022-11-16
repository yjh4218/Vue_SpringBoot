package com.vue_spring.demo.DTO;

import lombok.Data;

@Data
public class ErrorDto {
    private final int status;
    private final String message;
}
