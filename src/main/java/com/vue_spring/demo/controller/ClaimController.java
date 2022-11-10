package com.vue_spring.demo.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vue_spring.demo.DAO.ClaimDAO;
import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.model.Claim;
import com.vue_spring.demo.model.Product;
import com.vue_spring.demo.service.ClaimServiceImpl;
import com.vue_spring.demo.service.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("claim")
public class ClaimController {

        @Autowired
        private ClaimServiceImpl claimServiceImpl;
        @Autowired
        private ProductServiceImpl productServicrImpl;

        // 클레임 추가하기
        @PostMapping(value = "/insertClaim", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseDto<Integer> insertClaim(@RequestPart("data") Claim claim,
                @RequestPart("productId") long productId,
                @RequestPart(value = "image",required = false) List<MultipartFile> imgFiles
        ) throws Exception {
                // @RequestParam(value = "skuNo") MultipartFile imgFiles,
                log.info("Controller 접근됨. /insertClaim");
                log.info("claims : "+ claim);
                log.info("imgFiles : " + imgFiles);
                log.info("productId : " + productId);

                int data = 0;

                // product 에서 제품 검색
                Optional<Product> prodct = productServicrImpl.findProduct(productId);

                // 제품이 있는지 확인
                if(prodct.isPresent()){
                        Product tempProduct = prodct.get();

                        claim.setProduct(tempProduct);
                        log.info("tempProduct : " + tempProduct);

                        Boolean check = claimServiceImpl.insertClaim(claim, imgFiles);

                        log.info("check : " + check);

                        // 저장 성공
                        if(check) data=1;

                        // 저장 실패
                        else data=0;
                }

                log.info("data : " + data);
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

//         검수 수정하기
        @PutMapping(value = "/updateClaim",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseDto<Integer> updateClaim(@RequestPart("data") Claim claim,
                                                  @RequestPart("productId") long productId,
                                                  @RequestPart(value = "image",required = false) List<MultipartFile> imgFiles,
                                                @RequestParam(value = "imgId",required = false) List<Long> imgId
        ) throws Exception {
                log.info("Controller 접근됨. /updateClaim");
                log.info(claim.getNote());
                log.info("imgFiles : " + imgFiles);
                log.info("productId : " + productId);

                int data = 0;

                // 제품 검색
                Optional<Product> product = productServicrImpl.findProduct(productId);

                claim.setProduct(product.get());

                log.info("product : " + product.get());
                // 검수 내용이 있으면 수정 진행
                Boolean check = claimServiceImpl.updateClaim(claim, imgFiles, imgId);
                log.info("check : " + check);

                // 저장 성공
                if (check) data = 1;
                // 저장 실패
                else data = 0;
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

        // 검수 삭제하기
        @DeleteMapping("/deleteClaim")
        public ResponseDto<Integer> deleteClaim(@RequestParam(value = "id", defaultValue = "") long id) {
                log.info("Controller 접근됨. /deleteClaim");
                log.info("id : {}",id);

                boolean check = claimServiceImpl.deleteClaim(id);

                log.info("check : " + check );

                int data = 0;

                if(check) data=1;
                else data=0;

                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

        // 모든 제품 조회
        @GetMapping("/selectAllClaim")
        public ClaimDAO<List<Claim>> selectAllClaim() {
                log.info("Controller 접근됨. /selectAllClaim");

                Optional<List<Claim>> claims = Optional.ofNullable(claimServiceImpl.findProductAll());
                log.info("Service 조회 완료");
                log.info("claims : {}",claims);

                return ClaimDAO.<List<Claim>>builder()
                                .data(claims)
                                .build();
        }
//
        // 일부 검수 조회
        @GetMapping("/selectClaims")
        public ClaimDAO<List<Claim>> selectClaims(
                @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
                @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
                @RequestParam(value = "brandName", required = false, defaultValue = "") String brandName,
                @RequestParam(value = "maker", required = false, defaultValue = "") String maker,
                @RequestParam(value = "className", required = false, defaultValue = "") List<String> className,
                @RequestParam(value = "beforeDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date beforeDate,
                @RequestParam(value = "afterDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date afterDate
                ) {

                List<String> tempClass = new ArrayList<>();

                log.info("Controller 접근됨. /selectClaims");
                log.info("skuNo : " + skuNo + ", productName : " + productName + ", brandName : " + brandName + ", maker : " + maker + ", beforeDate : " +
                        beforeDate + ", afterDate : " + afterDate);

                // 제품 조회 조건이 없을 경우 날짜만 조회 진행
                if(skuNo.isEmpty() && productName.isEmpty() && brandName.isEmpty() && maker.isEmpty() && className.isEmpty()){
                        Optional<List<Claim>> claim = claimServiceImpl.findClaim(beforeDate, afterDate);
                        // log.info(products);
                        return ClaimDAO.<List<Claim>>builder()
                                .data(claim)
                                .build();
                }
                // 제품 조회 조건이 있을 경우
                else{
                        // 분류 디코더 진행
                        for(int i = 0; i< className.size(); i++){
                                try {
                                        log.info("selectChk : " + URLDecoder.decode(className.get(i), "UTF-8"));
                                        tempClass.add(URLDecoder.decode(className.get(i), "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                }
                        }
                        Set<String> tempClassName = new HashSet<>(tempClass);

                        log.info("tempSelectChk : " + tempClassName );

                        log.info("조건에 맞춰서 제품 조회");
                        // 조건에 맞춰서 제품 조회
                       List<Long> products = productServicrImpl.findAllProductId(skuNo, productName,
                                brandName, maker, tempClassName);

                        Optional<List<Claim>> claims = claimServiceImpl.findClaim(products, beforeDate, afterDate);
                        log.info("Service 조회 완료");

                        // log.info(products);
                        return ClaimDAO.<List<Claim>>builder()
                                .data(claims)
                                .build();
                }
        }
}
