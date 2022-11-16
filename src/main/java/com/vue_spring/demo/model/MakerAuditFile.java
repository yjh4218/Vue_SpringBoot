package com.vue_spring.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "maker_audit_file")
@NoArgsConstructor
@AllArgsConstructor
public class MakerAuditFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonManagedReference // 순환참조 방지
    @JsonIgnore
    @JoinColumn(name="makerAuditId")
    private MakerAudit makerAudit;

    // 파일 원본명
    @Column(nullable = true)
    private String fileName;

    // 파일 저장 경로
    @Column(nullable = true)
    private String fileOutPath;

    // 파일 저장 경로
    @Column(nullable = true)
    private String fileInPath;

    // 파일 사이즈
    @Column(nullable = true)
    private Long fileSize;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public MakerAuditFile(String fileName, String fileOutPath, String fileInPath, Long fileSize) {
        this.fileName = fileName;
        this.fileOutPath = fileOutPath;
        this.fileInPath = fileInPath;
        this.fileSize = fileSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MakerAudit getMakerAudit() {
        return makerAudit;
    }

    public void setMakerAudit(MakerAudit makerAudit) {
        this.makerAudit = makerAudit;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileOutPath() {
        return fileOutPath;
    }

    public void setFileOutPath(String fileOutPath) {
        this.fileOutPath = fileOutPath;
    }

    public String getFileInPath() {
        return fileInPath;
    }

    public void setFileInPath(String fileInPath) {
        this.fileInPath = fileInPath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}