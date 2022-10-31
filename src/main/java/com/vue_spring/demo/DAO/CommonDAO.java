package com.vue_spring.demo.DAO;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
public class CommonDAO<T> {

    private Optional<T> data;
    private String message;

    public CommonDAO() {
    };

    public CommonDAO(Optional<T> data) {
        this.data = data;
    }

    @Builder
    public CommonDAO(Optional<T> data, String message) {
        this.data = data;
        this.message = message;
    }

}
