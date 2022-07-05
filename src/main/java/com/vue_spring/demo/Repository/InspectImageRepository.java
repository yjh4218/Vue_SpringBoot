package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.InspectImage;
import com.vue_spring.demo.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = false)
public interface InspectImageRepository extends JpaRepository<InspectImage, Long> {

    void deleteByInspectId(@Param("inspectId") long inspectId);

    Boolean existsByInspectId(@Param("inspectId") long inspectId);

    Optional<List<InspectImage>> findByInspectId(@Param("inspectId") long inspectId);
}
