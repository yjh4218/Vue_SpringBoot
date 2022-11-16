package com.vue_spring.demo.controller;

import com.vue_spring.demo.DAO.MakerDAO;
import com.vue_spring.demo.DTO.MakerDTO;
import com.vue_spring.demo.DTO.ReplyDTO;
import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.service.MakerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("maker")
public class MakerController {
    @Autowired
    private MakerServiceImpl makerServicImpl;

    // 제조사 추가하기
    @PostMapping("/insertMaker")
    public ResponseDto<Integer> insertMaker(@RequestBody MakerDTO makerDTO) {
        log.info("Controller 접근됨. /insertMaker");
        log.info("makerDTO : " + makerDTO);

        Maker maker = Maker.builder()
                .makerName(makerDTO.getMakerName())
                .makerAddress(makerDTO.getMakerAddress())
                .className(makerDTO.getClassName())
                .process(makerDTO.getProcess())
                .importProduct(makerDTO.getImportProduct())
                .sales(makerDTO.getSales())
                .makerInfo(makerDTO.getMakerInfo())
                .makerScore(makerDTO.getMakerScore())
                .makerPerson(makerDTO.getMakerPerson())
                .makerPhone(makerDTO.getMakerPhone())
                .makerEmail(makerDTO.getMakerEmail())
                .note(makerDTO.getNote())
                .build();

        boolean check = makerServicImpl.insertMaker(maker, makerDTO.getMakerChangeContent());

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }
//
    // 제조사 수정하기
    @PutMapping("/updateMaker")
    public ResponseDto<Integer> updateMaker(@RequestBody MakerDTO makerDTO) {
        log.info("Controller 접근됨. /updateMaker");
//        log.info(maker.getId());

        Maker maker = Maker.builder()
                .id(makerDTO.getId())
                .makerName(makerDTO.getMakerName())
                .makerAddress(makerDTO.getMakerAddress())
                .className(makerDTO.getClassName())
                .process(makerDTO.getProcess())
                .importProduct(makerDTO.getImportProduct())
                .sales(makerDTO.getSales())
                .makerInfo(makerDTO.getMakerInfo())
                .makerScore(makerDTO.getMakerScore())
                .makerPerson(makerDTO.getMakerPerson())
                .makerPhone(makerDTO.getMakerPhone())
                .makerEmail(makerDTO.getMakerEmail())
                .note(makerDTO.getNote())
                .build();

        boolean check = makerServicImpl.updateMaker(maker, makerDTO.getMakerChangeContent());

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }
//
    // 제조사 삭제하기
    @DeleteMapping("/deleteMaker")
    public ResponseDto<Integer> deleteMaker(@RequestParam(value = "id", required = false, defaultValue = "") long id) {
        log.info("Controller 접근됨. /deleteMaker");
        log.info("id : " + id);

        boolean check = makerServicImpl.deleteMaker(id);

        log.info("check : " + check );

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }

    // 제조사 조회
    @GetMapping("/selectMakers")
    public MakerDAO<List<Maker>> selectProductlist(
            @RequestParam(value = "makerName", required = false, defaultValue = "") String makerName,
            @RequestParam(value = "makerAddress", required = false, defaultValue = "") String makerAddress,
            @RequestParam(value = "makerPerson", required = false, defaultValue = "") String makerPerson,
            @RequestParam(value = "makerPhone", required = false, defaultValue = "") String makerPhone,
            @RequestParam(value = "className", required = false, defaultValue = "") List<String> className,
            @RequestParam(value = "newProduct", required = false, defaultValue = "") String newProduct) {

        List<String> tempType = new ArrayList<>();

        log.info("Controller 접근됨. /selectMakers");
        log.info("makerName : " + makerName + ", makerAddress : " + makerAddress +
                ", makerPerson : " + makerPerson + ", makerPhone : " + makerPhone );


        for(int i = 0; i< className.size(); i++){
            try {
                log.info("selectChk : " + URLDecoder.decode(className.get(i), "UTF-8"));
                tempType.add(URLDecoder.decode(className.get(i), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        log.info("tempType : " + tempType );

        Set<String> tempClassName = new HashSet<>(tempType);
        log.info("tempSelectChk : " + tempClassName );
        Optional<List<Maker>> makerList = (Optional<List<Maker>>) makerServicImpl.findMaker(makerName,
                makerAddress, makerPerson, makerPhone, tempClassName);
        log.info("Service 조회 완료");

        log.info("newProduct : {}", newProduct);

        if(newProduct.equals("product")){
            return MakerDAO.<List<Maker>>builder()
                    .data(makerList)
                    .message("product")
                    .build();
        } else{
            return MakerDAO.<List<Maker>>builder()
                    .data(makerList)
                    .build();
        }

    }


    // 제품 변경 리플 수정하기
    @PutMapping("/updateMakerReply")
    public ResponseDto<Integer> updateProductReply(@RequestBody ReplyDTO productReplyDTO) throws Exception {
        log.info("Controller 접근됨. /updateProductReply");
        log.info("productReplyDTO : " + productReplyDTO);

        int data = 0;

        boolean check = makerServicImpl.updateMakerReply(productReplyDTO);

        if (check) data = 1;
        else data = 0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }

    // 제품 삭제하기
    @DeleteMapping("/deleteMakerReply")
    public ResponseDto<Integer> deleteProductReply(@RequestParam(value = "makerId", defaultValue = "") Long makerId,
                                                   @RequestParam(value = "makerReplyId", defaultValue = "") Long[] makerReplyId) throws Exception {
        log.info("Controller 접근됨. /deleteProductReply");

        boolean check = makerServicImpl.deleteMakerReply(makerId, makerReplyId);

        log.info("check : " + check );

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }


}
