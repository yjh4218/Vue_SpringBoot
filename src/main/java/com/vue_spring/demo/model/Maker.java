package com.vue_spring.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "maker")
public class Maker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    // 제조사명
    @Column(nullable = false, length = 300)
    private String makerName;

    // 제조사 주소
    @Column(nullable = false, length = 300)
    private String makerAddress;

    // 업종
    @Column(nullable = false, length = 100)
    private String className;

    // 주요공정
    @Column(nullable = true, length = 100)
    private String process;

    // 주요제품
    @Column(nullable = true, length = 100)
    private String importProduct;

    // 매출액
    @Column(nullable = true, length = 100)
    private Integer sales;

    // 거래처 담당자
    @Column(nullable = true, length = 100)
    private String makerPerson;

    // 거래처 연락처
    @Column(nullable = true, length = 100)
    private String makerPhone;

    // 거래처 메일주소
    @Column(nullable = true, length = 100)
    private String makerEmail;

    // 비고
    @Column(nullable = true, length = 500)
    private String note;

    // 제조사 변경 내역
    @OneToMany(
            mappedBy = "maker",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @BatchSize(size=10)
    private List<MakerChangeReply> makerChangeReplies = new ArrayList<>();


    // 제조사 점검 내역
    @OneToMany(
            mappedBy = "maker",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @BatchSize(size=10)
    private List<MakerAudit> makerAuditList = new ArrayList<>();

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;

    // maker에서 제조사 변경 처리 위함
    public void addReply(MakerChangeReply makerChangeReply) {
        this.makerChangeReplies.add(makerChangeReply);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(makerChangeReply.getMaker() != this)
            // 파일 저장
            makerChangeReply.setMaker(this);
    }

    // maker에서 제조사 점검 변경 처리 위함
    public void addMakerAudit(MakerAudit makerAudit) {
        this.makerAuditList.add(makerAudit);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(makerAudit.getMaker() != this)
            // 파일 저장
            makerAudit.setMaker(this);
    }

    @Builder
    public Maker(Long id, String makerName, String makerAddress, String className, String process, String importProduct, Integer sales, String makerPerson, String makerPhone, String makerEmail, String note) {
        this.id = id;
        this.makerName = makerName;
        this.makerAddress = makerAddress;
        this.className = className;
        this.process = process;
        this.importProduct = importProduct;
        this.sales = sales;
        this.makerPerson = makerPerson;
        this.makerPhone = makerPhone;
        this.makerEmail = makerEmail;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMakerName() {
        return makerName;
    }

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }

    public String getMakerAddress() {
        return makerAddress;
    }

    public void setMakerAddress(String makerAddress) {
        this.makerAddress = makerAddress;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getImportProduct() {
        return importProduct;
    }

    public void setImportProduct(String importProduct) {
        this.importProduct = importProduct;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getMakerPerson() {
        return makerPerson;
    }

    public void setMakerPerson(String makerPerson) {
        this.makerPerson = makerPerson;
    }

    public String getMakerPhone() {
        return makerPhone;
    }

    public void setMakerPhone(String makerPhone) {
        this.makerPhone = makerPhone;
    }

    public String getMakerEmail() {
        return makerEmail;
    }

    public void setMakerEmail(String makerEmail) {
        this.makerEmail = makerEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<MakerChangeReply> getMakerChangeReplies() {
        return makerChangeReplies;
    }

    public void setMakerChangeReplies(List<MakerChangeReply> makerChangeReplies) {
        this.makerChangeReplies = makerChangeReplies;
    }

    public List<MakerAudit> getMakerAuditList() {
        return makerAuditList;
    }

    public void setMakerAuditList(List<MakerAudit> makerAuditList) {
        this.makerAuditList = makerAuditList;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
