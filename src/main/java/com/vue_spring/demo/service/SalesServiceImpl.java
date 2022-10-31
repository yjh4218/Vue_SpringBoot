package com.vue_spring.demo.service;

import com.vue_spring.demo.DAO.SalesDAO;
import com.vue_spring.demo.DTO.SalesDTO;
import com.vue_spring.demo.DTO.SalesProductComponentDTO;
import com.vue_spring.demo.Repository.SalesProductComponentRepository;
import com.vue_spring.demo.Repository.SalesRepository;
import com.vue_spring.demo.model.Sales;
import com.vue_spring.demo.model.SalesProductComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
@Service
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private SalesProductComponentRepository salesProductComponentRepository;

    @Autowired
    public void setSalesRepository(SalesRepository salesRepository) {
         this.salesRepository = salesRepository;
     }

     @Autowired
    public void setSalesProductComponentRepository(SalesProductComponentRepository salesProductComponentRepository) {
        this.salesProductComponentRepository = salesProductComponentRepository;
    }

    @Autowired
    public void salesRepository(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

     // 신규 기획상품, 구성품 추가
    public Boolean insertSalesProduct(SalesProductComponentDTO salesProductComponentDTO){
        System.out.println("신규 기획상품, 구성품 추가 서비스 : " + salesProductComponentDTO);

        long componentNumber = 1;
        int size = salesProductComponentDTO.getComponentProduct().size();

        try{
            for(int i = 0; i<size; i++){
                SalesProductComponent salesProductComponent = new SalesProductComponent();

                salesProductComponent.setId(null);
                salesProductComponent.setSkuNo(salesProductComponentDTO.getSkuNo());
                salesProductComponent.setProductName(salesProductComponentDTO.getProductName());
                salesProductComponent.setComponentSkuNo(salesProductComponentDTO.getComponentProduct().get(i).getSkuNo());
                salesProductComponent.setComponentProductName(salesProductComponentDTO.getComponentProduct().get(i).getProductName());
                salesProductComponent.setComponentQuantity(salesProductComponentDTO.getComponentProduct().get(i).getQuantity());
                salesProductComponent.setComponentNumber(componentNumber);
                salesProductComponent.setProductLength(size);

                salesProductComponentRepository.save(salesProductComponent);

                componentNumber++;

            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 기획상품, 구성품 수정
    public Boolean updateSalesProduct(SalesProductComponentDTO salesProductComponentDTO){
        System.out.println("신규 기획상품, 구성품 추가 서비스 : " + salesProductComponentDTO);

        // 기존에 등록된 데이터 삭제.
        // 업데이트 하지 않고 삭제 이유는 데이터가 구성품 sku 기준으로 수정하더라도 기존 구성품이 완전 바뀌거나, 추가되는 경우, 삭제되는 경우가 있기 때문
        boolean check = deleteProductComponent(salesProductComponentDTO.getSkuNo());

        if(check){
            long componentNumber = 1;
            int size = salesProductComponentDTO.getComponentProduct().size();

            try{
                for(int i = 0; i<size; i++){
                    SalesProductComponent salesProductComponent = new SalesProductComponent();

                    salesProductComponent.setId(null);
                    salesProductComponent.setSkuNo(salesProductComponentDTO.getSkuNo());
                    salesProductComponent.setProductName(salesProductComponentDTO.getProductName());
                    salesProductComponent.setComponentSkuNo(salesProductComponentDTO.getComponentProduct().get(i).getSkuNo());
                    salesProductComponent.setComponentProductName(salesProductComponentDTO.getComponentProduct().get(i).getProductName());
                    salesProductComponent.setComponentQuantity(salesProductComponentDTO.getComponentProduct().get(i).getQuantity());
                    salesProductComponent.setComponentNumber(componentNumber);
                    salesProductComponent.setProductLength(size);

                    salesProductComponentRepository.save(salesProductComponent);

                    componentNumber++;
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        } else{
            return false;
        }
    }


    // 판매 구성품에 skuNo 중복 값 확인
    public boolean checkProductComponentSkuNo(String skuNo){
        boolean check = salesProductComponentRepository.existsBySkuNo(skuNo);
        return check;
    }

    // 제조사 조회. 조건에 따른 검색 진행
    public Optional<List<SalesProductComponent>> findSalesProduct(String skuNo,
                                                                  String productName, String componentSkuNo, String componentProductName){

        System.out.println("salesServiceImpl 판매 구성품 조회");

        return (Optional<List<SalesProductComponent>>) salesProductComponentRepository
                .findBySkuNoContainingAndProductNameContainingAndComponentSkuNoContainingAndComponentProductNameContainingIgnoreCase(
                        skuNo, productName, componentSkuNo, componentProductName);

    }

    // 구성품 삭제하기
    public Boolean deleteProductComponent(String skuNo){
        System.out.println("구성품 삭제 서비스: " + skuNo);

        // 제조사 조회 확인
        boolean check = checkProductComponentSkuNo(skuNo);

        // 조회된 데이터 없을 경우
        if(!check){
            System.out.println("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 삭제 진행.");
            salesProductComponentRepository.deleteBySkuNo(skuNo);
            return true;
        }
    }

    // 판매량 추가
    public Boolean insertSales(SalesDTO salesDTO){
        System.out.println("판매량 추가 서비스 : " + salesDTO);

        int size = salesDTO.getSalesData().size();

        List<Sales> listSales = new ArrayList<>();

        try{
            for(int i = 0; i<size; i++){
                Sales sales = new Sales();

                sales.setId(null);
                sales.setSkuNo(salesDTO.getSalesData().get(i).getSkuNo());
                sales.setProductName(salesDTO.getSalesData().get(i).getProductName());
                sales.setSalesVolumn(salesDTO.getSalesData().get(i).getSalesVolumn());
                sales.setSalesMonth(salesDTO.getSalesMonth());

                listSales.add(sales);
            }
            salesRepository.saveAll(listSales);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
//
    // 판매량 조회. 조건에 따른 검색 진행
    public Optional<List<SalesDAO>> selectSales(String skuNo, String productName, String comFlag, Date findDate){

        System.out.println("판매량 조회 서비스");
        System.out.println("skuNo : " + skuNo );
        System.out.println("productName : " + productName );
        System.out.println("findDate : " + findDate );

        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy");
        String date =SimpleDateFormat.format(findDate);

        System.out.println("date : " + date );
        boolean checkCom = Boolean.parseBoolean(comFlag);

        // 분류 검색 구분에 따른 조회 방법을 다르게 함
        // 구성품으로 판매량 조회
        if(checkCom){
            return (Optional<List<SalesDAO>>) salesRepository
                    .findByComponentSales(skuNo, productName, date);
        }
        // 판매상품으로 판매량 조회
        else{
            return (Optional<List<SalesDAO>>) salesRepository
                    .findBySales(skuNo, productName, date);
        }
    }

    // 월별 판매량 조회(수정 및 삭제를 위함)
    public Optional<List<Sales>> selectMonthSales(String skuNo, String productName, Date findDate){

        System.out.println("월별 판매량 조회 서비스");
        System.out.println("skuNo : " + skuNo );
        System.out.println("productName : " + productName );
        System.out.println("findDate : " + findDate );

        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date =SimpleDateFormat.format(findDate);

        System.out.println("date : " + date );

        // 월별 판매량 조회(수정 및 삭제를 위함)
        return (Optional<List<Sales>>) salesRepository
                .findByMonthSales(skuNo, productName, date);
    }

    // 월별 판매량 수정
    public Boolean updateMonthSales(SalesDTO salesDTO){
        System.out.println("월별 판매량 수정 서비스 : " + salesDTO);

        int size = salesDTO.getSalesData().size();

        List<Sales> listSales = new ArrayList<>();

        try{
            for(int i = 0; i<size; i++){
                Sales sales = new Sales();

                sales.setId(salesDTO.getSalesData().get(i).getId());
                sales.setSkuNo(salesDTO.getSalesData().get(i).getSkuNo());
                sales.setProductName(salesDTO.getSalesData().get(i).getProductName());
                sales.setSalesVolumn(salesDTO.getSalesData().get(i).getSalesVolumn());
                sales.setSalesMonth(salesDTO.getSalesMonth());

                listSales.add(sales);

            }
            salesRepository.saveAll(listSales);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 월별 판매량 삭제하기
    public Boolean deleteMonthSales(Long[] listIds){
        System.out.println("월별 판매량 삭제 서비스: ");

        // 데이터 조회(검증)
        boolean check = checkMonthSalesData(listIds[0]);

        System.out.println("check : " + check);

        // 조회된 데이터 없을 경우
        if(!check){
            System.out.println("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 삭제 진행.");
            salesRepository.deleteByIds(listIds);
            return true;
        }
    }

    // 월별 판매량 데이터 있는지 확인
    public boolean checkMonthSalesData(Date salesMonth){
        boolean check = salesRepository.existsBySalesMonth(salesMonth);
        return check;
    }
    // 월별 판매량 데이터 있는지 확인
    public boolean checkMonthSalesData(Long listId){
        boolean check = salesRepository.existsById(listId);
        return check;
    }
}
