package com.vue_spring.demo.service;

import com.vue_spring.demo.Repository.ProductRepository;
import com.vue_spring.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Service
public class ProductServicrImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    // @Autowired
    // public void setProductRepository(ProductRepository productRepository) {
    // this.productRepository = productRepository;
    // }

    public List<Product> findProductAll() {
        return (List<Product>) productRepository.findAll();
    }

    public Optional<List<Product>> findProduct(String skuNo, String productName,
            String brandName, String maker, Set<String> tempSelectChk) {

        System.out.println("ProductServicrImpl");
        System.out.println("productName : " + productName );
        return (Optional<List<Product>>) productRepository
                .findByproductNameContaining(productName);


//        return (Optional<List<Product>>) productRepository
//                .findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCaseAndClassName(
//                        skuNo, productName, brandName, maker, tempSelectChk);

        // 작동 됨
//         return (Optional<List<Product>>) productRepository
//         .findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCase(
//                 skuNo, productName, brandName, maker);

    }
}
