package com.vue_spring.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Optional;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InspectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
//    @JsonManagedReference // 순환참조 방지
    @JsonIgnore
    @JoinColumn(name="InspectId")
    private Inspect inspect;

    // 파일 원본명
    @Column(nullable = true)
    private String imgFileName;

    // 파일 저장 경로
    @Column(nullable = true)
    private String imgFilePath;

    // 파일 사이즈
    @Column(nullable = true)
    private Long imgFileSize;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;

     @Builder
    public InspectImage(String imgFileName, String imgFilePath, Long imgFileSize){
        this.imgFileName = imgFileName;
        this.imgFilePath = imgFilePath;
        this.imgFileSize = imgFileSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inspect getInspect() {
        return inspect;
    }

    public void setInspect(Inspect inspect) {
        this.inspect = inspect;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
    }

    public Long getImgFileSize() {
        return imgFileSize;
    }

    public void setImgFileSize(Long imgFileSize) {
        this.imgFileSize = imgFileSize;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
