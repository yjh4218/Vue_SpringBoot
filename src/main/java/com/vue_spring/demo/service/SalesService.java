package com.vue_spring.demo.service;

import com.vue_spring.demo.DAO.SalesDAO;
import com.vue_spring.demo.DTO.SalesDTO;
import com.vue_spring.demo.DTO.SalesProductComponentDTO;
import com.vue_spring.demo.model.Maker;
import com.vue_spring.demo.model.Sales;
import com.vue_spring.demo.model.SalesProductComponent;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface SalesService {


//    public Optional<List<Maker>> findMaker(String skuNo, String productName,
//                                             String brandName, String maker, Set<String> tempBusinessType);

    public Boolean insertSalesProduct(SalesProductComponentDTO salesProductComponentDTO);

    public Boolean updateSalesProduct(SalesProductComponentDTO salesProductComponentDTO);

    public boolean checkProductComponentSkuNo(String skuNo);

    public Optional<List<SalesProductComponent>> findSalesProduct(String skuNo,
                                                                  String productName, String comSkuNo, String comProductName);

    public Boolean deleteProductComponent(String skuNo);

    public Optional<List<SalesDAO>> selectSales(String skuNo, String productName, String comFlag, Date findDate);

    public Optional<List<Sales>> selectMonthSales(String skuNo, String productName, Date findDate);

    public Boolean updateMonthSales(SalesDTO salesDTO);

    public Boolean deleteMonthSales(Long[] listIds);

    public boolean checkMonthSalesData(Date salesMonth);
    public boolean checkMonthSalesData(Long listId);
//

//    public Boolean updateMaker(Maker maker);
//
//    public Boolean checkId(long makerId);
//
//    public Boolean deleteMaker(long id);
}
