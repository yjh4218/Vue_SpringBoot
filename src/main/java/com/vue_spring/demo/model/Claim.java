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
@Data
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

    // Claim에서 파일 처리 위함
    public void addPhoto(ClaimImage claimImage) {
        this.imageFile.add(claimImage);

        // 게시글에 파일이 저장되어있지 않은 경우
        if(claimImage.getClaim() != this)
            // 파일 저장
            claimImage.setClaim(this);
    }

}
