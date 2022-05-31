package com.vue_spring.demo.Repository;

import com.vue_spring.demo.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Optional<List<Product>> findBysku_noAndproductNameAndbrandNameAndmaker(long
    // sku_no, String productName, String brandName, String maker);
    // @Query(value = "SELECT * FROM Product WHERE product_name %:username%",
    // nativeQuery = true)
    // Optional<List<Product>>
    // findBySkunoContainingIgnoreCaseAndProductnameContainingIgnoreCaseAndBrandnameContainingIgnoreCaseAndMakerContainingIgnoreCase(
    // long sku_no, String product_name,
    // String brand_name, String maker);
//    @Query("select * from product where product_name like %:productName%")

//    되는 쿼리들
//        Optional<List<Product>> findByBrandNameContaining(String brandName);
//        Optional<List<Product>> findByMakerContainingIgnoreCase(String maker);
//        Optional<List<Product>> findByProductNameContaining(String productName);
//        Optional<List<Product>> findBySkuNoContaining(String skuNo);

        @Query(value = "SELECT * FROM PRODUCT WHERE productName LIKE %:productName%", nativeQuery = true)
        Optional<List<Product>> findByproductNameContaining(@Param("productName")String productName);

//        @Query("SELECT * FROM PRODUCT WHERE skuNo LIKE %:skuNo% AND productName LIKE %:productName% AND brandName LIKE %:brandName% AND maker LIKE %:maker% AND className IN (:className)")
//        Optional<List<Product>> findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCaseAndClassName(
//                @Param("skuNo")String skuNo,
//                @Param("productName")String productName,
//                @Param("brandName")String brandName,
//                @Param("maker")String maker,
//                @Param("className")Set<String> className
//        );
        //최종 쿼리
//        Optional<List<Product>> findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCase(String skuNo, String productName, String brandName, String maker);
    // @Query(value = "SELECT * FROM product WHERE sku_no = ?1", nativeQuery = true)
    // Optional<Product> findBySkuNo(int sku_no);
    //
//     @Query(value = "SELECT * FROM product WHERE productName = ?1", nativeQuery =true)
//     Optional<Product> findByProductName(String productName);
    //
    // @Query(value = "SELECT * FROM product WHERE brandName = ?1", nativeQuery =
    // true)
    // Optional<Product> findByBrandName(String brandName);
    //
    // @Query(value = "SELECT * FROM product WHERE maker = ?1", nativeQuery = true)
    // Optional<Product> findByMaker(String maker);

    // @Query(value = "SELECT * FROM user WHERE username = ?1 AND password = ?2",
    // nativeQuery = true)
    // User login(String username, String password);
}
