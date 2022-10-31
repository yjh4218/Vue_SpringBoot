package com.vue_spring.demo.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vue_spring.demo.DAO.CommonDAO;
import com.vue_spring.demo.DAO.MakerDAO;
import com.vue_spring.demo.DAO.SalesDAO;
import com.vue_spring.demo.DTO.ResponseDto;
import com.vue_spring.demo.DTO.SalesDTO;
import com.vue_spring.demo.DTO.SalesProductComponentDTO;
import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.model.Sales;
import com.vue_spring.demo.model.SalesProductComponent;
import com.vue_spring.demo.service.SalesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("sales")
public class SalesController {

    @Autowired
    private SalesServiceImpl salesServiceImpl;

    // 판매구성품 추가하기
    @PostMapping("/insertSalesProductComponent")
    public ResponseDto<Integer> insertSalesProductComponent(
            @RequestBody SalesProductComponentDTO salesProductComponentDTO
    ) {
        System.out.println("Controller 접근됨. /insertSalesProductComponent");
        System.out.println(salesProductComponentDTO.getSkuNo());
        System.out.println(salesProductComponentDTO.getProductName());
//        System.out.println(salesProductComponentDTO.getComponentProduct());
        for(int i = 0; i<salesProductComponentDTO.getComponentProduct().size(); i++){
            System.out.println(salesProductComponentDTO.getComponentProduct().get(i).getSkuNo());
        }

        boolean check = salesServiceImpl.insertSalesProduct(salesProductComponentDTO);

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }

    // 판매구성품 수정하기
    @PostMapping("/updateSalesProductComponent")
    public ResponseDto<Integer> updateSalesProductComponent(
            @RequestBody SalesProductComponentDTO salesProductComponentDTO
    ) {
        System.out.println("Controller 접근됨. /updateSalesProductComponent");

        boolean check = salesServiceImpl.updateSalesProduct(salesProductComponentDTO);

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }

    // 제품 삭제하기
    @DeleteMapping("/deleteSalesProductComponent")
    public ResponseDto<Integer> deleteSalesProductComponent(@RequestParam(value = "skuNo", defaultValue = "") String skuNo) {
        System.out.println("Controller 접근됨. /deleteSalesProductComponent");
        System.out.println(skuNo);

        boolean check = salesServiceImpl.deleteProductComponent(skuNo);

        System.out.println("check : " + check );

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }

    // 판매구성품 중복 조회
    @GetMapping("/checkProductComponentSkuNo")
    public ResponseDto<Integer> checkProductComponentSkuNo(@RequestParam(value = "skuNo", required = true, defaultValue = "") String skuNo) {
        System.out.println("Controller 접근됨. /checkProduct");
        System.out.println(skuNo);

        boolean check = salesServiceImpl.checkProductComponentSkuNo(skuNo);

        System.out.println(check);
        int data = 0;

        if(check) data=1;

        else data=0;

        // 데이터 존재할 경우 1 전달, 없을 경우 0 전달.
        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }

    // 판매구성품 조회
    @GetMapping("/selectSalesProduct")
    public CommonDAO<List<SalesProductComponent>> selectProductlist(
            @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
            @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
            @RequestParam(value = "comSkuNo", required = false, defaultValue = "") String componentSkuNo,
            @RequestParam(value = "comProductName", required = false, defaultValue = "") String componentProductName) {

        List<String> tempType = new ArrayList<>();

        System.out.println("Controller 접근됨. /selectSalesProduct");
        System.out.println("skuNo : " + skuNo + ", productName : " + productName +
                ", comSkuNo : " + componentSkuNo + ", comProductName : " + componentProductName );

        Optional<List<SalesProductComponent>> salesProductComponent = (Optional<List<SalesProductComponent>>) salesServiceImpl.findSalesProduct(skuNo,
                productName, componentSkuNo, componentProductName);
        System.out.println("Service 조회 완료");
        // System.out.println(products);
        return CommonDAO.<List<SalesProductComponent>>builder()
                .data(salesProductComponent)
                .build();
    }

    // 판매량 추가하기
    @PostMapping("/insertSales")
    public ResponseDto<Integer> insertSales(@RequestBody SalesDTO salesDTO) {
        System.out.println("Controller 접근됨. /insertSales");

        int data = 0;

        boolean checkData = salesServiceImpl.checkMonthSalesData(salesDTO.getSalesMonth());

        if(checkData){
            // 기존 등록된 데이터 존재함.
            data = 2;
        } else {
            boolean check = salesServiceImpl.insertSales(salesDTO);

            if (check) data = 1;
            else data = 0;
        }

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }

    // 판매량 조회
    @GetMapping("/selectSales")
    public CommonDAO<List<SalesDAO>> selectSales(
            @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
            @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
            @RequestParam(value = "comFlag", required = false, defaultValue = "") String comFlag,
            @RequestParam(value = "findDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date findDate
    ) {

        List<String> tempType = new ArrayList<>();

//        boolean comFlag = true;
        System.out.println("Controller 접근됨. /selectSales");
        System.out.println("skuNo : " + skuNo + ", productName : " + productName +
                ", comFlag : " + comFlag + ", findDate : " + findDate);

        Optional<List<SalesDAO>> sales = (Optional<List<SalesDAO>>) salesServiceImpl.selectSales(skuNo,
                productName, comFlag, findDate);

        System.out.println("Service 조회 완료");
        // System.out.println(products);
        return CommonDAO.<List<SalesDAO>>builder()
                .data(sales)
                .build();
    }

    // 월별 판매량 조회(수정 및 삭제를 위함)
    @GetMapping("/selectMonthSales")
    public CommonDAO<List<Sales>> selectSales(
            @RequestParam(value = "skuNo", required = false, defaultValue = "") String skuNo,
            @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
            @RequestParam(value = "findDate", required = false) @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date findDate
    ) {
        System.out.println("Controller 접근됨. /selectMonthSales");
        System.out.println("skuNo : " + skuNo + ", productName : " + productName + ", findDate : " + findDate);

        Optional<List<Sales>> sales = (Optional<List<Sales>>) salesServiceImpl.selectMonthSales(skuNo,
                productName, findDate);

        System.out.println("Service 조회 완료");
        System.out.println(sales.get().size());
        // System.out.println(products);
        return CommonDAO.<List<Sales>>builder()
                .data(sales)
                .build();
    }

    // 월별 판매량 수정하기
    @PutMapping("/updateMonthSales")
    public ResponseDto<Integer> updateMonthSales(@RequestBody SalesDTO salesDTO) {
        System.out.println("Controller 접근됨. /updateMonthSales");

        boolean check = salesServiceImpl.updateMonthSales(salesDTO);

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }

    // 월별 판매량 삭제하기
    @DeleteMapping("/deleteMonthSales")
    public ResponseDto<Integer> deleteMonthSales(@RequestParam(value = "ids", defaultValue = "") Long[] ids) {
        System.out.println("Controller 접근됨. /deleteMonthSales");

        boolean check = salesServiceImpl.deleteMonthSales(ids);

        System.out.println("check : " + check );

        int data = 0;

        if(check) data=1;
        else data=0;

        return new ResponseDto<Integer>(HttpStatus.OK.value(),data);
    }
}
