package com.vue_spring.demo.controller;

import com.vue_spring.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping({"","/"})
    public String index(){
        productService.productList();
    }
}
