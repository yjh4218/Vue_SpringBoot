package com.vue_spring.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SalesProductComponent")
@Data
public class SalesProductComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    // skuNo
    @Column(nullable = false, length = 300)
    private String skuNo;

    // 제품명
    @Column(nullable = false, length = 100)
    private String productName;

    // 구성품 skuNo
    @Column(nullable = false, length = 300)
    private String componentSkuNo;

    // 구성품 제품명
    @Column(nullable = false, length = 100)
    private String componentProductName;

    // 구성품 수량
    @Column(nullable = false, length = 100)
    private long componentQuantity;

    // 구성품 구성 번호
    @Column(nullable = false, length = 100)
    private long componentNumber;

    // 구성품 구성 수량
    @Column(nullable = false, length = 100)
    private long productLength;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;


}
