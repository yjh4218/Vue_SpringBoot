package com.vue_spring.demo.controller;

import java.util.List;

import com.vue_spring.demo.model.Product;
import com.vue_spring.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/selectProducts")
    public void list(Model model) {
        System.out.println("Controller 접근됨");
        List<Product> products = productService.findProductAll();
        System.out.println("Service 조회 완료");
        // System.out.println(products);
        model.addAttribute("product", products);
        System.out.println(model);
    }
}
