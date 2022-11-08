package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.ProductFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = false)
public interface ProductFileRepository extends JpaRepository<ProductFile, Long> {

    void deleteByProductId(@Param("productId") long productId);

    Boolean existsByProductId(@Param("productId") long productId);

    Optional<List<ProductFile>> findByProductId(@Param("productId") long productId);

}
