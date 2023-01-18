package com.vue_spring.demo.Repository;

import com.vue_spring.demo.DAO.SalesDAO;
import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.model.Sales;
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
public interface SalesRepository extends JpaRepository<Sales, Long> {

    // 판매량 집계
    @Query(nativeQuery = true, value ="select p.sku_No as skuNo, p.product_Name as productName" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-01') then s.SALES_VOLUMN else 0 end)) as Jan" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-02') then s.SALES_VOLUMN else 0 end)) as Feb" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-03') then s.SALES_VOLUMN else 0 end)) as Mar" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-04') then s.SALES_VOLUMN else 0 end)) as Apr" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-05') then s.SALES_VOLUMN else 0 end)) as May" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-06') then s.SALES_VOLUMN else 0 end)) as Jun" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-07') then s.SALES_VOLUMN else 0 end)) as Jul" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-08') then s.SALES_VOLUMN else 0 end)) as Aug" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-09') then s.SALES_VOLUMN else 0 end)) as Sep" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-10') then s.SALES_VOLUMN else 0 end)) as Oct" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-11') then s.SALES_VOLUMN else 0 end)) as Nov" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-12') then s.SALES_VOLUMN else 0 end)) as December" +
            " from sales_product_component p left outer join sales s on p.sku_no = s.sku_No where p.sku_no like %:skuNo% and p.product_Name like %:productName% " +
            " group by s.sku_No")
    Optional<List<SalesDAO>> findBySales(
            @Param("skuNo")String skuNo,
            @Param("productName")String productName,
            @Param("date") String findDate
    );

     // 구성품 판매량 집계
        @Query(nativeQuery = true, value ="select p.component_sku_no as skuNo, p.component_product_name as productName" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-01') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Jan" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-02') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Feb" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-03') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Mar" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-04') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Apr" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-05') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as May" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-06') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Jun" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-07') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Jul" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-08') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Aug" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-09') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Sep" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-10') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Oct" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-11') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as Nov" +
            ", ROUND(SUM(case when date_format(s.sales_month, '%Y-%m')= concat(:findDate, '-12') then s.SALES_VOLUMN * p.component_quantity else 0 end)) as December" +
            " from sales_product_component p left outer join sales s on p.sku_no = s.sku_No where p.component_sku_no like %:skuNo% and p.component_product_name like %:productName% " +
            " group by p.component_sku_no")
    Optional<List<SalesDAO>> findByComponentSales(
                @Param("skuNo")String skuNo,
                @Param("productName")String productName,
                @Param("date") String findDate
        );

    @Query(nativeQuery = true, value ="SELECT * FROM SALES where sku_no like %:skuNo% and product_name like %:productName% and sales_month = :findDate")
    Optional<List<Sales>> findByMonthSales(
            @Param("skuNo")String skuNo,
            @Param("productName")String productName,
            @Param("date") String findDate
    );

    Boolean existsBySalesMonth(@Param("salesMonth") Date salesMonth);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value ="DELETE FROM SALES where id in (:listIds)")
    Integer deleteByIds(@Param("listIds") Long[] listIds);


}
