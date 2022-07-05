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
@Data
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

    // 검수 내용
    @Column(nullable = true, length = 300)
    private String inspectContent;

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

}
