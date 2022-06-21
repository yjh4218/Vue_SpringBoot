package com.vue_spring.demo.controller;

import com.vue_spring.demo.DAO.InspectDAO;
import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Product;
import com.vue_spring.demo.service.InspectServicrImpl;
import com.vue_spring.demo.service.ProductServicrImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                @RequestPart("skuNo") String skuNo,
                @RequestPart(value = "image",required = false) List<MultipartFile> imgFiles
        ) throws Exception {
                // @RequestParam(value = "skuNo") MultipartFile imgFiles,
                System.out.println("Controller 접근됨. /insertInspect");
                System.out.println(inspect);
                System.out.println("imgFiles : " + imgFiles);

                String tempSkuNo = skuNo.replaceAll("\"", "");
//                String tempSkuNo = "";

                int data = 0;

                // product 에서 제품 검색
                Product prodct = productServicrImpl.findSkuNo(tempSkuNo);
                // 기존에 등록된 검수 내용 있는지 확인
                boolean checkSelectInspect = inspectServicrImpl.checkInspect(prodct, inspect.getInspectDate());
                
                // 기존에 등록된 검수 내용 없으면 생성 및 저장
                if(!checkSelectInspect){

                        inspect.setProduct(prodct);
                        System.out.println("prodct : " + prodct);

                        Boolean check = inspectServicrImpl.insertInspect(inspect, imgFiles);

                        System.out.println("check : " + check);

                        // 저장 성공
                        if(check) data=1;
                        // 저장 실패
                        else data=0;
                } 
                // 기존에 등록된 검수 내용 있으면 생성 불가.
                else{
                        data = 2;
                }

                System.out.println("data : " + data);
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

//        // 제품 수정하기
//        @PutMapping("/updateProduct")
//        public ResponseDto<Integer> updateProduct(@RequestBody Product product) {
//                System.out.println("Controller 접근됨. /updateProduct");
//                System.out.println(product);
//
//                boolean check = productServicrImpl.updateProduct(product);
//
//                int data = 0;
//
//                if(check) data=1;
//                else data=0;
//
//                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
//        }
//
//        // 제품 삭제하기
//        @DeleteMapping("/deleteProduct")
//        public ResponseDto<Integer> deleteProduct(@RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo) {
//                System.out.println("Controller 접근됨. /deleteProduct");
//                System.out.println(skuNo);
//
//                boolean check = productServicrImpl.deleteProduct(skuNo);
//
//                System.out.println("check : " + check );
//
//                int data = 0;
//
//                if(check) data=1;
//                else data=0;
//
//                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
//        }
//
//        // 제품 중복 조회
//        @GetMapping("/checkProduct")
//        public ResponseDto<Integer> checkProduct(@RequestParam(value = "skuNo", required = true, defaultValue = "") String skuNo) {
//                System.out.println("Controller 접근됨. /checkProduct");
//                System.out.println(skuNo);
//
//                boolean check = productServicrImpl.checkSkuNo(skuNo);
//
//                System.out.println(check);
//                int data = 0;
//
//                if(check) data=1;
//
//                else data=0;
//
//                // 데이터 존재할 경우 1 전달, 없을 경우 0 전달.
//                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
//        }
//
        // 모든 제품 조회
        @GetMapping("/selectAllInspect")
        public InspectDAO<List<Inspect>> selectAllProductlist() {
                System.out.println("Controller 접근됨. /selectAllProducts");

                Optional<List<Inspect>> inspects = Optional.ofNullable(inspectServicrImpl.findProductAll());
                System.out.println("Service 조회 완료");
                System.out.println(inspects);
                for(int i =0; i<inspects.get().size(); i++){
                        System.out.println(inspects.get().get(i).getProduct().getSkuNo());
                }

                // System.out.println(products);
                return InspectDAO.<List<Inspect>>builder()
                                .data(inspects)
                                .build();
        }
//
//        // 일부 제품 조회
//        @GetMapping("/selectProducts")
//        public ProductDAO<List<Product>> selectProductlist(
//                @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
//                @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
//                @RequestParam(value = "brandName", required = false, defaultValue = "") String brandName,
//                @RequestParam(value = "maker", required = false, defaultValue = "") String maker,
//                @RequestParam(value = "className", required = false, defaultValue = "") List<String> className) {
//
//                List<String> tempClass = new ArrayList<>();
//
//                System.out.println("Controller 접근됨. /selectProducts");
//                System.out.println("skuNo : " + skuNo + ", productName : " + productName + ", brandName : " + brandName + ", maker : " + maker );
//
//
//                for(int i = 0; i< className.size(); i++){
//                        try {
//                                System.out.println("selectChk : " + URLDecoder.decode(className.get(i), "UTF-8"));
//                                tempClass.add(URLDecoder.decode(className.get(i), "UTF-8"));
//                        } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                        }
//                }
//
//                System.out.println("tempChk : " + tempClass );
//
//                Set<String> tempClassName = new HashSet<>(tempClass);
//                System.out.println("tempSelectChk : " + tempClassName );
//                Optional<List<Product>> products = (Optional<List<Product>>) productServicrImpl.findProduct(skuNo,
//                                productName,
//                                brandName, maker, tempClassName);
//                System.out.println("Service 조회 완료");
//                // System.out.println(products);
//                return ProductDAO.<List<Product>>builder()
//                                .data(products)
//                                .build();
//        }
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
