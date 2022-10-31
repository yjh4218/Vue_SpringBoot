package com.vue_spring.demo.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maker_audit")
@NoArgsConstructor
@AllArgsConstructor
public class MakerAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonManagedReference // 순환참조 방지
    @JsonIgnore
    @JoinColumn(name="makerId")
    private Maker maker;

    // 작성일자
    @Column(nullable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate writDate;

    // 오디트 일자
    @Column(nullable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate auditDate;

    // 오디트 점검 목적
    @Column(nullable = true)
    private String auditPurpose;

    // 오디트 점수
    @Column(nullable = true)
    private Integer auditScore;

    // 오디트 점검 결과
    @Column(nullable = true)
    private String auditResult;

    // 오디트 점검 내용
    @Column(nullable = true, length = 1000)
    private String auditContent;

    // 오디트 부적합 사항
    @Column(nullable = true, length = 1000)
    private String auditIncon;

    // 오디트 개선 사항
    @Column(nullable = true, length = 1000)
    private String auditImprove;

    //     파일 원본명
    @OneToMany(
            mappedBy = "makerAudit",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
//    @JsonBackReference //순환참조 방지
    @BatchSize(size=10)
    private List<MakerAuditFile> makerAuditFiles = new ArrayList<>();

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;

    // product에서 이미지 파일 처리 위함
    public void addFile(MakerAuditFile makerAuditFile) {
        this.makerAuditFiles.add(makerAuditFile);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(makerAuditFile.getMakerAudit() != this)
            // 파일 저장
            makerAuditFile.setMakerAudit(this);
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

    public LocalDate getWritDate() {
        return writDate;
    }

    public void setWritDate(LocalDate writDate) {
        this.writDate = writDate;
    }

    public LocalDate getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(LocalDate auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditPurpose() {
        return auditPurpose;
    }

    public void setAuditPurpose(String auditPurpose) {
        this.auditPurpose = auditPurpose;
    }

    public Integer getAuditScore() {
        return auditScore;
    }

    public void setAuditScore(Integer auditScore) {
        this.auditScore = auditScore;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public String getAuditIncon() {
        return auditIncon;
    }

    public void setAuditIncon(String auditIncon) {
        this.auditIncon = auditIncon;
    }

    public String getAuditImprove() {
        return auditImprove;
    }

    public void setAuditImprove(String auditImprove) {
        this.auditImprove = auditImprove;
    }

    public List<MakerAuditFile> getMakerAuditFiles() {
        return makerAuditFiles;
    }

    public void setMakerAuditFiles(List<MakerAuditFile> makerAuditFiles) {
        this.makerAuditFiles = makerAuditFiles;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}