package com.vue_spring.demo.service;

import com.vue_spring.demo.Repository.InspectImageRepository;
import com.vue_spring.demo.Repository.InspectRepository;
import com.vue_spring.demo.component.FileHandler;
import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.InspectImage;
import com.vue_spring.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Repository
@Service
public class InspectServicrImpl implements InspectService {

    @Autowired
    private InspectRepository inspectRepository;
    @Autowired
    private InspectImageRepository inspectImageRepository;

    private final FileHandler fileHandler;

    public InspectServicrImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Autowired
     public void setInspectRepository(InspectRepository inspectRepository) {
         this.inspectRepository = inspectRepository;
     }


     // 신규 검수 추가
    public Boolean insertInspect(Inspect inspect, List<MultipartFile> imgFiles) throws Exception {
        System.out.println("검수 추가 서비스 : " + inspect);
        System.out.println("검수 추가 서비스222 : " + imgFiles);

        // 이미지 파일 경로 지정
        List<InspectImage> inspectList = fileHandler.parseFileInfo(imgFiles);

        System.out.println(inspect);
        System.out.println(inspectList);

        // JPA에 저장되는지 확인. 기본 값은 저장 실패로 해놓는다.
        boolean check = false;

        try{
            // 검수 데이터 저장 DB에 저장
            Long id = inspectRepository.save(inspect).getId();
            // DB에 저장된 검수 내용 조회
            Optional<Inspect> inspectTemp = inspectRepository.findById(id);

            // 이미지 파일이 있을 때만 실행함.
            if(!inspectList.isEmpty()) {

                for(InspectImage image : inspectList) {

                    System.out.println("검수 추가 서비스33333 : " + inspectList);
                    System.out.println("검수 추가 서비스44444 : " + inspect);

                    // 이미지 데이터가 존재할 경우 진행
                    if(!inspectTemp.isEmpty()){
                        Inspect inspectSelect = inspectTemp.get();
                        System.out.println("inspectTemp : " + inspectSelect);

                        // 이미지 파일들에 검수정보 저장(inspect_id)
                        image.setInspect(inspectSelect);

                        System.out.println("image : " + image);

                        // image DB에 저장
                        inspectImageRepository.save(image);

                        // DB에 저장된 검수 내용에 사진 정보 저장 후 다시 저장
                        inspectSelect.addPhoto(image);
                        inspectRepository.save(inspectSelect);

                        // 저장 성공
                        check = true;
                    }
                }
            }
            // 이미지 파일이 없을 경우 검수 내용만 저장
            else{
                inspectRepository.save(inspect);
                // 저장 성공
                check = true;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            check=false;
        }

        return check;

    }
//
//    // 상품 정보 수정
//    public Boolean updateProduct(Product product){
//        System.out.println("상품 추가 서비스 : " + product);
//
//        boolean check = checkSkuNo(product.getSkuNo());
//
//        // 조회된 데이터 없을 경우
//        if(!check){
//
//            System.out.println("데이터 없음. 수정 불가");
//            return false;
//        }
//        else{
//            System.out.println("데이터 있음. 수정 진행.");
//            productRepository.save(product);
//            return true;
//        }
//    }
//
//    // 제품 삭제하기
//    public Boolean deleteProduct(String SkuNo){
//        System.out.println("제품 삭제 : " + SkuNo);
//
//        boolean check = checkSkuNo(SkuNo);
//
//        // 조회된 데이터 없을 경우
//        if(!check){
//            System.out.println("데이터 없음. 삭제 불가");
//            return false;
//        }
//        else{
//            System.out.println("데이터 있음. 삭제 진행.");
//            productRepository.deleteBySkuNo(SkuNo);
//            return true;
//        }
//    }
//
    // 모든 상품 검색
    public List<Inspect> findProductAll() {
        return (List<Inspect>) inspectRepository.findAll();
    }
//
//    // 일부 상품 조회. 조건에 따른 검색 진행
//    public Optional<List<Product>> findProduct(String skuNo, String productName,
//            String brandName, String maker, Set<String> tempClassName) {
//
//        System.out.println("ProductServicrImpl");
//        System.out.println("productName : " + productName );
//        System.out.println("tempSelectChk : " + tempClassName );
//
//        // 분류 검색 구분에 따른 조회 방법을 다르게 함
//        if(tempClassName.size() < 1){
//            System.out.println("제품분류 없음.");
//            // 분류 조회 하지 않을 경우
//            return (Optional<List<Product>>) productRepository
//                .findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCase(
//                     skuNo, productName, brandName, maker);
//        }else{
//            System.out.println("제품분류 있음.");
//            // 분류 조회할 경우
//            return (Optional<List<Product>>) productRepository
//                    .findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCaseAndClassName(
//                            skuNo, productName, brandName, maker, tempClassName);
//        }
//    }
//
//    // 1개 제품 조회
//    public Optional<Product> findSkuNo(String skuNo) {
//
//        System.out.println("ProductServicrImpl");
//        System.out.println("skuNo : " + skuNo );
//
//        return (Optional<Product>) productRepository.findBySkuNo(skuNo);
//    }
//
    // 제품 중복 확인
    public Boolean checkInspect(Product product, String inspectDate){
        boolean check = inspectRepository.existsByProductAndInspectDate(product, inspectDate);
        return check;
    }
}
