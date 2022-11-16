package com.vue_spring.demo.DAO;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
public class ClaimDAO<T> {
    private Optional<T> data;
    private String message;

    public ClaimDAO() {
    };

    public ClaimDAO(Optional<T> data) {
        this.data = data;
    }

    @Builder
    public ClaimDAO(Optional<T> data, String message) {
        this.data = data;
        this.message = message;
    }
}
