package com.vue_spring.demo.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vue_spring.demo.DAO.InspectDAO;
import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Product;
import com.vue_spring.demo.service.InspectServicrImpl;
import com.vue_spring.demo.service.ProductServicrImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping("inspect")
public class InspectController {

        @Autowired
        private InspectServicrImpl inspectServicrImpl;
        @Autowired
        private ProductServicrImpl productServicrImpl;

        // 제품 추가하기
        @PostMapping(value = "/insertInspect", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseDto<Integer> insertProduct(@RequestPart("data") Inspect inspect,
                @RequestPart("productId") long productId,
                @RequestPart(value = "image",required = false) List<MultipartFile> imgFiles
        ) throws Exception {
                // @RequestParam(value = "skuNo") MultipartFile imgFiles,
                System.out.println("Controller 접근됨. /insertInspect");
                System.out.println(inspect);
                System.out.println("imgFiles : " + imgFiles);
                System.out.println("productId : " + productId);

                int data = 0;

                // product 에서 제품 검색
                Optional<Product> prodct = productServicrImpl.findProduct(productId);

                // 제품이 있는지 확인
                if(prodct.isPresent()){
                        Product tempProduct = prodct.get();
                        // 기존에 등록된 검수 내용 있는지 확인
                        boolean checkSelectInspect = inspectServicrImpl.checkInspect(tempProduct, inspect.getInspectDate());

                        // 기존에 등록된 검수 내용 없으면 생성 및 저장
                        if(!checkSelectInspect){

                                inspect.setProduct(tempProduct);
                                System.out.println("tempProduct : " + tempProduct);

                                Boolean check = inspectServicrImpl.insertInspect(inspect, imgFiles);

                                System.out.println("check : " + check);

                                // 저장 성공
                                if(check) data=1;

                                // 저장 실패
                                else data=0;
                        }
                        // 기존에 등록된 검수 내용 있으면 생성 불가.
                        else {
                                data = 2;
                        }
                }

                System.out.println("data : " + data);
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

//         검수 수정하기
        @PutMapping(value = "/updateInspect",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseDto<Integer> updateInspect(@RequestPart("data") Inspect inspect,
                                                  @RequestPart("productId") long productId,
                                                  @RequestPart(value = "image",required = false) List<MultipartFile> imgFiles
        ) throws Exception {
                System.out.println("Controller 접근됨. /updateInspect");
                System.out.println(inspect.getNote());
                System.out.println("imgFiles : " + imgFiles);
                System.out.println("productId : " + productId);

                int data = 0;

                // 제품 검색
                Optional<Product> product = productServicrImpl.findProduct(productId);

                inspect.setProduct(product.get());

                System.out.println("product : " + product.get());
                // 검수 내용이 있으면 수정 진행
                Boolean check = inspectServicrImpl.updateInspect(inspect, imgFiles);
                System.out.println("check : " + check);

                // 저장 성공
                if (check) data = 1;
                // 저장 실패
                else data = 0;
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }


        // 검수 삭제하기
        @DeleteMapping("/deleteInspect")
        public ResponseDto<Integer> deleteInspect(@RequestParam(value = "id", defaultValue = "") long id) {
                System.out.println("Controller 접근됨. /deleteInspect");
                System.out.println(id);

                boolean check = inspectServicrImpl.deleteInspect(id);

                System.out.println("check : " + check );

                int data = 0;

                if(check) data=1;
                else data=0;

                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

        // 모든 제품 조회
        @GetMapping("/selectAllInspect")
        public InspectDAO<List<Inspect>> selectAllInspectlist() {
                System.out.println("Controller 접근됨. /selectAllInspectlist");

                Optional<List<Inspect>> inspects = Optional.ofNullable(inspectServicrImpl.findProductAll());
                System.out.println("Service 조회 완료");
                System.out.println(inspects);

                return InspectDAO.<List<Inspect>>builder()
                                .data(inspects)
                                .build();
        }
//
        // 일부 검수 조회
        @GetMapping("/selectInspects")
        public InspectDAO<List<Inspect>> selectInspectist(
                @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
                @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
                @RequestParam(value = "brandName", required = false, defaultValue = "") String brandName,
                @RequestParam(value = "maker", required = false, defaultValue = "") String maker,
                @RequestParam(value = "className", required = false, defaultValue = "") List<String> className,
                @RequestParam(value = "beforeDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date beforeDate,
                @RequestParam(value = "afterDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date afterDate
                ) {

                List<String> tempClass = new ArrayList<>();

                System.out.println("Controller 접근됨. /selectInspects");
                System.out.println("skuNo : " + skuNo + ", productName : " + productName + ", brandName : " + brandName + ", maker : " + maker + ", beforeDate : " +
                        beforeDate + ", afterDate : " + afterDate);


                for(int i = 0; i< className.size(); i++){
                        try {
                                System.out.println("selectChk : " + URLDecoder.decode(className.get(i), "UTF-8"));
                                tempClass.add(URLDecoder.decode(className.get(i), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                        }
                }

                System.out.println("tempChk : " + tempClass );

                Set<String> tempClassName = new HashSet<>(tempClass);
                System.out.println("tempSelectChk : " + tempClassName );
                Optional<List<Inspect>> inspect = (Optional<List<Inspect>>) inspectServicrImpl.findInspect(skuNo,
                                productName,
                                brandName, maker, tempClassName, beforeDate, afterDate);
                System.out.println("Service 조회 완료");
                // System.out.println(products);
                return InspectDAO.<List<Inspect>>builder()
                                .data(inspect)
                                .build();
        }
//
//        // sku 1개 제품 조회
//        @GetMapping("/selectSkuNo")
//        public ProductDAO<Product> selectSkuNo(
//                @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo) {
//
//                List<String> tempChk = new ArrayList<>();
//
//                System.out.println("Controller 접근됨. /selectSkuNo");
//                System.out.println("skuNo : " + skuNo);
//                Optional<Product> product = productServicrImpl.findSkuNo(skuNo);
//                System.out.println("Service 조회 완료");
//                // System.out.println(products);
//                return ProductDAO.<Product>builder()
//                        .data(product)
//                        .build();
//        }
}
