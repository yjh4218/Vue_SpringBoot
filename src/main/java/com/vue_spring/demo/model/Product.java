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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private String skuNo;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false, length = 100)
    private String brandName;

    @Column(nullable = false, length = 100)
    private String maker;

    @Column(nullable = false, length = 100)
    private String className;

    @Column(nullable = false, length = 100)
    private int makeDate;

    @Column(nullable = false, length = 100)
    private int expDate;

    @CreationTimestamp
    private Timestamp createDate;
}
