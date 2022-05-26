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

@Entity
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int sku_no;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false, length = 100)
    private String brandName;

    @Column(nullable = false, length = 100)
    private String maker;

    @CreationTimestamp
    private Timestamp createDate;
}
