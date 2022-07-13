package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.ClaimImage;
import com.vue_spring.demo.model.InspectImage;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = false)
public interface ClaimImageRepository extends JpaRepository<ClaimImage, Long> {

    void deleteByClaimId(@Param("claimId") long claimId);

    Boolean existsByClaimId(@Param("claimId") long claimId);

    Optional<List<ClaimImage>> findByClaimId(@Param("claimId") long claimId);
}
