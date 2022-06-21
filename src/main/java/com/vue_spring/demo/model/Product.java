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

@NoArgsConstructor
@Entity
@Data
public class Product {
    // Sku 번호
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
//    private long id;
//
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

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(String makeDate) {
        this.makeDate = makeDate;
    }

    public int getExpDate() {
        return expDate;
    }

    public void setExpDate(int expDate) {
        this.expDate = expDate;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getProductStandard() {
        return productStandard;
    }

    public void setProductStandard(String productStandard) {
        this.productStandard = productStandard;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductShape() {
        return productShape;
    }

    public void setProductShape(String productShape) {
        this.productShape = productShape;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
