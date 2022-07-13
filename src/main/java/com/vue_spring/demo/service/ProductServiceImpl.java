package com.vue_spring.demo.service;

import com.vue_spring.demo.Repository.ProductImageRepository;
import com.vue_spring.demo.Repository.ProductRepository;
import com.vue_spring.demo.component.FileHandler;
import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Product;
import com.vue_spring.demo.model.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private InspectServiceImpl inspectServicrImpl;

     @Autowired
     public void setProductRepository(ProductRepository productRepository) {
         this.productRepository = productRepository;
     }


    private final FileHandler fileHandler;

    public ProductServiceImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

     // 신규 상품 추가
//    public Boolean insertProduct(Product product){
//        System.out.println("상품 추가 서비스 : " + product);
//
//        boolean check = checkSkuNo(product.getSkuNo());
//
//        // 중복된 데이터 존재
//        if(check){
//            return false;
//        }
//        else{
//            productRepository.save(product);
//            return true;
//        }
//    }
    // 신규 검수 추가
    public Boolean insertProduct(Product product, List<MultipartFile> imgFiles) throws Exception {
        System.out.println("상품 추가 서비스 : " + product);
        System.out.println("상품 추가 서비스222 : " + imgFiles);

        // 이미지 파일 경로 지정
        ProductImage tmpImage = new ProductImage();
        List<ProductImage> productImageList = fileHandler.parseFileInfo(imgFiles, tmpImage);

        System.out.println(product);
        System.out.println(productImageList);

        // JPA에 저장되는지 확인. 기본 값은 저장 실패로 해놓는다.
        boolean check = false;

        try{
            // 검수 데이터 저장 DB에 저장
            Long id = productRepository.save(product).getId();
            // DB에 저장된 검수 내용 조회
            Optional<Product> productTemp = productRepository.findById(id);


            System.out.println("111111111111 " );

            // 이미지 파일이 있을 때만 실행함.
            if(!productImageList.isEmpty()) {
                Product productSel = productTemp.get();

                for(ProductImage image : productImageList) {

                    System.out.println("검수 추가 서비스33333 : " + productImageList);
                    System.out.println("검수 추가 서비스44444 : " + image);

                    // 이미지 데이터가 존재할 경우 진행
                    if(!productTemp.isEmpty()){

                        System.out.println("inspectTemp : " + productSel);

                        // 이미지 파일들에 검수정보 저장(inspect_id)
                        image.setProduct(productSel);

                        System.out.println("image : " + image);

                        // image DB에 저장
                        productImageRepository.save(image);

                        // DB에 저장된 검수 내용에 사진 정보 저장
                        productSel.addPhoto(image);
                    }
                }

                // DB에 사진정보 추가한 상태로 다시 저장
                productRepository.save(productSel);

                // 저장 성공
                check = true;
            }
            // 이미지 파일이 없을 경우 검수 내용만 저장
            else{
                productRepository.save(product);
                // 저장 성공
                check = true;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            //저장 실패
            check=false;
        }

        return check;
    }

    // 상품 정보 수정
//    public Boolean updateProduct(Product product){
//        System.out.println("상품 정보 수정 서비스 : " + product);
//
//        // 제품 조회 확인
//        boolean check = checkSkuNo(product.getSkuNo());
//
//        // 조회된 데이터 없을 경우
//        if(!check){
//            System.out.println("데이터 없음. 수정 불가");
//            return false;
//        }
//        else{
//            System.out.println("데이터 있음. 수정 진행.");
//            productRepository.save(product);
//            return true;
//        }
//    }
    // 상품 수정
    public Boolean updateProduct(Product product, List<MultipartFile> imgFiles) throws Exception {
        System.out.println("검수 수정 서비스 : " + product);
        System.out.println("검수 수정 서비스222 : " + imgFiles);

        boolean check = false;

        // 기존에 등록된 이미지가 있는지 확인
        boolean existImg = productImageRepository.existsByProductId(product.getId());

        // 새로운 이미지 파일이 있다면
        if(imgFiles != null){
            // 이미지 파일 지정
            ProductImage tmpImage = new ProductImage();
            List<ProductImage> productImageList = fileHandler.parseFileInfo(imgFiles, tmpImage);

            System.out.println("이미지 있음");

            // 기존에 등록된 이미지가 있으면 삭제
            if(existImg){
                Optional<List<ProductImage>> deleteImg = productImageRepository.findByProductId(product.getId());
                // 경로에 지정된 파일 삭제
                for(ProductImage image : deleteImg.get()){
                    File file = new File(image.getImgFilePath());
                    file.delete();
                }
                // DB의 파일 삭제
                productImageRepository.deleteByProductId(product.getId());
            }
            try{
                // 이미지 파일 저장.
                for(ProductImage image : productImageList) {

                    System.out.println("검수 추가 서비스33333 : " + productImageList);

                    image.setProduct(product);
                    product.addPhoto(image);

                    System.out.println("image : " + image);

                    // image DB에 저장
                    productImageRepository.save(image);
                }

                // inspect 업데이트
                productRepository.save(product);

                // 저장 성공
                check = true;
            }catch (Exception e){
                System.out.println(e.getMessage());
                check=false;
            }
        }
        // DB에 있는 이미지 갖고와서 연결 진행
        else{
            // 기존에 등록된 이미지가 있으면 다시 연결
            if(existImg){
                Optional<List<ProductImage>> productImageList = productImageRepository.findByProductId(product.getId());
                for(ProductImage image : productImageList.get()){
                    product.addPhoto(image);
                }
            }
            // inspect 업데이트
            productRepository.save(product);
            // 저장 성공
            check = true;
        }

        return check;
    }


    // 제품 삭제하기
//    public Boolean deleteProduct(long id, String skuNo){
//        System.out.println("제품 삭제 : " + id);
//
//        boolean check = checkSkuNo(skuNo);
//
//        // 조회된 데이터 없을 경우
//        if(!check){
//            System.out.println("데이터 없음. 삭제 불가");
//            return false;
//        }
//        else{
//            System.out.println("데이터 있음. 삭제 진행.");
//            productRepository.deleteById(id);
//            return true;
//        }
//    }
    // 상품 삭제하기
    public Boolean deleteProduct(long id){
        System.out.println("상품 삭제 서비스 : " + id);

        // 검수 데이터 있는지 확인
        boolean check = checkId(id);

        // 조회된 데이터 없을 경우
        if(!check){
            System.out.println("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 삭제 진행.");

            // 검수 데이터 먼저 삭제 처리.
            if(inspectServicrImpl.findProductInspect(id)){
                System.out.println("검수 삭제.");
                Optional<List<Inspect>> tmpInspect = inspectServicrImpl.findInspectProductList(id);

                System.out.println(tmpInspect);

                List<Inspect> inspect = tmpInspect.get();
                for(int i=0; i<inspect.size(); i++){
                    inspectServicrImpl.deleteInspect(inspect.get(i).getId());
                }
            };

            // 기존에 등록된 이미지가 있는지 확인
            boolean existImg = productImageRepository.existsByProductId(id);

            // 기존에 등록된 이미지가 있으면 삭제
            if(existImg){
                Optional<List<ProductImage>> deleteImg = productImageRepository.findByProductId(id);
                // 경로에 지정된 파일 삭제
                for(ProductImage image : deleteImg.get()){
                    File file = new File(image.getImgFilePath());
                    file.delete();
                }
                // DB의 파일 삭제
                productImageRepository.deleteByProductId(id);
                productRepository.deleteById(id);
            }

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
    public Optional<Product> findProduct(long productId) {

        System.out.println("ProductServicrImpl");
        System.out.println("productId : " + productId );

        boolean check = checkId(productId);

        System.out.println("check : " + check );

        if(check){
            return productRepository.findById(productId);
        }
        else{
            return null;
        }
    }

    // 제품 중복 확인
    public Boolean checkSkuNo(String SkuNo){
        boolean check = productRepository.existsBySkuNo(SkuNo);
        return check;
    }

    // 제품 중복 확인
    public Boolean checkId(long productId){
        boolean check = productRepository.existsById(productId);
        return check;
    }
}
