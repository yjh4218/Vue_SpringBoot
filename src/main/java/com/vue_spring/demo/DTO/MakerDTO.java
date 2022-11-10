package com.vue_spring.demo.DTO;

import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;

@NoArgsConstructor
public class MakerDTO {

    private Long id;

    // 제조사명
    private String makerName;

    // 제조사 주소
    private String makerAddress;

    // 업종
    private String className;

    // 주요공정
    private String process;

    // 주요제품
    private String importProduct;

    // 매출액
    private Integer sales;

    // 거래처 담당자
    private String makerPerson;

    // 거래처 연락처
    private String makerPhone;

    // 거래처 메일주소
    private String makerEmail;

    // 비고
    private String note;

    // 제조사 정보
    private String makerInfo;

    // 제조사 평가점수
    private Integer makerScore;

    // 제조사 변경 내역
    private String makerChangeContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMakerName() {
        return makerName;
    }

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }

    public String getMakerAddress() {
        return makerAddress;
    }

    public void setMakerAddress(String makerAddress) {
        this.makerAddress = makerAddress;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getImportProduct() {
        return importProduct;
    }

    public void setImportProduct(String importProduct) {
        this.importProduct = importProduct;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getMakerPerson() {
        return makerPerson;
    }

    public void setMakerPerson(String makerPerson) {
        this.makerPerson = makerPerson;
    }

    public String getMakerPhone() {
        return makerPhone;
    }

    public void setMakerPhone(String makerPhone) {
        this.makerPhone = makerPhone;
    }

    public String getMakerEmail() {
        return makerEmail;
    }

    public void setMakerEmail(String makerEmail) {
        this.makerEmail = makerEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMakerChangeContent() {
        return makerChangeContent;
    }

    public void setMakerChangeContent(String makerChangeContent) {
        this.makerChangeContent = makerChangeContent;
    }

    public String getMakerInfo() {
        return makerInfo;
    }

    public void setMakerInfo(String makerInfo) {
        this.makerInfo = makerInfo;
    }

    public Integer getMakerScore() {
        return makerScore;
    }

    public void setMakerScore(Integer makerScore) {
        this.makerScore = makerScore;
    }
}
