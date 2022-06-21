package com.vue_spring.demo.DAO;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
public class InspectDAO<T> {
    private Optional<T> data;
    private String message;

    public InspectDAO() {
    };

    public InspectDAO(Optional<T> data) {
        this.data = data;
    }

    @Builder
    public InspectDAO(Optional<T> data, String message) {
        this.data = data;
        this.message = message;
    }
}
