package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.model.MakerAudit;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = false)
public interface MakerAuditRepository extends JpaRepository<MakerAudit, Long> {

        // 제품 조회(분류 포함)
        @Query(nativeQuery = true, value = "SELECT * FROM maker_audit WHERE maker_name LIKE %:makerName% AND maker_address LIKE %:makerAddress% AND maker_person LIKE %:makerPerson% AND maker_phone LIKE %:makerPhone% AND class_name IN (:className) order by class_name desc")
        Optional<List<MakerAudit>> findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCaseAndtempBusinessType(
                @Param("makerName") String makerName,
                @Param("makerAddress") String makerAddress,
                @Param("makerPerson") String makerPerson,
                @Param("makerPhone") String makerPhone,
                @Param("className")  Set<String> className
        );
//
//        // 제품 조회(분류 제외)
        @Query(nativeQuery = true, value = "SELECT * FROM maker_audit WHERE maker_name LIKE %:makerName% AND maker_address LIKE %:makerAddress% AND maker_person LIKE %:makerPerson% AND maker_phone LIKE %:makerPhone% order by class_name desc")
        Optional<List<MakerAudit>> findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCase(
                String makerName, String makerAddress, String makerPerson, String makerPhone);

        Optional<List<MakerAudit>> findByMakerId(Long makerId);
//
//        // sku-no로 조회
//        Product findBySkuNo(String SkuNo);
//
//        // sku-no로 삭제
//        @Modifying
//        @Query("delete from Product where sku_no = ?1")
//        void deleteBySkuNo(String SkuNo);
//
//        // sku-no 중복 검사
//        Boolean existsBySkuNo(String SkuNo);
}
