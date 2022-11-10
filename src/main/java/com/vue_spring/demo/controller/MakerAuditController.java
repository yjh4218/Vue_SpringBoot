package com.vue_spring.demo.controller;

import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.model.MakerAudit;
import com.vue_spring.demo.service.MakerAuditServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("makerAudit")
public class MakerAuditController {

    private MakerAuditServiceImpl makerAuditServiceImpl;

    @Autowired
    public void setMakerAuditServiceImpl(MakerAuditServiceImpl makerAuditServiceImpl) {
        this.makerAuditServiceImpl = makerAuditServiceImpl;
    }

    // 제조사 점검 추가하기
    @PostMapping("/insertMakerAudit")
    public ResponseDto<Integer> insertMakerAudit(@RequestPart("data") MakerAudit makerAudit,
                                                 @RequestPart(value = "file",required = false) List<MultipartFile> fileData,
                                                 @RequestPart("makerId") Long makerId) {
        log.info("Controller 접근됨. /insertMakerAudit");
        log.info("makerAudit : ", makerAudit);

        boolean check = makerAuditServiceImpl.insertMakerAudit(makerAudit, fileData, makerId);

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }

    // 제조사 점검 수정하기
    @PutMapping(value = "/updateMakerAudit",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<Integer> updateMakerAudit(@RequestPart("data") MakerAudit makerAudit,
                                              @RequestPart(value = "file",required = false) List<MultipartFile> fileData,
                                              @RequestPart("makerId") Long makerId,
                                              @RequestParam(value = "fileId",required = false) List<Long> fileId
    ) throws Exception {
        log.info("Controller 접근됨. /updateMakerAudit");

        int data = 0;

        // 검수 내용이 있으면 수정 진행
        Boolean check = makerAuditServiceImpl.updateMakerAudit(makerAudit, fileData, makerId, fileId);
        log.info("check : " + check);

        // 저장 성공
        if (check) data = 1;
            // 저장 실패
        else data = 0;
        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }
////
    // 제조사 삭제하기
    @DeleteMapping("/deleteMakerAudit")
    public ResponseDto<Integer> deleteMakerAudit(@RequestParam(value = "id", required = false, defaultValue = "") long id) {
        log.info("Controller 접근됨. /deleteMakerAudit");
        log.info("id : " + id);

        boolean check = makerAuditServiceImpl.deleteMakerAudit(id);

        log.info("check : " + check );

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }
}
