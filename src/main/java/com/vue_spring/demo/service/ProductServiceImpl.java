package com.vue_spring.demo.service;

import com.vue_spring.demo.DTO.ReplyDTO;
import com.vue_spring.demo.Repository.*;
import com.vue_spring.demo.component.FileHandler;
import com.vue_spring.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
//    private ProductRepository2 productRepository2;
    private ProductImageRepository productImageRepository;
    private InspectServiceImpl inspectServiceImpl;
    private SalesProductComponentRepository salesProductComponentRepository;
    private ProductChangeReplyRepository productChangeReplyRepository;
    private final FileHandler fileHandler;

     @Autowired
     public void setProductRepository(ProductRepository productRepository) {
         this.productRepository = productRepository;
     }

    @Autowired
    public void setProductImageRepository(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Autowired
    public void setInspectServiceImpl(InspectServiceImpl inspectServiceImpl) {
        this.inspectServiceImpl = inspectServiceImpl;
    }

    @Autowired
    public void setSalesProductComponentRepository(SalesProductComponentRepository salesProductComponentRepository) {
        this.salesProductComponentRepository = salesProductComponentRepository;
    }

//    @Autowired
//    public void setProductRepository2(ProductRepository2 productRepository2) {
//        this.productRepository2 = productRepository2;
//    }

    @Autowired
    public void setProductChangeReplyRepository(ProductChangeReplyRepository productChangeReplyRepository) {
        this.productChangeReplyRepository = productChangeReplyRepository;
    }

    public ProductServiceImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    // 신규 상품 추가
    public Boolean insertProduct(Product product, List<MultipartFile> imgFiles, String productChangeReply) throws Exception {
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
            // 상품 데이터 저장 DB에 저장
            Long id = productRepository.save(product).getId();
            // DB에 저장된 상품의 id 갖고옴
            Optional<Product> productTemp = productRepository.findById(id);
            Product productSel = productTemp.get();

            // 판매상품 구성품에 해당 상품 정보 저장
            SalesProductComponent salesProductComponent = new SalesProductComponent();
            salesProductComponent.setSkuNo(product.getSkuNo());
            salesProductComponent.setComponentSkuNo(product.getSkuNo());
            salesProductComponent.setProductName(product.getProductName());
            salesProductComponent.setComponentProductName(product.getProductName());
            salesProductComponent.setProductLength(1);
            salesProductComponent.setComponentQuantity(1);
            salesProductComponent.setComponentNumber(1);

            salesProductComponentRepository.save(salesProductComponent);

            System.out.println("111111111111" );

            // 제품 변경내역이 있으면 제품 변경내역 저장
            if(!productChangeReply.isEmpty()){
                ProductChangeReply tmpProductChangeReply = new ProductChangeReply();

                // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
                LocalDate now = LocalDate.now();

                tmpProductChangeReply.setProduct(productSel);
                tmpProductChangeReply.setProductChangeReply(productChangeReply);
                tmpProductChangeReply.setReplyDate(now);
                productChangeReplyRepository.save(tmpProductChangeReply);
                productSel.addReply(tmpProductChangeReply);
            }

            // 이미지 파일이 있을 때만 실행함.
            if(!productImageList.isEmpty()) {
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

    // 상품 수정
    public Boolean updateProduct(Product product, List<MultipartFile> imgFiles, List<Long> imgId, String productChangeReply) throws Exception {
        System.out.println("검수 수정 서비스 : " + product);
        System.out.println("검수 수정 서비스222 : " + imgFiles);

        boolean check = false;

        try{
            // 기존에 등록된 이미지가 있는지 확인
            boolean existImg = productImageRepository.existsByProductId(product.getId());

            // 상품 리플 정보 있는지 확인
            boolean existReply = productChangeReplyRepository.existsByProductId(product.getId());

            // 상품 변경 정보 있으면 저장
            if(existReply){
                Optional<List<ProductChangeReply>> productChangeReplies = productChangeReplyRepository.findByProductId(product.getId());
                List<ProductChangeReply> tmpProductChangeReply = productChangeReplies.get();
                for (ProductChangeReply reply: tmpProductChangeReply) {
                    product.addReply(reply);
                }
            }

            // 제품 변경내역이 있으면 제품 변경내역 저장
            if(!productChangeReply.isEmpty()){
                ProductChangeReply tmpProductChangeReply = new ProductChangeReply();

                // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
                LocalDate now = LocalDate.now();

                tmpProductChangeReply.setId(null);
                tmpProductChangeReply.setProduct(product);
                tmpProductChangeReply.setProductChangeReply(productChangeReply);
                tmpProductChangeReply.setReplyDate(now);
                productChangeReplyRepository.save(tmpProductChangeReply);
                product.addReply(tmpProductChangeReply);
            }

            // 기존에 등록된 이미지 확인
            if(existImg){
                Optional<List<ProductImage>> exiImg = productImageRepository.findByProductId(product.getId());

                // 사용자가 모든 파일 삭제했을 경우
                if(imgId == null){
                    for (ProductImage image : exiImg.get()) {
                        File file = new File(image.getImgFilePath());
                        file.delete();

                        // DB의 파일 삭제
                        productImageRepository.deleteById(image.getId());
                    }
                }
                else if(exiImg.get().size() == imgId.size()){
                    for(ProductImage image : exiImg.get()){
                        product.addPhoto(image);
                    }
                } else{
                    // 경로에 지정된 파일 삭제. 사용자가 삭제하지 않은 파일은 삭제 안 함
                    for(ProductImage image : exiImg.get()){
                        boolean checkImg = false;
                        for(long tempId : imgId){
                            if(image.getId().equals(tempId)){
                                checkImg = true;
                                break;
                            }
                        }
                        // 사용자가 삭제한 파일만 삭제
                        if(!checkImg){
                            // 경로에 있는 파일 삭제
                            System.out.println("사용자가 삭제한 이미지 삭제");
                            System.out.println(image);
                            File file = new File(image.getImgFilePath());
                            file.delete();

                            // DB의 파일 삭제
                            productImageRepository.deleteById(image.getId());
                        }
                        // 사용자가 삭제하지 않은 파일은 다시 저장
                        else{
                            Optional<ProductImage> saveImg = productImageRepository.findById(image.getId());
                            ProductImage tempImg = saveImg.get();
                            tempImg.setProduct(product);
                            product.addPhoto(tempImg);
                        }
                    }
                }
                // 저장 성공
                check = true;
            }

            // 새로운 이미지 파일이 있다면
            if(imgFiles != null){
                // 이미지 파일 지정
                ProductImage tmpImage = new ProductImage();
                List<ProductImage> productImageList = fileHandler.parseFileInfo(imgFiles, tmpImage);

                System.out.println("이미지 있음");

                // 이미지 파일 저장.
                for(ProductImage image : productImageList) {

                    System.out.println("검수 추가 서비스33333 : " + productImageList);

                    image.setProduct(product);
                    product.addPhoto(image);

                    System.out.println("image : " + image);

                    // image DB에 저장
                    productImageRepository.save(image);
                }

            }

            // 상품 업데이트
            productRepository.save(product);

            // 판매 구성품 수정
            String skuNo = product.getSkuNo();
            String productName = product.getProductName();
            salesProductComponentRepository.updateProduct(skuNo, productName);

            // 저장 성공
            check = true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            check=false;
        }


        return check;
    }

    // 상품 삭제하기
    public Boolean deleteProduct(long id){
        System.out.println("상품 삭제 서비스 : " + id);

        // 상품 데이터 있는지 확인
        boolean check = checkId(id);

        // skuNo 조회
        String skuNo = productRepository.findById(id).get().getSkuNo();

        // 조회된 데이터 없을 경우
        if(!check){
            System.out.println("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 삭제 진행.");

            // 검수 데이터 먼저 삭제 처리.
            if(inspectServiceImpl.findProductInspect(id)){
                System.out.println("검수 삭제.");
                Optional<List<Inspect>> tmpInspect = inspectServiceImpl.findInspectProductList(id);

                System.out.println(tmpInspect);

                List<Inspect> inspect = tmpInspect.get();
                for(int i=0; i<inspect.size(); i++){
                    inspectServiceImpl.deleteInspect(inspect.get(i).getId());
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
            }

            // 제품 변경 내역 삭제(리플)
            productChangeReplyRepository.deleteByProductId(id);

            // 상품 정보 삭제
            productRepository.deleteById(id);

            // 판매 구성품 정보 삭제
            salesProductComponentRepository.deleteBySkuNo(skuNo);

            return true;
        }
    }

    // 일부 상품 조회. 조건에 따른 검색 진행
//    public Optional<List<Product>> findProduct(String skuNo, String productName,
//                                               String brandName, String maker, Set<String> tempClassName, String page, long productCurseId) {
//
//        System.out.println("ProductServicrImpl");
//        System.out.println("productName : " + productName );
//        System.out.println("tempSelectChk : " + tempClassName );
//
//        // 모든 데이터 조회
//        if(page.equals("0")){
//            System.out.println("모든 데이터 조회");
//            if(tempClassName.size() < 1){
//                // 분류 조회 하지 않을 경우
//                return (Optional<List<Product>>) productRepository.findByProductList(
//                        skuNo, productName, brandName, maker);
//            } else{
//                System.out.println("제품분류 있음.");
//                // 분류 조회할 경우
//                return (Optional<List<Product>>) productRepository.findByProductListAndClassName(
//                        skuNo, productName, brandName, maker, tempClassName);
//            }
//        }
//        // 처음 페이지 조회
//        else if(page.equals("1")){
//            System.out.println("모든 데이터 조회");
//            if(tempClassName.size() < 1){
//                // 분류 조회 하지 않을 경우
//                return (Optional<List<Product>>) productRepository.findByProductListFirst(
//                        skuNo, productName, brandName, maker);
//            } else{
//                System.out.println("제품분류 있음.");
//                // 분류 조회할 경우
//                return (Optional<List<Product>>) productRepository.findByProductListAndClassNameFirst(
//                        skuNo, productName, brandName, maker, tempClassName);
//            }
//        }
//        // 커서에 맞춰 페이지 조회
//        else{
//            System.out.println("모든 데이터 조회");
//            if(tempClassName.size() < 1){
//                // 분류 조회 하지 않을 경우
//                return (Optional<List<Product>>) productRepository.findByProductListCurse(
//                        skuNo, productName, brandName, maker, productCurseId);
//            } else{
//                System.out.println("제품분류 있음.");
//                // 분류 조회할 경우
//                return (Optional<List<Product>>) productRepository.findByProductListAndClassNameCurse(
//                        skuNo, productName, brandName, maker, tempClassName, productCurseId);
//            }
//        }
//
////        // 분류 검색 구분에 따른 조회 방법을 다르게 함
////        if(tempClassName.size() < 1){
////            System.out.println("제품분류 없음.");
////            // 분류 조회 하지 않을 경우
//////            return (Optional<List<Product>>) productRepository
//////                .findBySkuNoContainingAndProductNameContainingAndBrandNameContainingAndMakerContainingIgnoreCase(
//////                     skuNo, productName, brandName, maker);
////
////
////            // test 중 //13.226초 434개 조회
////            return (Optional<List<Product>>) productRepository
////                .findByProductList(
////                     skuNo, productName, brandName, maker);
//////
//////            return (Optional<List<Product>>) productRepository
//////                .findByProductList();
////
////            //test 중 //9.6초 434개 조회
//////            return Optional.ofNullable(productRepository2.findByProductList());
////        }else{
////            System.out.println("제품분류 있음.");
////            // 분류 조회할 경우
////            return (Optional<List<Product>>) productRepository
////                    .findByProductListAndClassName(skuNo, productName, brandName, maker, tempClassName);
////        }
//    }


    // 처음 조회 시 진행.
    public Optional<List<Product>> findProduct(String skuNo, String productName,
                                               String brandName, String maker, Set<String> tempClassName) {

        System.out.println("ProductServicrImpl");
        System.out.println("productName : " + productName );
        System.out.println("tempSelectChk : " + tempClassName );

        if(tempClassName.size() < 1){
            // 분류 조회 하지 않을 경우
            return (Optional<List<Product>>) productRepository.findByProductListFirst(
                    skuNo, productName, brandName, maker);
        } else{
            System.out.println("제품분류 있음.");
            // 분류 조회할 경우
            return (Optional<List<Product>>) productRepository.findByProductListAndClassNameFirst(
                    skuNo, productName, brandName, maker, tempClassName);
        }
    }

    // excel 다운용
    public Optional<List<Product>> findProductExcel(String skuNo, String productName,
                                               String brandName, String maker, Set<String> tempClassName) {

        System.out.println("ProductServicrImpl");
        System.out.println("productName : " + productName );
        System.out.println("tempSelectChk : " + tempClassName );

        if(tempClassName.size() < 1){
            // 분류 조회 하지 않을 경우
            return (Optional<List<Product>>) productRepository.findByProductListExcel(
                    skuNo, productName, brandName, maker);
        } else{
            System.out.println("제품분류 있음.");
            // 분류 조회할 경우
            return (Optional<List<Product>>) productRepository.findByProductListAndClassNameExcel(
                    skuNo, productName, brandName, maker, tempClassName);
        }
    }

    // page에 따라 진행
    public Optional<List<Product>> findCurseProduct(Long[] productCurseId) {
        return (Optional<List<Product>>) productRepository.findByProductListCurse(
                productCurseId);
    }

    // 모든 상품 검색
    public List<Long> findAllProductId(String skuNo, String productName,
                                               String brandName, String maker, Set<String> tempClassName) {

        System.out.println("ProductServicrImpl");
        System.out.println("productName : " + productName );
        System.out.println("tempSelectChk : " + tempClassName );

        // 모든 데이터 조회
        if(tempClassName.size() < 1){
            // 분류 조회 하지 않을 경우
            return productRepository.findByProductList(
                    skuNo, productName, brandName, maker);
        } else {
            System.out.println("제품분류 있음.");
            // 분류 조회할 경우
            return productRepository.findByProductListAndClassName(
                    skuNo, productName, brandName, maker, tempClassName);
        }
    }

    // 검색 제품 카운트 조회
    public Long findSelectProductCnt(String skuNo, String productName,
                                     String brandName, String maker, Set<String> tempClassName) {

        System.out.println("ProductServiceImpl, findSelectProductCnt");

        if(tempClassName.size() < 1){
            // 분류 조회 하지 않을 경우
            return productRepository.findByProductCnt(
                    skuNo, productName, brandName, maker);
        } else{
            System.out.println("제품분류 있음.");
            // 분류 조회할 경우
            return productRepository.findByProductCntClassName(
                    skuNo, productName, brandName, maker, tempClassName);
        }
    }

    // 검색 제품 Id 모두 조회
    public List<Long> findSelectId(String skuNo, String productName,
                                     String brandName, String maker, Set<String> tempClassName) {

        System.out.println("ProductServiceImpl, findSelectId");

        if(tempClassName.size() < 1){
            // 분류 조회 하지 않을 경우
            return productRepository.findById(
                    skuNo, productName, brandName, maker);
        } else{
            System.out.println("제품분류 있음.");
            // 분류 조회할 경우
            return productRepository.findByIdClassName(
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

    // 제품 변경 리플 내용 확인
    public Boolean checkReplyId(long replyId){
        boolean check = productChangeReplyRepository.existsById(replyId);
        return check;
    }

    // 제품 변경 리플 내용 수정
    public Boolean updateProductReply(ReplyDTO productReplyDTO) throws Exception{

        System.out.println("ProductServicrImpl");
        System.out.println("제품 변경 리플 내용 수정");
        Optional<Product> OptProduct = productRepository.findById(productReplyDTO.getId());
        Product product = OptProduct.get();

        Optional<List<ProductChangeReply>> tmpProductChangeReply = productChangeReplyRepository.findByProductId(productReplyDTO.getId());

        List<ProductChangeReply> productChangeReplyList = tmpProductChangeReply.get();

        int proReplySize = productChangeReplyList.size();
        int chReplySize = productReplyDTO.getReplyDataList().size();

        // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
        LocalDate now = LocalDate.now();

        // 등록된 변경내역 리플들을 갖고옴
        for (ProductChangeReply productChangeReply: productChangeReplyList) {
            for(int j = 0; j<chReplySize; j++){
                // 수정된 리플의 데이터만 변경해서 저장함.
                if(productChangeReply.getId().equals(productReplyDTO.getReplyDataList().get(j).getReplyId())){
                    productChangeReply.setProduct(product);
                    productChangeReply.setReplyDate(now);
                    productChangeReply.setProductChangeReply(productReplyDTO.getReplyDataList().get(j).getChangeReplyData());

                    break;
                }
            }
            product.addReply(productChangeReply);
            productChangeReplyRepository.save(productChangeReply);
        }

        productRepository.save(product);
        return true;
    }


    // 제품 변경 리플 내용 삭제
    public Boolean deleteProductReply(Long productId, Long[] productReplyId) throws Exception{

        System.out.println("ProductServicrImpl");
        System.out.println("제품 변경 리플 내용 삭제");
        Optional<Product> OptProduct = productRepository.findById(productId);
        Product product = OptProduct.get();

        Optional<List<ProductChangeReply>> tmpProductChangeReply = productChangeReplyRepository.findByProductId(productId);

        List<ProductChangeReply> productChangeReplyList = tmpProductChangeReply.get();

        int chReplySize = productReplyId.length;

        // 등록된 변경내역 리플들을 갖고옴
        for (ProductChangeReply productChangeReply: productChangeReplyList) {
            boolean chkDelData = false;
            for(int j = 0; j<chReplySize; j++){
                // 수정된 리플의 데이터만 삭제
                if(productChangeReply.getId().equals(productReplyId[j])){
                    chkDelData = true;
                    productChangeReplyRepository.deleteById(productChangeReply.getId());
                    break;
                }
            }

            // 삭제되지 않은 데이터 저장
            if(!chkDelData){
                product.addReply(productChangeReply);
                productChangeReplyRepository.save(productChangeReply);
            }
        }

        productRepository.save(product);
        return true;
    }
}
