package com.vue_spring.demo.controller;

import com.vue_spring.demo.DAO.MakerDAO;
import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.service.MakerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping("maker")
public class MakerController {
    @Autowired
    private MakerServiceImpl makerServicImpl;

    // 제조사 추가하기
    @PostMapping("/insertMaker")
    public ResponseDto<Integer> insertMaker(@RequestBody Maker maker) {
        System.out.println("Controller 접근됨. /insertMaker");
        System.out.println(maker);

        boolean check = makerServicImpl.insertMaker(maker);

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }
//
    // 제조사 수정하기
    @PutMapping("/updateMaker")
    public ResponseDto<Integer> updateMaker(@RequestBody Maker maker) {
        System.out.println("Controller 접근됨. /updateMaker");
        System.out.println(maker.getId());

        boolean check = makerServicImpl.updateMaker(maker);

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }
//
    // 제조사 삭제하기
    @DeleteMapping("/deleteMaker")
    public ResponseDto<Integer> deleteMaker(@RequestParam(value = "id", required = false, defaultValue = "") long id) {
        System.out.println("Controller 접근됨. /deleteMaker");
        System.out.println(id);

        boolean check = makerServicImpl.deleteMaker(id);

        System.out.println("check : " + check );

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
            @RequestParam(value = "businessType", required = false, defaultValue = "") List<String> businessType) {

        List<String> tempType = new ArrayList<>();

        System.out.println("Controller 접근됨. /selectMakers");
        System.out.println("makerName : " + makerName + ", makerAddress : " + makerAddress +
                ", makerPerson : " + makerPerson + ", makerPhone : " + makerPhone );


        for(int i = 0; i< businessType.size(); i++){
            try {
                System.out.println("selectChk : " + URLDecoder.decode(businessType.get(i), "UTF-8"));
                tempType.add(URLDecoder.decode(businessType.get(i), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        System.out.println("tempType : " + tempType );

        Set<String> tempBusinessType = new HashSet<>(tempType);
        System.out.println("tempSelectChk : " + tempBusinessType );
        Optional<List<Maker>> products = (Optional<List<Maker>>) makerServicImpl.findMaker(makerName,
                makerAddress, makerPerson, makerPhone, tempBusinessType);
        System.out.println("Service 조회 완료");
        // System.out.println(products);
        return MakerDAO.<List<Maker>>builder()
                .data(products)
                .build();
    }

}
