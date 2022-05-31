package com.vue_spring.demo.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vue_spring.demo.DAO.ProductDAO;
import com.vue_spring.demo.model.Product;
import com.vue_spring.demo.service.ProductServicrImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductController {

        @Autowired
        private ProductServicrImpl productServicrImpl;

        @GetMapping("/selectAllProducts")
        public ProductDAO<List<Product>> selectAllProductlist() {
                System.out.println("Controller 접근됨");
                Optional<List<Product>> products = Optional.ofNullable(productServicrImpl.findProductAll());
                System.out.println("Service 조회 완료");
                // System.out.println(products);
                return ProductDAO.<List<Product>>builder()
                                .data(products)
                                .build();
        }

        @GetMapping("/selectProducts")
        public ProductDAO<List<Product>> selectProductlist(
                        @RequestParam(value = "sku_no", required = false, defaultValue = "") String skuNo,
                        @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
                        @RequestParam(value = "brandName", required = false, defaultValue = "") String brandName,
                        @RequestParam(value = "maker", required = false, defaultValue = "") String maker,
                        @RequestParam(value = "selectChk", required = false, defaultValue = "") List<String> selectChk) {
                System.out.println("Controller 접근됨");
//                System.out.println("tempskuNo : " + tempskuNo + ", productName : " + productName + ", brandName : " + brandName + ", maker : " + maker );
                System.out.println("productName : " + productName );

                Set<String> tempSelectChk = new HashSet<>(selectChk);
                Optional<List<Product>> products = (Optional<List<Product>>) productServicrImpl.findProduct(skuNo,
                                productName,
                                brandName, maker, tempSelectChk);
                System.out.println("Service 조회 완료");
                // System.out.println(products);
                return ProductDAO.<List<Product>>builder()
                                .data(products)
                                .build();
        }

}
