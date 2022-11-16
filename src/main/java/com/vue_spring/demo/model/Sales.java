package com.vue_spring.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Sales")
@Data
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    // skuNo
    @Column(nullable = false, length = 300)
    private String skuNo;

    // 제품명
    @Column(nullable = false, length = 100)
    private String productName;

    // 판매량 날짜
    @Column(nullable = false, length = 100)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date salesMonth;

    // 판매량
    @Column(nullable = false, length = 100)
    private long salesVolumn;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;


}
