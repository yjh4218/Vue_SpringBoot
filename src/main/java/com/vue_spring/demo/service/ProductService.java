package com.vue_spring.demo.service;

import com.vue_spring.demo.Repository.ProductRepository;
import com.vue_spring.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> productList() {
        return (List<Product>) productRepository.findAll();
    }
}
