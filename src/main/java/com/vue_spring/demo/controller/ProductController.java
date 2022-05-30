package com.vue_spring.demo.controller;

import java.util.List;
import java.util.Optional;

import com.vue_spring.demo.DAO.ProductDAO;
import com.vue_spring.demo.model.Product;
import com.vue_spring.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/selectAllProducts")
    public ProductDAO<List<Product>> selectAllProductlist() {
        System.out.println("Controller 접근됨");
        Optional<List<Product>> products = Optional.ofNullable(productService.findProductAll());
        System.out.println("Service 조회 완료");
        // System.out.println(products);
        return ProductDAO.<List<Product>>builder()
                .data(products)
                .build();
    }

    @GetMapping("/selectProducts")
    public ProductDAO<List<Product>> selectProductlist(
            @RequestParam(value = "sku_no", required = false, defaultValue = "0") long sku_no,
            @RequestParam(value = "productName", required = false, defaultValue = "") String productName,
            @RequestParam(value = "brandName", required = false, defaultValue = "") String brandName,
            @RequestParam(value = "maker", required = false, defaultValue = "") String maker) {
        System.out.println("Controller 접근됨");
        Optional<List<Product>> products = (Optional<List<Product>>) productService.findProduct(sku_no, productName, brandName, maker);
        System.out.println("Service 조회 완료");
        // System.out.println(products);
        return ProductDAO.<List<Product>>builder()
                .data(products)
                .build();
    }

}
