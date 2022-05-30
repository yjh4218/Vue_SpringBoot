package com.vue_spring.demo.service;

import com.vue_spring.demo.Repository.ProductRepository;
import com.vue_spring.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findProductAll() {
        return (List<Product>) productRepository.findAll();
    }
    public Optional<List<Product>> findProduct(long sku_no, String productName, String brandName, String maker) {
        return (Optional<List<Product>>) productRepository.findByProductnameContaning(productName);
//        return (Optional<List<Product>>) productRepository.findBysku_noAndproductNameAndbrandNameAndmaker(sku_no, productName,brandName,maker);
    }
}
