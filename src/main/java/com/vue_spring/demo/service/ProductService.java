package com.vue_spring.demo.service;

import com.vue_spring.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface ProductService {

    public List<Product> findProductAll();

    public Optional<List<Product>> findProduct(String sku_no, String productName,
            String brandName, String maker, Set<String> tempSelectChk);

    public Optional<Product> findProduct(long productId);

//    public Boolean insertProduct(Product product);

    public Boolean insertProduct(Product product, List<MultipartFile> imgFiles) throws Exception;

//    public Boolean updateProduct(Product product);
    public Boolean updateProduct(Product product, List<MultipartFile> imgFiles) throws Exception;

    public Boolean deleteProduct(long id);

    public Boolean checkSkuNo(String skuNo);

    public Boolean checkId(long productId);
}
