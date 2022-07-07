package com.vue_spring.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "maker")
@Data
public class Maker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    // 제조사명
    @Column(nullable = false, length = 300)
    private String makerName;

    // 제조사 주소
    @Column(nullable = false, length = 300)
    private String makerAddress;

    // 업종
    @Column(nullable = false, length = 100)
    private String businessType;

    // 주요공정
    @Column(nullable = true, length = 100)
    private String process;

    // 주요제품
    @Column(nullable = true, length = 100)
    private String importProduct;

    // 매출액
    @Column(nullable = true, length = 100)
    private Integer sales;

    // 거래처 담당자
    @Column(nullable = true, length = 100)
    private String makerPerson;

    // 거래처 연락처
    @Column(nullable = true, length = 100)
    private String makerPhone;

    // 거래처 메일주소
    @Column(nullable = true, length = 100)
    private String makerEmail;

    // 비고
    @Column(nullable = true, length = 500)
    private String note;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;


}
