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

        // 제품 조회(분류 포함) - 처음 15개 조회
        @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className) AND operation IN (:operation) order by id asc limit 15")
        Optional<List<Product>> findByProductListClassOperFirst(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("className")  Set<String> className,
                @Param("operation") Set<String> operation
        );

        @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className) order by id asc limit 15")
        Optional<List<Product>> findByProductListClassFirst(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("className")  Set<String> className
        );

        @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker%  AND operation IN (:operation) order by id asc limit 15")
        Optional<List<Product>> findByProductListOperFirst(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("operation") Set<String> operation
        );

        // 제품 조회(분류 제외) - 처음 15개 조회
        @Transactional(readOnly = true)
        @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% order by id asc limit 15")
        Optional<List<Product>> findByProductListFirst(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker
        );

        // 제품 조회(분류 제외) - 커서에 맞춰 15개 조회
        @Transactional(readOnly = true)
        @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM PRODUCT WHERE id in (:productCurseId)")
        Optional<List<Product>> findByProductListCurse(
                @Param("productCurseId") Long[] productCurseId
        );

        // 제품 조회(분류 포함) - 모두 조회
        @Query(nativeQuery = true, value = "SELECT DISTINCT id FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className)")
        List<Long>  findByProductListAndClassName(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("className")  Set<String> className
        );

        // 제품 조회(분류 제외) - 모두 조회
        @Transactional(readOnly = true)
        @Query(nativeQuery = true, value = "SELECT DISTINCT id FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker%")
        List<Long> findByProductList(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker
        );

        // 제품 조회(운영여부, 제품분류 포함) - 모두 조회(엑셀용)
        @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className) AND operation IN (:operation)")
        Optional<List<Product>>  findByProductClassOperExcel(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("className") Set<String> className,
                @Param("operation") Set<String> operation
        );

        // 제품 조회(제품 분류 포함) - 모두 조회(엑셀용)
        @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className)")
        Optional<List<Product>>  findByProductClassExcel(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("className") Set<String> className
        );

        // 제품 조회(운영여부 분류 포함) - 모두 조회(엑셀용)
        @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND operation IN (:operation)")
        Optional<List<Product>>  findByProductOperExcel(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("operation") Set<String> operation
        );

        // 제품 조회(분류 제외) - 모두 조회(엑셀용)
        @Transactional(readOnly = true)
        @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker%")
        Optional<List<Product>> findByProductListExcel(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker
        );

        // 제품 검색 수량 확인- 분류 없음
        @Transactional(readOnly = true)
        @Query(nativeQuery = true, value = "SELECT DISTINCT COUNT(1) FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker%")
        Long findByProductCnt(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker
        );

        // 제품 검색 수량 확인(분류 포함) - 모두 조회
        @Query(nativeQuery = true, value = "SELECT DISTINCT COUNT(1) FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className)")
        Long findByProductCntClassName(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("className")  Set<String> className
        );

        // 제품 id 모두 조회(분류포함)
        @Query(nativeQuery = true, value = "SELECT DISTINCT id FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className)  AND operation IN (:operation)")
        List<Long> findByIdClassName(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker,
                @Param("className")  Set<String> className,
                @Param("operation") Set<String> operation
        );

        // 제품 id 모두 조회 - 분류 없음
        @Transactional(readOnly = true)
        @Query(nativeQuery = true, value = "SELECT DISTINCT id FROM PRODUCT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker%")
        List<Long> findById(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("brandName")String brandName,
                @Param("maker")String maker
        );

        // sku-no로 삭제
        @Modifying
        @Query("delete from Product where sku_no = ?1")
        void deleteBySkuNo(@Param("skuNo") String SkuNo);

        // sku-no 중복 검사
        Boolean existsBySkuNo(String SkuNo);
}
