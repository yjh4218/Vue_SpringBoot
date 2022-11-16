package com.vue_spring.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Claim")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
//    @JsonManagedReference // 순환참조 방지
    @JoinColumn(name="productId")
//    @JsonIgnore
    private Product product;

    // 주문 번호
    @Column(nullable = true, length = 100)
    private String orderNum;

    // 젠데스크 티켓 ID
    @Column(nullable = true, length = 100)
    private String zenDeskID;

    // 클레임 접수 날짜
    @Column(nullable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date claimDate;

    // 유통기한
    @Column(nullable = true)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lotDate;

    // 클레임 분류
    @Column(nullable = true, length = 100)
    private String claimDecide;

    // 클레임 상세 분류
    @Column(nullable = true, length = 100)
    private String claimDecideSideOptions;
     
    // 회수 여부
    @Column(nullable = true, length = 300)
    private String recall;

    // 아사나 링크
    @Column(nullable = true, length = 300)
    private String asanaLink;

    // 클레임 내용
    @Column(nullable = true, length = 300)
    private String claimContent;

    // 특이사항
    @Column(nullable = true, length = 300)
    private String specialReport;

//     파일 원본명
    @OneToMany(
        mappedBy = "claim",
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
        orphanRemoval = true
    )
//    @JsonBackReference //순환참조 방지
    @Builder.Default
    private List<ClaimImage> imageFile = new ArrayList<>();

    // 비고
    @Column(nullable = true, length = 500)
    private String note;

    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;

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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getZenDeskID() {
        return zenDeskID;
    }

    public void setZenDeskID(String zenDeskID) {
        this.zenDeskID = zenDeskID;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public Date getLotDate() {
        return lotDate;
    }

    public void setLotDate(Date lotDate) {
        this.lotDate = lotDate;
    }

    public String getClaimDecide() {
        return claimDecide;
    }

    public void setClaimDecide(String claimDecide) {
        this.claimDecide = claimDecide;
    }

    public String getClaimDecideSideOptions() {
        return claimDecideSideOptions;
    }

    public void setClaimDecideSideOptions(String claimDecideSideOptions) {
        this.claimDecideSideOptions = claimDecideSideOptions;
    }

    public String getRecall() {
        return recall;
    }

    public void setRecall(String recall) {
        this.recall = recall;
    }

    public String getAsanaLink() {
        return asanaLink;
    }

    public void setAsanaLink(String asanaLink) {
        this.asanaLink = asanaLink;
    }

    public String getClaimContent() {
        return claimContent;
    }

    public void setClaimContent(String claimContent) {
        this.claimContent = claimContent;
    }

    public String getSpecialReport() {
        return specialReport;
    }

    public void setSpecialReport(String specialReport) {
        this.specialReport = specialReport;
    }

    public List<ClaimImage> getImageFile() {
        return imageFile;
    }

    public void setImageFile(List<ClaimImage> imageFile) {
        this.imageFile = imageFile;
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

    // Claim에서 파일 처리 위함
    public void addPhoto(ClaimImage claimImage) {
        this.imageFile.add(claimImage);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(claimImage.getClaim() != this)
            // 파일 저장
            claimImage.setClaim(this);
    }

}
