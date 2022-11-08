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
        System.out.println("Controller 접근됨. /insertMakerAudit");
        System.out.println(makerAudit);

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
        System.out.println("Controller 접근됨. /deleteMakerAudit");
        System.out.println(id);

        boolean check = makerAuditServiceImpl.deleteMakerAudit(id);

        System.out.println("check : " + check );

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }
//
//    // 제조사 조회
//    @GetMapping("/selectMakers")
//    public MakerDAO<List<Maker>> selectProductlist(
//            @RequestParam(value = "makerName", required = false, defaultValue = "") String makerName,
//            @RequestParam(value = "makerAddress", required = false, defaultValue = "") String makerAddress,
//            @RequestParam(value = "makerPerson", required = false, defaultValue = "") String makerPerson,
//            @RequestParam(value = "makerPhone", required = false, defaultValue = "") String makerPhone,
//            @RequestParam(value = "className", required = false, defaultValue = "") List<String> className,
//            @RequestParam(value = "newProduct", required = false, defaultValue = "") String newProduct) {
//
//        List<String> tempType = new ArrayList<>();
//
//        System.out.println("Controller 접근됨. /selectMakers");
//        System.out.println("makerName : " + makerName + ", makerAddress : " + makerAddress +
//                ", makerPerson : " + makerPerson + ", makerPhone : " + makerPhone );
//
//
//        for(int i = 0; i< className.size(); i++){
//            try {
//                System.out.println("selectChk : " + URLDecoder.decode(className.get(i), "UTF-8"));
//                tempType.add(URLDecoder.decode(className.get(i), "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println("tempType : " + tempType );
//
//        Set<String> tempClassName = new HashSet<>(tempType);
//        System.out.println("tempSelectChk : " + tempClassName );
//        Optional<List<Maker>> makerList = (Optional<List<Maker>>) makerServicImpl.findMaker(makerName,
//                makerAddress, makerPerson, makerPhone, tempClassName);
//        System.out.println("Service 조회 완료");
//
//        log.info("newProduct : {}", newProduct);
//
//        if(newProduct.equals("product")){
//            return MakerDAO.<List<Maker>>builder()
//                    .data(makerList)
//                    .message("product")
//                    .build();
//        } else{
//            return MakerDAO.<List<Maker>>builder()
//                    .data(makerList)
//                    .build();
//        }
//
//    }
//
//
//    // 제품 변경 리플 수정하기
//    @PutMapping("/updateMakerReply")
//    public ResponseDto<Integer> updateProductReply(@RequestBody ReplyDTO productReplyDTO) throws Exception {
//        System.out.println("Controller 접근됨. /updateProductReply");
//        System.out.println(productReplyDTO);
//
//        int data = 0;
//
//        boolean check = makerServicImpl.updateMakerReply(productReplyDTO);
//
//        if (check) data = 1;
//        else data = 0;
//
//        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
//    }
//
//    // 제품 삭제하기
//    @DeleteMapping("/deleteMakerReply")
//    public ResponseDto<Integer> deleteProductReply(@RequestParam(value = "makerId", defaultValue = "") Long makerId,
//                                                   @RequestParam(value = "makerReplyId", defaultValue = "") Long[] makerReplyId) throws Exception {
//        log.info("Controller 접근됨. /deleteProductReply");
//
//        boolean check = makerServicImpl.deleteMakerReply(makerId, makerReplyId);
//
//        log.info("check : " + check );
//
//        int data = 0;
//
//        if(check) data=1;
//        else data=0;
//
//        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
//    }


}
