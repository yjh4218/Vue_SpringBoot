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
@Table(name = "maker_change_reply")
@NoArgsConstructor
@AllArgsConstructor
public class MakerChangeReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonManagedReference // 순환참조 방지
    @JsonIgnore
    @JoinColumn(name="makerId")
    private Maker maker;

    // 제품 변경 내역
    @Column(nullable = true)
    private String makerChangeReply;

    // 검수날짜
    @Column(nullable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate replyDate;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public MakerChangeReply(String makerChangeReply){
        this.makerChangeReply = makerChangeReply;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Maker getMaker() {
        return maker;
    }

    public void setMaker(Maker maker) {
        this.maker = maker;
    }

    public String getMakerChangeReply() {
        return makerChangeReply;
    }

    public void setMakerChangeReply(String makerChangeReply) {
        this.makerChangeReply = makerChangeReply;
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