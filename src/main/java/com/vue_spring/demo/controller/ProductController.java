package com.vue_spring.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import com.vue_spring.demo.DAO.ResponseDAO;
import com.vue_spring.demo.DTO.ReplyDTO;
import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.model.Product;

import com.vue_spring.demo.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

        private final ProductServiceImpl productServiceImpl;

        // 제품 추가하기
        @PostMapping(value = "/insertProduct", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseDto<Integer> insertProduct(@RequestPart("data") Product product,
                                                  @RequestPart("productChangeContent") String productChangeReply,
                                                  @RequestPart(value = "file",required = false) List<MultipartFile> fileData
        ) throws Exception {
                log.info("Controller 접근됨. /insertProduct");
                log.info(String.valueOf(product));
                productChangeReply = productChangeReply.replaceAll("\"","");
                log.info("productChangeReply : " + productChangeReply);
                log.info("imgFiles : " + fileData);

                int data = 0;

                Boolean check = productServiceImpl.insertProduct(product, fileData, productChangeReply);

                log.info("check : " + check);

                // 저장 성공
                if(check) data=1;
                // 저장 실패
                else data=0;

                log.info("data : " + data);
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

        // 제품 수정하기
        @PutMapping(value = "/updateProduct",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseDto<Integer> updateProduct(@RequestPart("data") Product product,
                                                  @RequestPart("productId") long productId,
                                                  @RequestPart("productChangeContent") String productChangeReply,
                                                  @RequestPart(value = "file",required = false) List<MultipartFile> fileData,
                                                  @RequestParam(value = "fileId",required = false) List<Long> fileId
        ) throws Exception {
                log.info("Controller 접근됨. /updateInspect");
                log.info("imgFiles : " + fileData);
                log.info("productId : " + productId);
                log.info("imgId : " + fileId);
                productChangeReply = productChangeReply.replaceAll("\"","");
                log.info("productChangeReply : " + productChangeReply);

                int data = 0;

                // 검수 내용이 있으면 수정 진행
                Boolean check = productServiceImpl.updateProduct(product, fileData, fileId, productChangeReply);
                log.info("check : " + check);

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
                log.info("Controller 접근됨. /deleteProduct");
                log.info(skuNo);

                boolean check = productServiceImpl.deleteProduct(id);

                log.info("check : " + check );

                int data = 0;

                if(check) data=1;
                else data=0;

                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

        // 제품 중복 조회
        @GetMapping("/checkProduct")
        public ResponseDto<Integer> checkProduct(@RequestParam(value = "skuNo", required = true, defaultValue = "") String skuNo) {
                log.info("Controller 접근됨. /checkProduct");
                log.info(skuNo);

                boolean check = productServiceImpl.checkSkuNo(skuNo);

                log.info("check : " + check );

                int data = 0;

                if(check) data=1;

                else data=0;

                // 데이터 존재할 경우 1 전달, 없을 경우 0 전달.
                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }


        // 일부 제품 조회. 처음 조회했을 때 진행됨.
        @GetMapping("/selectProducts")
        public ResponseDAO<List<Product>> selectProducts(
                @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
                @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
                @RequestParam(value = "brandName", required = false, defaultValue = "") String brandName,
                @RequestParam(value = "maker", required = false, defaultValue = "") String maker,
                @RequestParam(value = "className", required = false, defaultValue = "") List<String> className,
                @RequestParam(value = "operation", required = false, defaultValue = "") List<String> operation,
                @RequestParam(value = "downExcel", required = false, defaultValue = "") String downExcel) {

//                @RequestParam(value = "page", defaultValue = "") String page,
//
                List<String> tempClass = new ArrayList<>();
                List<String> tempOper = new ArrayList<>();

                log.info("Controller 접근됨. /selectProducts");
                log.info("skuNo : " + skuNo + ", productName : " + productName + ", brandName : " + brandName + ", maker : " + maker );
                log.info("downExcel : {}", downExcel);
//                log.info("page : " + page + ", productCurseId : " + productCurseId);
                // 분류 디코딩
                for(int i = 0; i< className.size(); i++){
                        try {
                                log.info("selectChk : " + URLDecoder.decode(className.get(i), "UTF-8"));
                                tempClass.add(URLDecoder.decode(className.get(i), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                        }
                }

                // 운영여부 디코딩
                for(int i = 0; i< operation.size(); i++){
                        try {
                                log.info("selectOpr : " + URLDecoder.decode(operation.get(i), "UTF-8"));
                                tempOper.add(URLDecoder.decode(operation.get(i), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                        }
                }
                log.info("tempChk : " + tempClass );
                log.info("tempOper : " + tempOper );

                long start = System.currentTimeMillis();
                log.info("@@@ Service 시작");

                Set<String> tempClassName = new HashSet<>(tempClass);
                Set<String> tempOperation = new HashSet<>(tempOper);
                log.info("tempSelectClass : " + tempClassName );
                log.info("tempSelectOper : " + tempOperation );
                log.info("downExcel : " + downExcel );

                // excel 다운 일경우
                if(downExcel.equals("excel")){

                        // excel 다운
                        List<Product> products = productServiceImpl.findProductExcel(
                                skuNo, productName,brandName, maker, tempClassName, tempOperation);

                        // log.info(products);
                        return ResponseDAO.<List<Product>>builder()
                                .data(Optional.ofNullable(products))
                                .build();
                }

                // 제품 조회 일 경우
                else{
                        // 처음 15번만 조회
                        log.info("처음 15번만 조회");
                        List<Product> products = productServiceImpl.findProduct(
                                skuNo, productName,brandName, maker, tempClassName, tempOperation);

                        // 제품 개수 조회, id 리스트 조회
                        int productSelectCnt = 0;
                        List<Long> listId = new ArrayList<>();
                        List<List<Long>> pageListId = new ArrayList<>();

                        // id들 모두 조회
                        List<Long> tmpId = new ArrayList<>();
                        tmpId = productServiceImpl.findSelectId(skuNo, productName,brandName, maker, tempClassName, tempOperation);
                        // 처음 조회 시 진행(총 검색 수량 확인. vue의 페이지 처리를 위함)
                        productSelectCnt = tmpId.size();

                        int cnt = 0;
                        int len = tmpId.size();
                        int last = len - 1;

                        // 15개씩 정리해서 데이터를 넘김
                        for(int i = 0; i < len; i++){
                                if(cnt != 15){
                                        cnt++;
                                        listId.add(tmpId.get(i));
                                } else{
                                        pageListId.add(listId);
                                        listId = new ArrayList<>();
                                        i--;
                                        cnt = 0;
                                }

                                if(i == last){
                                        pageListId.add(listId);
                                }
                        }

                        log.info("Service 조회 완료");
                        long end = System.currentTimeMillis();
                        log.info("@@@ Service 완료 실행 시간 : " + (end - start) / 1000.0);

                        // log.info(products);
                        return ResponseDAO.<List<Product>>builder()
                                .data(Optional.ofNullable(products))
                                .selectCnt(productSelectCnt)
                                .idList(pageListId)
                                .build();
                }


        }

        // page에 따라 데이터 조회
        @GetMapping("/selectPageProduct")
        public ResponseDAO<List<Product>> selectPageProduct(
        @RequestParam(value = "productCurseId", defaultValue = "") Long[] productCurseId){

//                @RequestParam(value = "productCurseId", defaultValue = "0") List<Long> productCurseId

                Optional<List<Product>> products = (Optional<List<Product>>) productServiceImpl.findCurseProduct(productCurseId);

                return ResponseDAO.<List<Product>>builder()
                        .data(products)
                        .build();
        }

        // 제품 변경 리플 수정하기
        @PutMapping("/updateProductReply")
        public ResponseDto<Integer> updateProductReply(@RequestBody ReplyDTO productReplyDTO) throws Exception {
                log.info("Controller 접근됨. /updateProductReply");
                log.info("productReplyDTO : " + productReplyDTO);

                int data = 0;

                boolean check = productServiceImpl.updateProductReply(productReplyDTO);

                if (check) data = 1;
                else data = 0;

                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

        // 제품 삭제하기
        @DeleteMapping("/deleteProductReply")
        public ResponseDto<Integer> deleteProductReply(@RequestParam(value = "productId", defaultValue = "") Long productId,
                                                       @RequestParam(value = "productReplyId", defaultValue = "") Long[] productReplyId) throws Exception {
                log.info("Controller 접근됨. /deleteProductReply");

                boolean check = productServiceImpl.deleteProductReply(productId, productReplyId);

                log.info("check : " + check );

                int data = 0;

                if(check) data=1;
                else data=0;

                return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
        }

}
