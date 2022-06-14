package com.vue_spring.demo.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Product {
    // Sku 번호
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    @Column(length = 12)
    private String skuNo;

    // 제품명
    @Column(nullable = false, length = 100)
    private String productName;

    // 브랜드명
    @Column(nullable = false, length = 100)
    private String brandName;

    // 제조사
    @Column(nullable = false, length = 100)
    private String maker;

    // 제품분류
    @Column(nullable = false, length = 100)
    private String className;

    // 제품 초도생산 일자
    @Column(nullable = false, length = 100)
    private String makeDate;

    // 제품 유통기한
    @Column(nullable = false, length = 100)
    private int expDate;

    // 제품 구매가격
    @Column(nullable = true, length = 100)
    private int purchasePrice;

    // 제품 판매가격
    @Column(nullable = true, length = 100)
    private int sellingPrice;

    // 제품 성상
    @Column(nullable = true, length = 300)
    private String productCondition;

    // 제품 규격
    @Column(nullable = true, length = 300)
    private String productStandard;

    // 제품 열량
    @Column(nullable = true, length = 100)
    private double calorie;

    // 제품 나트륨
    @Column(nullable = true, length = 100)
    private double sodium;

    // 제품 색상
    @Column(nullable = true, length = 300)
    private String productColor;

    // 제품 모양
    @Column(nullable = true, length = 300)
    private String productShape;

    // 비고
    @Column(nullable = true, length = 500)
    private String note;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;
}
