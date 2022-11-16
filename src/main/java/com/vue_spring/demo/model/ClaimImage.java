package com.vue_spring.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "claim_image")
@NoArgsConstructor
@AllArgsConstructor
public class ClaimImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonManagedReference // 순환참조 방지
    @JsonIgnore
    @JoinColumn(name="ClaimId")
    private Claim claim;

    // 파일 원본명
    @Column(nullable = true)
    private String imgFileName;

    // 파일 저장 경로
    @Column(nullable = true)
    private String imgFileOutPath;

    // 파일 저장 경로
    @Column(nullable = true)
    private String imgFileInPath;

    // 파일 사이즈
    @Column(nullable = true)
    private Long imgFileSize;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public ClaimImage(String imgFileName, String imgFileOutPath, String imgFileInPath, Long imgFileSize) {
        this.imgFileName = imgFileName;
        this.imgFileOutPath = imgFileOutPath;
        this.imgFileInPath = imgFileInPath;
        this.imgFileSize = imgFileSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }

    public String getImgFileOutPath() {
        return imgFileOutPath;
    }

    public void setImgFileOutPath(String imgFileOutPath) {
        this.imgFileOutPath = imgFileOutPath;
    }

    public String getImgFileInPath() {
        return imgFileInPath;
    }

    public void setImgFileInPath(String imgFileInPath) {
        this.imgFileInPath = imgFileInPath;
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
