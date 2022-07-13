package com.vue_spring.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import com.vue_spring.demo.DAO.ProductDAO;
import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.model.Product;
import com.vue_spring.demo.service.ProductServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("product")
public class ProductController {

        @Autowired
        private ProductServiceImpl productServicrImpl;

//        // 제품 추가하기
//        @PostMapping("/insertProduct")
//        public ResponseDto<Integer> insertProduct(@RequestBody Product product) {
//                System.out.println("Controller 접근됨. /insertProduct");
//                System.out.println(product);
//
//                boolean check = productServicrImpl.insertProduct(product);
//
//                int data = 0;
//
//                if(check) data=1;
//                else data=0;
//
//                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
//        }

        // 제품 추가하기
        @PostMapping(value = "/insertProduct", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseDto<Integer> insertProduct(@RequestPart("data") Product product,
                                                  @RequestPart(value = "image",required = false) List<MultipartFile> imgFiles
        ) throws Exception {
                // @RequestParam(value = "skuNo") MultipartFile imgFiles,
                System.out.println("Controller 접근됨. /insertInspect");
                System.out.println(product);
                System.out.println("imgFiles : " + imgFiles);

                int data = 0;

                Boolean check = productServicrImpl.insertProduct(product, imgFiles);

                System.out.println("check : " + check);

                // 저장 성공
                if(check) data=1;
                // 저장 실패
                else data=0;

                System.out.println("data : " + data);
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }


        // 제품 수정하기
//        @PutMapping("/updateProduct")
//        public ResponseDto<Integer> updateProduct(@RequestBody Product product) {
//                System.out.println("Controller 접근됨. /updateProduct");
//                System.out.println(product.getId());
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
        // 제품 수정하기
        @PutMapping(value = "/updateProduct",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseDto<Integer> updateProduct(@RequestPart("data") Product product,
                                                  @RequestPart("productId") long productId,
                                                  @RequestPart(value = "image",required = false) List<MultipartFile> imgFiles
        ) throws Exception {
                System.out.println("Controller 접근됨. /updateInspect");
                System.out.println("imgFiles : " + imgFiles);
                System.out.println("productId : " + productId);

                int data = 0;

                // 검수 내용이 있으면 수정 진행
                Boolean check = productServicrImpl.updateProduct(product, imgFiles);
                System.out.println("check : " + check);

                // 저장 성공
                if (check) data = 1;
                        // 저장 실패
                else data = 0;
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }


        // 제품 삭제하기
        @DeleteMapping("/deleteProduct")
        public ResponseDto<Integer> deleteProduct(@RequestParam(value = "id", required = false, defaultValue = "") long id,
                                                  @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo) {
                System.out.println("Controller 접근됨. /deleteProduct");
                System.out.println(skuNo);

                boolean check = productServicrImpl.deleteProduct(id);

                System.out.println("check : " + check );

                int data = 0;

                if(check) data=1;
                else data=0;

                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

        // 제품 중복 조회
        @GetMapping("/checkProduct")
        public ResponseDto<Integer> checkProduct(@RequestParam(value = "skuNo", required = true, defaultValue = "") String skuNo) {
                System.out.println("Controller 접근됨. /checkProduct");
                System.out.println(skuNo);

                boolean check = productServicrImpl.checkSkuNo(skuNo);

                System.out.println(check);
                int data = 0;

                if(check) data=1;

                else data=0;

                // 데이터 존재할 경우 1 전달, 없을 경우 0 전달.
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

//        // 모든 제품 조회
//        @GetMapping("/selectAllProducts")
//        public ProductDAO<List<Product>> selectAllProductlist() {
//                System.out.println("Controller 접근됨. /selectAllProducts");
//                Optional<List<Product>> products = Optional.ofNullable(productServicrImpl.findProductAll());
//                System.out.println("Service 조회 완료");
//                // System.out.println(products);
//                return ProductDAO.<List<Product>>builder()
//                                .data(products)
//                                .build();
//        }

        // 일부 제품 조회
        @GetMapping("/selectProducts")
        public ProductDAO<List<Product>> selectProductlist(
                @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
                @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
                @RequestParam(value = "brandName", required = false, defaultValue = "") String brandName,
                @RequestParam(value = "maker", required = false, defaultValue = "") String maker,
                @RequestParam(value = "className", required = false, defaultValue = "") List<String> className) {

                List<String> tempClass = new ArrayList<>();

                System.out.println("Controller 접근됨. /selectProducts");
                System.out.println("skuNo : " + skuNo + ", productName : " + productName + ", brandName : " + brandName + ", maker : " + maker );


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
                Optional<List<Product>> products = (Optional<List<Product>>) productServicrImpl.findProduct(skuNo,
                                productName,
                                brandName, maker, tempClassName);
                System.out.println("Service 조회 완료");
                // System.out.println(products);
                return ProductDAO.<List<Product>>builder()
                                .data(products)
                                .build();
        }
}
