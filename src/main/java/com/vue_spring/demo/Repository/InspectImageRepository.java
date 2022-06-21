package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.InspectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = false)
public interface InspectImageRepository extends JpaRepository<InspectImage, Long> {


}
