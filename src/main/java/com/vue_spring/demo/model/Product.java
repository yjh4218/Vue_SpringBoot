package com.vue_spring.demo.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    // Sku 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private long id;

    @Column(length = 13)
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

    // 보관방법
    @Column(nullable = false, length = 100)
    private String storMethod;

    // 수분율
    @Column(nullable = false)
    private long moisture;

    // 제품 초도생산 일자
    @Column(nullable = false, length = 100)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date makeDate;

    // 제품 유통기한
    @Column(nullable = false, length = 100)
    private String expDate;

    //     파일 원본명
    @OneToMany(
            mappedBy = "product",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
//    @JsonBackReference //순환참조 방지
    @Builder.Default
    @BatchSize(size=10)
    private List<ProductImage> imageFile = new ArrayList<>();

    // 제품 중량(100g 기준)
    @Column(nullable = true, length = 100)
    private String productWeight;

    // 원재료명
    @Column(nullable = true, length = 500)
    private String rawMaterialName;

    // 제품 규격
    @Column(nullable = true, length = 500)
    private String productStandard;


    // 포장 단위
    @Column(nullable = true, length = 500)
    private String packingUnit;

    // 제품 열량
    @Column(nullable = true, length = 100)
    private Float calorie;

    // 제품 나트륨
    @Column(nullable = true, length = 100)
    private Float sodium;

    // 비고
    @Column(nullable = true, length = 500)
    private String note;


    // 제품 변경내역
    @OneToMany(
            mappedBy = "product",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
//    @JsonBackReference //순환참조 방지
    @Builder.Default
    @BatchSize(size=10)
    private List<ProductChangeReply> productChangeReplies = new ArrayList<>();

    @Builder.Default
    @OneToMany (mappedBy = "product")
    @JsonBackReference //순환참조 방지
    @BatchSize(size=10)
    private List<Inspect> inspect = new ArrayList<>();

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;


    // product에서 이미지 파일 처리 위함
    public void addPhoto(ProductImage productImage) {
        this.imageFile.add(productImage);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(productImage.getProduct() != this)
            // 파일 저장
            productImage.setProduct(this);
    }

    // product에서 제품 내용 변경 처리 위함
    public void addReply(ProductChangeReply productChangeReply) {
        this.productChangeReplies.add(productChangeReply);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(productChangeReply.getProduct() != this)
            // 파일 저장
            productChangeReply.setProduct(this);
    }

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

    public String getStorMethod() {
        return storMethod;
    }

    public void setStorMethod(String storMethod) {
        this.storMethod = storMethod;
    }

    public long getMoisture() {
        return moisture;
    }

    public void setMoisture(long moisture) {
        this.moisture = moisture;
    }

    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public List<ProductImage> getImageFile() {
        return imageFile;
    }

    public void setImageFile(List<ProductImage> imageFile) {
        this.imageFile = imageFile;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getRawMaterialName() {
        return rawMaterialName;
    }

    public void setRawMaterialName(String rawMaterialName) {
        this.rawMaterialName = rawMaterialName;
    }

    public String getProductStandard() {
        return productStandard;
    }

    public void setProductStandard(String productStandard) {
        this.productStandard = productStandard;
    }

    public String getPackingUnit() {
        return packingUnit;
    }

    public void setPackingUnit(String packingUnit) {
        this.packingUnit = packingUnit;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ProductChangeReply> getProductChangeReplies() {
        return productChangeReplies;
    }

    public void setProductChangeReplies(List<ProductChangeReply> productChangeReplies) {
        this.productChangeReplies = productChangeReplies;
    }

    public List<Inspect> getInspect() {
        return inspect;
    }

    public void setInspect(List<Inspect> inspect) {
        this.inspect = inspect;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
