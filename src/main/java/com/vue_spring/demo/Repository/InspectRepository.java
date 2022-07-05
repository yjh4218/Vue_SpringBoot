package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = false)
public interface InspectRepository extends JpaRepository<Inspect, Long> {

    Inspect findById(String id);

    Boolean existsByProductAndInspectDate(Product product, Date inspectDate);

    // 제품 조회(분류 포함)
    @Query(nativeQuery = true, value = "SELECT * FROM INSPECT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND class_name IN (:className) AND inspect_date BETWEEN :beforeDate AND :afterDate")
    Optional<List<Inspect>> findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCaseAndClassNameAndInspectDateBetween(
            @Param("skuNo")String skuNo,
            @Param("productName")String productName,
            @Param("className")  Set<String> className,
            @Param("beforeDate")Date beforeDate,
            @Param("afterDate") Date afterDate
            );

    // 제품 조회(분류 제외)
    @Query(nativeQuery = true, value = "SELECT * FROM INSPECT WHERE sku_no LIKE %:skuNo% AND product_name LIKE %:productName% AND brand_name LIKE %:brandName% AND maker LIKE %:maker% AND inspect_date BETWEEN :beforeDate AND :afterDate")
    Optional<List<Inspect>> findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCaseAndInspectDateBetween(
            @Param("skuNo")String skuNo,
            @Param("productName")String productName,
            @Param("beforeDate")Date beforeDate,
            @Param("afterDate") Date afterDate
    );


    // 제품 조회(분류 제외)
//    @Query(nativeQuery = false, value = "select DISTINCT i from Inspect i join fetch i.product p where p.skuNo like %:skuNo% and p.productName like %:productName% and p.brandName like %:brandName% and p.maker like %:maker% and p.productName like %:productName% AND i.inspectDate between :beforeDate AND :afterDate" )
//    Optional<List<Inspect>> findByInspectWithProduct(
//            @Param("skuNo")String skuNo,
//            @Param("productName")String productName,
//            @Param("brandName")String brandName,
//            @Param("maker")String maker,
//            @Param("beforeDate")Date beforeDate,
//            @Param("afterDate") Date afterDate
//    );

//    @Query(nativeQuery = true, value = "SELECT * FROM INSPECT WHERE inspect_date BETWEEN :beforeDate AND :afterDate")
    Optional<List<Inspect>> findByInspectDateBetween(Date beforeDate, Date afterDate);

}
