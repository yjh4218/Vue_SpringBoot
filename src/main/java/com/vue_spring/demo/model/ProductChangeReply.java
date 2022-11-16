package com.vue_spring.demo.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "product_change_reply")
@NoArgsConstructor
@AllArgsConstructor
public class ProductChangeReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonManagedReference // 순환참조 방지
    @JsonIgnore
    @JoinColumn(name="productId")
    private Product product;

    // 제품 변경 내역
    @Column(nullable = true)
    private String productChangeReply;

    // 검수날짜
    @Column(nullable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate replyDate;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public ProductChangeReply(String productChangeReply){
        this.productChangeReply = productChangeReply;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductChangeReply() {
        return productChangeReply;
    }

    public void setProductChangeReply(String productChangeReply) {
        this.productChangeReply = productChangeReply;
    }

    public LocalDate getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDate replyDate) {
        this.replyDate = replyDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}