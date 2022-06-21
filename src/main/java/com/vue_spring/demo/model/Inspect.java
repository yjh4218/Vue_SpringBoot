package com.vue_spring.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Inspect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="skuNo")
    private Product product;

//    @Column(nullable = false, length = 12)
//    private String skuNo;

    // 제품명
    @Column(nullable = false, length = 100)
    private String productName;

    // 검수날짜
    @Column(nullable = false, length = 100)
    private String inspectDate;

    // 유통기한
    @Column(nullable = false, length = 100)
    private String lotDate;

    // 검수 결과
    @Column(nullable = false, length = 100)
    private String decideResult;

    // 수분율율
    @Column(nullable = false, length = 100)
    private int moisture;

    // 검수 내용
    @Column(nullable = true, length = 300)
    private String inspectContent;

    // 특이사항
    @Column(nullable = true, length = 300)
    private String specialReport;

    // 이미지 파일
//    @Lob
//    private List<MultipartFile> imgFiles;

//     파일 원본명
    @OneToMany(
        mappedBy = "inspect",
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
        orphanRemoval = true
    )
//    @JsonBackReference //순환참조 방지
    @Builder.Default
    private List<InspectImage> imageFile = new ArrayList<>();

    // 비고
    @Column(nullable = true, length = 500)
    private String note;

    
    // 데이터 입력, 수정 시간
    @CreationTimestamp
    private Timestamp createDate;
//
//    @Builder
//    public Inspect(List<String> imgFileName, List<String> imgFilePath, List<Long> imgFileSize){
//        this.imgFileName = imgFileName;
//        this.imgFilePath = imgFilePath;
//        this.imgFileSize = imgFileSize;
//    }

    // Inspect에서 파일 처리 위함
    public void addPhoto(InspectImage inspectImage) {
        this.imageFile.add(inspectImage);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(inspectImage.getInspect() != this)
            // 파일 저장
            inspectImage.setInspect(this);
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getInspectDate() {
        return inspectDate;
    }

    public void setInspectDate(String inspectDate) {
        this.inspectDate = inspectDate;
    }

    public String getLotDate() {
        return lotDate;
    }

    public void setLotDate(String lotDate) {
        this.lotDate = lotDate;
    }

    public String getDecideResult() {
        return decideResult;
    }

    public void setDecideResult(String decideResult) {
        this.decideResult = decideResult;
    }

    public int getMoisture() {
        return moisture;
    }

    public void setMoisture(int moisture) {
        this.moisture = moisture;
    }

    public String getInspectContent() {
        return inspectContent;
    }

    public void setInspectContent(String inspectContent) {
        this.inspectContent = inspectContent;
    }

    public String getSpecialReport() {
        return specialReport;
    }

    public void setSpecialReport(String specialReport) {
        this.specialReport = specialReport;
    }

    public List<InspectImage> getImageFile() {
        return imageFile;
    }

    public void setImageFile(List<InspectImage> imageFile) {
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
}
