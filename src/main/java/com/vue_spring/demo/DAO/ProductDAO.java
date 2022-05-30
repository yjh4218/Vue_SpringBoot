package com.vue_spring.demo.DAO;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
public class ProductDAO<T> {

    private Optional<T> data;
    private String message;

    public ProductDAO() {
    };

    public ProductDAO(Optional<T> data) {
        this.data = data;
    }

    @Builder
    public ProductDAO(Optional<T> data, String message) {
        this.data = data;
        this.message = message;
    }

}
