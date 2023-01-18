package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.model.SalesProductComponent;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = false)
public interface SalesProductComponentRepository extends JpaRepository<SalesProductComponent, Long> {

    // 제품 조회(분류 제외)
    Optional<List<SalesProductComponent>> findBySkuNoContainingAndProductNameContainingAndComponentSkuNoContainingAndComponentProductNameContainingIgnoreCase(
            @Param("skuNo") String skuNo,
            @Param("productName") String productName,
            @Param("componentSkuNo") String componentSkuNo,
            @Param("componentProductName") String componentProductName);

    Boolean existsBySkuNo(@Param("skuNo") String skuNo);

    // sku-no로 삭제
    @Modifying
    @Query("delete from SalesProductComponent where sku_no = :skuNo")
    void deleteBySkuNo(@Param("skuNo") String skuNo);

    @Modifying
    @Query(nativeQuery = true, value = "update sales_product_component set \n" +
            "product_name = if(sku_no = :skuNo, :productName, product_name),\n" +
            " component_product_name = if(component_sku_no = :skuNo, :productName, component_product_name)\n" +
            " where sku_no = :skuNo or component_sku_no = :skuNo")
    void updateProduct(@Param("skuNo") String skuNo,
                       @Param("productName") String productName);
}
