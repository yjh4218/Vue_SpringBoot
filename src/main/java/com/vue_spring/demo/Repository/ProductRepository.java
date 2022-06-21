package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = false)
public interface ProductRepository extends JpaRepository<Product, Long> {

        // 제품 조회(분류 포함)
        @Query(nativeQuery = true, value = "SELECT * FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className)")
        Optional<List<Product>> findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCaseAndClassName(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("className")  Set<String> className
        );

        // 제품 조회(분류 제외)
        Optional<List<Product>> findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCase(String skuNo, String productName, String brandName, String maker);

        // sku-no로 조회
        Product findBySkuNo(String SkuNo);

        // sku-no로 삭제
        @Modifying
        @Query("delete from Product where sku_no = ?1")
        void deleteBySkuNo(String SkuNo);

        // sku-no 중복 검사
        Boolean existsBySkuNo(String SkuNo);
}
