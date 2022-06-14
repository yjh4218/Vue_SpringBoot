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

     @Autowired
     public void setProductRepository(ProductRepository productRepository) {
         this.productRepository = productRepository;
     }


     // 신규 상품 추가
    public Boolean insertProduct(Product product){
        System.out.println("상품 추가 서비스 : " + product);

        boolean check = checkSkuNo(product.getSkuNo());

        // 중복된 데이터 존재
        if(check){
            return false;
        }
        else{
            productRepository.save(product);
            return true;
        }
    }

    // 상품 정보 수정
    public Boolean updateProduct(Product product){
        System.out.println("상품 추가 서비스 : " + product);

        boolean check = checkSkuNo(product.getSkuNo());

        // 조회된 데이터 없을 경우
        if(!check){

            System.out.println("데이터 없음. 수정 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 수정 진행.");
            productRepository.save(product);
            return true;
        }
    }

    // 제품 삭제하기
    public Boolean deleteProduct(String SkuNo){
        System.out.println("제품 삭제 : " + SkuNo);

        boolean check = checkSkuNo(SkuNo);

        // 조회된 데이터 없을 경우
        if(!check){
            System.out.println("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 삭제 진행.");
            productRepository.deleteBySkuNo(SkuNo);
            return true;
        }
    }

    // 모든 상품 검색
    public List<Product> findProductAll() {
        return (List<Product>) productRepository.findAll();
    }

    // 일부 상품 조회. 조건에 따른 검색 진행
    public Optional<List<Product>> findProduct(String skuNo, String productName,
            String brandName, String maker, Set<String> tempClassName) {

        System.out.println("ProductServicrImpl");
        System.out.println("productName : " + productName );
        System.out.println("tempSelectChk : " + tempClassName );

        // 분류 검색 구분에 따른 조회 방법을 다르게 함
        if(tempClassName.size() < 1){
            System.out.println("제품분류 없음.");
            // 분류 조회 하지 않을 경우
            return (Optional<List<Product>>) productRepository
                .findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCase(
                     skuNo, productName, brandName, maker);
        }else{
            System.out.println("제품분류 있음.");
            // 분류 조회할 경우
            return (Optional<List<Product>>) productRepository
                    .findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCaseAndClassName(
                            skuNo, productName, brandName, maker, tempClassName);
        }
    }

    // 1개 제품 조회
    public Optional<Product> findSkuNo(String skuNo) {

        System.out.println("ProductServicrImpl");
        System.out.println("skuNo : " + skuNo );

        return (Optional<Product>) productRepository.findBySkuNo(skuNo);
    }

    // 제품 중복 확인
    public Boolean checkSkuNo(String SkuNo){
        boolean check = productRepository.existsBySkuNo(SkuNo);
        return check;
    }
}
