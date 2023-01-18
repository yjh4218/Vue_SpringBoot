package com.vue_spring.demo.Repository;

import com.vue_spring.demo.DAO.InspectExcelInterfaceDAO;
import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = false)
public interface InspectRepository extends JpaRepository<Inspect, Long> {

    Inspect findById(String id);

    Boolean existsByProductAndInspectDate(Product product, Date inspectDate);

    Boolean existsByProductId(Long productId);

    Boolean deleteByProductId(Long productId);

    // 검수 조회(분류 포함) - 처음 15개 조회
    @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM INSPECT WHERE inspect_date BETWEEN :beforeDate AND :afterDate order by inspect_date limit 15")
    Optional<List<Inspect>> findByInspectListAndClassNameFirst(
            @Param("beforeDate")Date beforeDate,
            @Param("afterDate") Date afterDate
    );

    // 검수 id 모두 조회 - 분류 없음
    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT DISTINCT id, inspect_date FROM INSPECT WHERE inspect_date BETWEEN :beforeDate AND :afterDate order by inspect_date")
    List<Long> findById(
            @Param("beforeDate")Date beforeDate,
            @Param("afterDate") Date afterDate
    );


    // 제품 조회(분류 제외) - 커서에 맞춰 15개 조회
    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM INSPECT WHERE id in (:inspectCurseId)")
    Optional<List<Inspect>> findByInspectCurseId(
            @Param("inspectCurseId") Long[] inspectCurseId
    );

    // product에 맞는 id 값들 전부 조회
    @Query(nativeQuery = true, value = "SELECT DISTINCT id FROM INSPECT WHERE product_id = :productId and inspect_date BETWEEN :beforeDate AND :afterDate")
    Optional<List<Long>> findByProductIdInspectList(
            @Param("productId") Long productId,
            @Param("beforeDate") Date beforeDate,
            @Param("afterDate") Date afterDate);

    Optional<List<Inspect>> findByProductId(Long productId);

    // 날짜에 맞는 모든 데이터 조회(엑셀용)
    @Query(nativeQuery = true, value = "select distinct p.sku_no as skuNo, \n" +
            "p.product_name as productName, \n" +
            "p.class_name as className, \n" +
            "i.appearance as appearance, \n" +
            "i.check_packing as checkPacking, \n" +
            "i.check_work as checkWork, \n" +
            "i.color as color, \n" +
            "i.damage as damage,\n" +
            "i.decide_result as decideResult,\n" +
            "i.finish_state as finishState,\n" +
            "i.foreign_body as foreignBody,\n" +
            "i.inspect_content as inspectContent,\n" +
            "i.inspect_date as inspectDate,\n" +
            "i.lot_date as lotDate,\n" +
            "i.moisture as moisture,\n" +
            "i.size as size,\n" +
            "i.special_report as specialReport,\n" +
            "i.usability as usability,\n" +
            "i.sensuality as sensuality,\n" +
            "i.weight as weight\n" +
            "from product p join inspect i where p.id = i.product_id and inspect_date BETWEEN :beforeDate AND :afterDate")
    Optional<List<InspectExcelInterfaceDAO>> findByInspectDateBetween(
            @Param("beforeDate")Date beforeDate,
            @Param("afterDate") Date afterDate);

    // 날짜와 product에 맞는 id 값들 전부 조회(엑셀용)
    @Query(nativeQuery = true, value = "select distinct p.sku_no as skuNo, \n" +
            "p.product_name as productName, \n" +
            "p.class_name as className, \n" +
            "i.appearance as appearance, \n" +
            "i.check_packing as checkPacking, \n" +
            "i.check_work as checkWork, \n" +
            "i.color as color, \n" +
            "i.damage as damage,\n" +
            "i.decide_result as decideResult,\n" +
            "i.finish_state as finishState,\n" +
            "i.foreign_body as foreignBody,\n" +
            "i.inspect_content as inspectContent,\n" +
            "i.inspect_date as inspectDate,\n" +
            "i.lot_date as lotDate,\n" +
            "i.moisture as moisture,\n" +
            "i.size as size,\n" +
            "i.special_report as specialReport,\n" +
            "i.usability as usability,\n" +
            "i.sensuality as sensuality,\n" +
            "i.weight as weight\n" +
            "from product p join inspect i where p.id = i.product_id and product_id in (:productId) and inspect_date BETWEEN :beforeDate AND :afterDate")
    Optional<List<InspectExcelInterfaceDAO>> findByProductInspectList(
            @Param("productId") List<Long> productId,
            @Param("beforeDate")Date beforeDate,
            @Param("afterDate") Date afterDate);

    // 직전등록 검수 조회
    @Query(nativeQuery = true, value = "select * from inspect where product_id = :productId order by product_id desc limit 1;")
    Optional<Inspect> findByInspectReg(@Param("productId") Long productId);


}
