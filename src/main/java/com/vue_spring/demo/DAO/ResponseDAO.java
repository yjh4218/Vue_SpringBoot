package com.vue_spring.demo.DAO;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class ResponseDAO<T> {

    private Optional<T> data;
    private int selectCnt;
    private String message;
    private List<List<Long>> idList;

    public ResponseDAO() {
    };

    public ResponseDAO(Optional<T> data) {
        this.data = data;
    }

    @Builder
    public ResponseDAO(Optional<T> data, int selectCnt, String message, List<List<Long>>  idList) {
        this.data = data;
        this.selectCnt = selectCnt;
        this.message = message;
        this.idList = idList;
    }

}
