package com.vue_spring.demo.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vue_spring.demo.DAO.InspectExcelInterfaceDAO;
import com.vue_spring.demo.DAO.ResponseDAO;
import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Product;
import com.vue_spring.demo.service.InspectServiceImpl;
import com.vue_spring.demo.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping("inspect")
public class InspectController {

        @Autowired
        private InspectServiceImpl inspectServiceImpl;
        @Autowired
        private ProductServiceImpl productServiceImpl;

        // 검수 추가하기
        @PostMapping(value = "/insertInspect", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseDto<Integer> insertInspect(@RequestPart("data") Inspect inspect,
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
                Optional<Product> prodct = productServiceImpl.findProduct(productId);

                // 제품이 있는지 확인
                if(prodct.isPresent()){
                        Product tempProduct = prodct.get();
                        // 기존에 등록된 검수 내용 있는지 확인
                        boolean checkSelectInspect = inspectServiceImpl.checkInspect(tempProduct, inspect.getInspectDate());

                        // 기존에 등록된 검수 내용 없으면 생성 및 저장
                        if(!checkSelectInspect){

                                inspect.setProduct(tempProduct);
                                System.out.println("tempProduct : " + tempProduct);

                                Boolean check = inspectServiceImpl.insertInspect(inspect, imgFiles);

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
                                                  @RequestPart(value = "image",required = false) List<MultipartFile> imgFiles,
                                                  @RequestParam(value = "imgId",required = false) List<Long> imgId
        ) throws Exception {
                System.out.println("Controller 접근됨. /updateInspect");
                System.out.println("imgFiles : " + imgFiles);
                System.out.println("productId : " + productId);

                int data = 0;

                // 제품 검색
                Optional<Product> product = productServiceImpl.findProduct(productId);

                inspect.setProduct(product.get());

                System.out.println("product : " + product.get());
                // 검수 내용이 있으면 수정 진행
                Boolean check = inspectServiceImpl.updateInspect(inspect, imgFiles, imgId);
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

                boolean check = inspectServiceImpl.deleteInspect(id);

                System.out.println("check : " + check );

                int data = 0;

                if(check) data=1;
                else data=0;

                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

        // 모든 제품 조회
        @GetMapping("/selectAllInspect")
        public ResponseDAO<List<Inspect>> selectAllInspectlist() {
                System.out.println("Controller 접근됨. /selectAllInspectlist");

                Optional<List<Inspect>> inspects = Optional.ofNullable(inspectServiceImpl.findProductAll());
                System.out.println("Service 조회 완료");
                System.out.println(inspects);

                return ResponseDAO.<List<Inspect>>builder()
                                .data(inspects)
                                .build();
        }
//
        // 검수 데이터 excel 다운
        @GetMapping("/selectInspectExcel")
        public ResponseDAO<List<InspectExcelInterfaceDAO>> selectInspectExcel(
                @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
                @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
                @RequestParam(value = "brandName", required = false, defaultValue = "") String brandName,
                @RequestParam(value = "maker", required = false, defaultValue = "") String maker,
                @RequestParam(value = "className", required = false, defaultValue = "") List<String> className,
                @RequestParam(value = "beforeDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date beforeDate,
                @RequestParam(value = "afterDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date afterDate,
                @RequestParam(value = "page", defaultValue = "") String page
                ) {

                List<String> tempClass = new ArrayList<>();

                System.out.println("Controller 접근됨. /selectInspects");
                System.out.println("skuNo : " + skuNo + ", productName : " + productName + ", brandName : " + brandName + ", maker : " + maker + ", beforeDate : " +
                        beforeDate + ", afterDate : " + afterDate);

//                Optional<List<Inspect>> inspect = Optional.empty()

                long start = System.currentTimeMillis();
                System.out.println("@@@ Service 시작");

                Optional<List<InspectExcelInterfaceDAO>> inspectExcelInterfaceDAO = Optional.empty();



                // 제품 조회 조건이 없을 경우 날짜만 조회 진행
                if (skuNo.isEmpty() && productName.isEmpty() && brandName.isEmpty() && maker.isEmpty() && className.isEmpty()) {
                        inspectExcelInterfaceDAO = inspectServiceImpl.findInspectAllDate(beforeDate, afterDate);
                }
                // 제품 조회 조건이 있을 경우
                else {
                        // 분류 디코더 진행
                        for (int i = 0; i < className.size(); i++) {
                                try {
                                        System.out.println("selectChk : " + URLDecoder.decode(className.get(i), "UTF-8"));
                                        tempClass.add(URLDecoder.decode(className.get(i), "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                }
                        }
                        Set<String> tempClassName = new HashSet<>(tempClass);

                        System.out.println("tempSelectChk : " + tempClassName);

                        System.out.println("조건에 맞춰서 제품 조회");
                        // 조건에 맞춰서 제품 id 조회
                        List<Long> products = productServiceImpl.findAllProductId(skuNo, productName,
                                brandName, maker, tempClassName);


                        inspectExcelInterfaceDAO = inspectServiceImpl.findInspectAllDate(products, beforeDate, afterDate);

                }
                return ResponseDAO.<List<InspectExcelInterfaceDAO>>builder()
                        .data(inspectExcelInterfaceDAO)
                        .build();
        }

        @GetMapping("/selectInspects")
        public ResponseDAO<List<Inspect>> selectInspect(
                @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
                @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
                @RequestParam(value = "brandName", required = false, defaultValue = "") String brandName,
                @RequestParam(value = "maker", required = false, defaultValue = "") String maker,
                @RequestParam(value = "className", required = false, defaultValue = "") List<String> className,
                @RequestParam(value = "beforeDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date beforeDate,
                @RequestParam(value = "afterDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date afterDate,
                @RequestParam(value = "page", defaultValue = "") String page
        ) {

                List<String> tempClass = new ArrayList<>();

                System.out.println("Controller 접근됨. /selectInspects");
                System.out.println("skuNo : " + skuNo + ", productName : " + productName + ", brandName : " + brandName + ", maker : " + maker + ", beforeDate : " +
                        beforeDate + ", afterDate : " + afterDate);

//                Optional<List<Inspect>> inspect = Optional.empty()

                long start = System.currentTimeMillis();
                System.out.println("@@@ Service 시작");

                // 제품의 총 개수
                int inspectSelectCnt = 0;
                // 15개씩 자르기 위한 변수
                int cnt = 0;
                // 조회된 데이터의 길이
                int len = 0;
                // 조회된 데이터의 15개씩 자르고 남은 데이터 저장용
                int last = 0;

                Optional<List<Inspect>> inspect = Optional.empty();
                List<Long> listId = new ArrayList<>();
                List<List<Long>> pageListId = new ArrayList<>();


                // 제품 조회 조건이 없을 경우 날짜만 조회 진행
                if (skuNo.isEmpty() && productName.isEmpty() && brandName.isEmpty() && maker.isEmpty() && className.isEmpty()) {

                        // 15개만 조회
                        inspect = inspectServiceImpl.findInspect(beforeDate, afterDate);

                        // id들 모두 조회
                        List<Long> tmpId = new ArrayList<>();
                        tmpId = inspectServiceImpl.findSelectId(beforeDate, afterDate);
                        // 처음 조회 시 진행(총 검색 수량 확인. vue의 페이지 처리를 위함)
                        inspectSelectCnt = tmpId.size();

                        len = tmpId.size();
                        last = len - 1;

                        // 15개씩 정리해서 데이터를 넘김
                        for (int i = 0; i < len; i++) {
                                if (cnt != 15) {
                                        cnt++;
                                        listId.add(tmpId.get(i));
                                } else {
                                        pageListId.add(listId);
                                        listId = new ArrayList<>();
                                        i--;
                                        cnt = 0;
                                }

                                if (i == last) {
                                        pageListId.add(listId);
                                }
                        }
                }
                // 제품 조회 조건이 있을 경우
                else {
                        // 분류 디코더 진행
                        for (int i = 0; i < className.size(); i++) {
                                try {
                                        System.out.println("selectChk : " + URLDecoder.decode(className.get(i), "UTF-8"));
                                        tempClass.add(URLDecoder.decode(className.get(i), "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                }
                        }
                        Set<String> tempClassName = new HashSet<>(tempClass);

                        System.out.println("tempSelectChk : " + tempClassName);

                        System.out.println("조건에 맞춰서 제품 조회");
                        // 조건에 맞춰서 제품 id 조회
                        List<Long> products = productServiceImpl.findAllProductId(skuNo, productName,
                                brandName, maker, tempClassName);


                        System.out.println("제품 조회 완료");

                        // id 데이터들을 뽑아옴
                        List<Long> tmpId = inspectServiceImpl.findInspect(products, beforeDate, afterDate);

                        System.out.println("Service 조회 완료");

                        inspectSelectCnt = tmpId.size();

                        len = tmpId.size();
                        last = len - 1;

                        // 15개씩 정리해서 데이터를 넘김
                        for (int i = 0; i < len; i++) {
                                if (cnt != 15) {
                                        cnt++;
                                        listId.add(tmpId.get(i));
                                } else {
                                        pageListId.add(listId);
                                        listId = new ArrayList<>();
                                        i--;
                                        cnt = 0;
                                }

                                if (i == last) {
                                        pageListId.add(listId);
                                }
                        }

                        Long[] result = new Long[pageListId.get(0).size()];
                        for (int i = 0; i < pageListId.get(0).size(); i++) {
                                result[i] = pageListId.get(0).get(i);
                        }

                        // 첫 번째 페이지 조회
                        inspect = inspectServiceImpl.findInspect(result);

                }
                return ResponseDAO.<List<Inspect>>builder()
                        .data(inspect)
                        .selectCnt(inspectSelectCnt)
                        .idList(pageListId)
                        .build();

        }
        //  검수 조회
        @GetMapping("/selectPageInspect")
        public ResponseDAO<List<Inspect>> selectPageInspect(
                @RequestParam(value = "inspectCurseId", defaultValue = "0") Long[] inspectCurseId) {

                Optional<List<Inspect>> inspect = inspectServiceImpl.findInspect(inspectCurseId);

                return ResponseDAO.<List<Inspect>>builder()
                        .data(inspect)
                        .build();
        }
}
