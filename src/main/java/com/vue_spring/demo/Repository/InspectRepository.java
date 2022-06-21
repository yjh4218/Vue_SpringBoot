package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = false)
public interface InspectRepository extends JpaRepository<Inspect, Long> {

    Inspect findById(String id);

    Boolean existsByProductAndInspectDate(Product product, String inspectDate);
}
