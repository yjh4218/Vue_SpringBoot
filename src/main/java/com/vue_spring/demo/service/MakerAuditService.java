package com.vue_spring.demo.service;

import com.vue_spring.demo.DTO.ReplyDTO;
import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.model.MakerAudit;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface MakerAuditService {


//    public Optional<List<MakerAudit>> findMaker(String skuNo, String productName,
//                                                String brandName, String maker, Set<String> tempClassName);
//
        public Boolean insertMakerAudit(MakerAudit makerAudit, List<MultipartFile> fileData, Long makerId);
//
public Boolean updateMakerAudit(MakerAudit makerAudit, List<MultipartFile> fileData, Long makerId, List<Long> fileId);
//    public Boolean updateMaker(MakerAudit makerAudit, String makerChangeContent);
//
//    public Boolean checkId(long makerId);
//
//    public Boolean deleteMaker(long id);
//
//    public Boolean updateMakerReply(ReplyDTO replyDTO) throws Exception;
//
//    public Boolean deleteMakerReply(Long makerId, Long[] makerReplyId) throws Exception;
}
