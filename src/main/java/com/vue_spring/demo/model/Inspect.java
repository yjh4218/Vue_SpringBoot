package com.vue_spring.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inspect")
public class Inspect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
//    @JsonManagedReference // 순환참조 방지
    @JoinColumn(name="productId")
//    @JsonIgnore
    private Product product;

    // 검수날짜
    @Column(nullable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date inspectDate;

    // 유통기한
    @Column(nullable = true)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lotDate;

    // 검수 결과
    @Column(nullable = true, length = 100)
    private String decideResult;

    // 수분율율
    @Column(nullable = true, length = 100)
    private Float moisture;

    // 성상
    @Column(nullable = true, length = 100)
    private String appearance;

    // 검수 내용
    @Column(nullable = true, length = 300)
    private String inspectContent;

    // 색상
    @Column(nullable = true, length = 300)
    private String color;

    // 크기
    @Column(nullable = true, length = 300)
    private String size;

    // 파손여부
    @Column(nullable = true, length = 300)
    private String damage;

    // 마감상태
    @Column(nullable = true, length = 300)
    private String finishState;

    // 작동여부
    @Column(nullable = true, length = 300)
    private String checkWork;

    // 사용성
    @Column(nullable = true, length = 300)
    private String usability;

    // 중량
    @Column(nullable = true, length = 300)
    private String weight;

    // 포장상태
    @Column(nullable = true, length = 300)
    private String checkPacking;

    // 이물/이취
    @Column(nullable = true, length = 300)
    private String foreignBody;

    // 특이사항
    @Column(nullable = true, length = 300)
    private String specialReport;

//     파일 원본명
    @OneToMany(
        mappedBy = "inspect",
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
        orphanRemoval = true
    )
//    @JsonBackReference //순환참조 방지
    @Builder.Default
    private List<InspectImage> imageFile = new ArrayList<>();

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

    public Date getInspectDate() {
        return inspectDate;
    }

    public void setInspectDate(Date inspectDate) {
        this.inspectDate = inspectDate;
    }

    public Date getLotDate() {
        return lotDate;
    }

    public void setLotDate(Date lotDate) {
        this.lotDate = lotDate;
    }

    public String getDecideResult() {
        return decideResult;
    }

    public void setDecideResult(String decideResult) {
        this.decideResult = decideResult;
    }

    public Float getMoisture() {
        return moisture;
    }

    public void setMoisture(Float moisture) {
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

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getFinishState() {
        return finishState;
    }

    public void setFinishState(String finishState) {
        this.finishState = finishState;
    }

    public String getCheckWork() {
        return checkWork;
    }

    public void setCheckWork(String checkWork) {
        this.checkWork = checkWork;
    }

    public String getUsability() {
        return usability;
    }

    public void setUsability(String usability) {
        this.usability = usability;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCheckPacking() {
        return checkPacking;
    }

    public void setCheckPacking(String checkPacking) {
        this.checkPacking = checkPacking;
    }

    public String getForeignBody() {
        return foreignBody;
    }

    public void setForeignBody(String foreignBody) {
        this.foreignBody = foreignBody;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }
}
