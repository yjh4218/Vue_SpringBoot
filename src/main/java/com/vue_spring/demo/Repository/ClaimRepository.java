package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.Claim;
import com.vue_spring.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = false)
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    Claim findById(String id);

    Boolean existsByProductAndClaimDate(Product product, Date claimDate);

    Boolean existsByProductId(Long productId);

    Boolean deleteByProductId(Long productId);

    // 제품 조회(분류 포함)
//    @Query(nativeQuery = true, value = "SELECT * FROM CLAIM WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className) AND claim_date BETWEEN :beforeDate AND :afterDate")
//    Optional<List<Claim>> findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCaseAndClassNameAndClaimDateBetween(
//            @Param("skuNo") String skuNo,
//            @Param("productName") String productName,
//            @Param("brandName") String brandName,
//            @Param("maker") String maker,
//            @Param("className") Set<String> className,
//            @Param("beforeDate") Date beforeDate,
//            @Param("afterDate") Date afterDate
//            );
//
//    // 제품 조회(분류 제외)
//    @Query(nativeQuery = true, value = "SELECT * FROM CLAIM WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND claim_date BETWEEN :beforeDate AND :afterDate")
//    Optional<List<Claim>> findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCaseAndClaimDateBetween(
//            @Param("skuNo")String skuNo,
//            @Param("productName")String productName,
//            @Param("brandName") String brandName,
//            @Param("maker") String maker,
//            @Param("beforeDate")Date beforeDate,
//            @Param("afterDate") Date afterDate
//    );

    Optional<List<Claim>> findByProductIdAndClaimDateBetween(Long productId, Date beforeDate, Date afterDate);

    Optional<List<Claim>> findByProductId(Long productId);

    Optional<List<Claim>> findByClaimDateBetween(Date beforeDate, Date afterDate);



}
