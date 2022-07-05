package com.vue_spring.demo.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    // Sku 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment

    private long id;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date makeDate;

    // 제품 유통기한
    @Column(nullable = false, length = 100)
    private Integer expDate;

    // 제품 구매가격
    @Column(nullable = true, length = 100)
    private Integer purchasePrice;

    // 제품 판매가격
    @Column(nullable = true, length = 100)
    private Integer sellingPrice;

    // 제품 성상
    @Column(nullable = true, length = 300)
    private String productCondition;

    // 제품 규격
    @Column(nullable = true, length = 300)
    private String productStandard;

    // 제품 열량
    @Column(nullable = true, length = 100)
    private Float calorie;

    // 제품 나트륨
    @Column(nullable = true, length = 100)
    private Float sodium;

    // 제품 색상
    @Column(nullable = true, length = 300)
    private String productColor;

    // 제품 모양
    @Column(nullable = true, length = 300)
    private String productShape;

    // 비고
    @Column(nullable = true, length = 500)
    private String note;

    @OneToMany (mappedBy = "product")
    @JsonBackReference //순환참조 방지
    private List<Inspect> insepct = new ArrayList<>();

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    public Integer getExpDate() {
        return expDate;
    }

    public void setExpDate(Integer expDate) {
        this.expDate = expDate;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Integer sellingPrice) {
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

    public Float getCalorie() {
        return calorie;
    }

    public void setCalorie(Float calorie) {
        this.calorie = calorie;
    }

    public Float getSodium() {
        return sodium;
    }

    public void setSodium(Float sodium) {
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

    public List<Inspect> getInsepct() {
        return insepct;
    }

    public void setInsepct(List<Inspect> insepct) {
        this.insepct = insepct;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
