package com.vue_spring.demo.DAO;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
public class MakerDAO<T> {
    private Optional<T> data;
    private String message;

    public MakerDAO() {
    };

    public MakerDAO(Optional<T> data) {
        this.data = data;
    }

    @Builder
    public MakerDAO(Optional<T> data, String message) {
        this.data = data;
        this.message = message;
    }
}
