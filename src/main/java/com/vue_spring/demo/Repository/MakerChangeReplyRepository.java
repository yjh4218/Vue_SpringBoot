package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.MakerChangeReply;
import com.vue_spring.demo.model.ProductChangeReply;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = false)
public interface MakerChangeReplyRepository extends JpaRepository<MakerChangeReply, Long> {

    void deleteByMakerId(@Param("makerId") long makerId);

    Boolean existsByMakerId(@Param("makerId") long makerId);

    Optional<List<MakerChangeReply>> findByMakerId(@Param("makerId") long makerId);

}
