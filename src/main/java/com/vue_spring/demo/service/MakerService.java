package com.vue_spring.demo.service;

import com.vue_spring.demo.DTO.ReplyDTO;
import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface MakerService {


    public Optional<List<Maker>> findMaker(String skuNo, String productName,
                                             String brandName, String maker, Set<String> tempClassName);

    public Boolean insertMaker(Maker maker, String makerChangeContent);

    public Boolean updateMaker(Maker maker, String makerChangeContent);

    public Boolean checkId(long makerId);

    public Boolean deleteMaker(long id);

    public Boolean updateMakerReply(ReplyDTO replyDTO) throws Exception;

    public Boolean deleteMakerReply(Long makerId, Long[] makerReplyId) throws Exception;
}
