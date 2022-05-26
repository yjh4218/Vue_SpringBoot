package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {



//    @Query(value = "SELECT * FROM product WHERE sku_no = ?1", nativeQuery = true)
//    Optional<Product> findBySkuNo(int sku_no);
//
//    @Query(value = "SELECT * FROM product WHERE productName = ?1", nativeQuery = true)
//    Optional<Product> findByProductName(String productName);
//
//    @Query(value = "SELECT * FROM product WHERE brandName = ?1", nativeQuery = true)
//    Optional<Product> findByBrandName(String brandName);
//
//    @Query(value = "SELECT * FROM product WHERE maker = ?1", nativeQuery = true)
//    Optional<Product> findByMaker(String maker);

    //@Query(value = "SELECT * FROM user WHERE username = ?1 AND password = ?2", nativeQuery = true)
//User login(String username, String password);
}
