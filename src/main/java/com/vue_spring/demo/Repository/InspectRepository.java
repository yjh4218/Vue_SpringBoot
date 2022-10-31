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
    @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM INSPECT WHERE inspect_date BETWEEN :beforeDate AND :afterDate order by id asc limit 15")
    Optional<List<Inspect>> findByInspectListAndClassNameFirst(
            @Param("beforeDate")Date beforeDate,
            @Param("afterDate") Date afterDate
    );

    // 검수 id 모두 조회 - 분류 없음
    @Transactional(readOnly = true)
    @Query(nativeQuery = true, value = "SELECT DISTINCT id FROM INSPECT WHERE inspect_date BETWEEN :beforeDate AND :afterDate")
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
    List<Long> findByProductIdInspectList(Long productId, Date beforeDate, Date afterDate);

//    @Query(nativeQuery = true, value = "SELECT id FROM INSPECT WHERE product_id = :productId")
    Optional<List<Inspect>> findByProductId(Long productId);

    // 날짜에 맞는 모든 데이터 조회(엑셀용)
    @Query(nativeQuery = true, value = "select distinct p.sku_no as skuNo, p.product_name as productName, i.decide_result as decideResult, i.inspect_content as inspectContent, i.inspect_date as inspectDate, i.lot_date as lotDate, i.moisture as moisture, i.note as note, i.special_report as specialReport from product p join inspect i where p.id = i.product_id and inspect_date BETWEEN :beforeDate AND :afterDate")
    Optional<List<InspectExcelInterfaceDAO>> findByInspectDateBetween(Date beforeDate, Date afterDate);

    // 날짜와 product에 맞는 id 값들 전부 조회(엑셀용)
    @Query(nativeQuery = true, value = "select distinct p.sku_no as skuNo, p.product_name as productName, i.decide_result as decideResult, i.inspect_content as inspectContent, i.inspect_date as inspectDate, i.lot_date as lotDate, i.moisture as moisture, i.note as note, i.special_report as specialReport from product p join inspect i where p.id = i.product_id and product_id in (:productId) and inspect_date BETWEEN :beforeDate AND :afterDate")
    Optional<List<InspectExcelInterfaceDAO>> findByProductInspectList(List<Long> productId, Date beforeDate, Date afterDate);


}
