package com.vue_spring.demo.service;

import com.vue_spring.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface ProductService {

    public List<Product> findProductAll();

    public Optional<List<Product>> findProduct(String sku_no, String productName,
            String brandName, String maker, Set<String> tempSelectChk);

    public Product findSkuNo(String skuNo);

    public Boolean insertProduct(Product product);

    public Boolean updateProduct(Product product);

    public Boolean deleteProduct(String skuNo);

    public Boolean checkSkuNo(String skuNo);
}
