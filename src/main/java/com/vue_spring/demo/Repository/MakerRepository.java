package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = false)
public interface MakerRepository extends JpaRepository<Maker, Long> {

        // 제품 조회(분류 포함)
        @Query(nativeQuery = true, value = "SELECT * FROM MAKER WHERE maker_name LIKE %:makerName% AND maker_address LIKE %:makerAddress% AND maker_person LIKE %:makerPerson% AND maker_phone LIKE %:makerPhone% AND class_name IN (:className) order by class_name desc")
        Optional<List<Maker>> findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCaseAndtempBusinessType(
                @Param("makerName") String makerName,
                @Param("makerAddress") String makerAddress,
                @Param("makerPerson") String makerPerson,
                @Param("makerPhone") String makerPhone,
                @Param("className")  Set<String> className
        );
//
//        // 제품 조회(분류 제외)
        @Query(nativeQuery = true, value = "SELECT * FROM MAKER WHERE maker_name LIKE %:makerName% AND maker_address LIKE %:makerAddress% AND maker_person LIKE %:makerPerson% AND maker_phone LIKE %:makerPhone% order by class_name desc")
        Optional<List<Maker>> findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCase(
                String makerName, String makerAddress, String makerPerson, String makerPhone);
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
