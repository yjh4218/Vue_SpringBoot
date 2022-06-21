package com.vue_spring.demo.service;

import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface InspectService {

//    public List<Inspect> findProductAll();

//    public Optional<List<Inspect>> findProduct(String sku_no, String productName,
//            String brandName, String maker, Set<String> tempSelectChk);
//
//    public Optional<Inspect> findSkuNo(String skuNo);
//
    public Boolean insertInspect(Inspect inspect, List<MultipartFile> imgFiles) throws Exception;
//
//    public Boolean updateInspect(Inspect inspect);
//
//    public Boolean deleteInspect(String skuNo);

    public Boolean checkInspect(Product product, String inspectDate);
}
