package com.vue_spring.demo.service;

import com.vue_spring.demo.DAO.InspectExcelInterfaceDAO;
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

import java.io.File;
import java.util.*;

@Repository
@Service
public class InspectServiceImpl implements InspectService {

    @Autowired
    private InspectRepository inspectRepository;
    @Autowired
    private InspectImageRepository inspectImageRepository;

    private final FileHandler fileHandler;

    public InspectServiceImpl(FileHandler fileHandler) {
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
        InspectImage tmpImage = new InspectImage();
        List<InspectImage> inspectList = fileHandler.parseFileInfo(imgFiles, tmpImage);

        System.out.println(inspect);
        System.out.println(inspectList);

        // JPA에 저장되는지 확인. 기본 값은 저장 실패로 해놓는다.
        boolean check = false;

        try{
            // 검수 데이터 저장 DB에 저장
            Long id = inspectRepository.save(inspect).getId();

            // 이미지 파일이 있을 때만 실행함.
            if(!inspectList.isEmpty()) {

                // DB에 저장된 검수 내용 조회
                Inspect saveInspect = inspectRepository.findById(id).get();

                for(InspectImage image : inspectList) {

                    // 이미지 파일들에 검수정보 저장(inspect_id)
                    image.setInspect(saveInspect);

                    // image DB에 저장
                    inspectImageRepository.save(image);

                    // DB에 저장된 검수 내용에 사진 정보 저장
                    saveInspect.addPhoto(image);
                }
                // DB에 사진정보 추가한 상태로 다시 저장
                inspectRepository.save(saveInspect);

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

    // 검수 수정
    public Boolean updateInspect(Inspect inspect, List<MultipartFile> imgFiles, List<Long> imgId) throws Exception {
        System.out.println("검수 수정 서비스 : " + inspect);
        System.out.println("검수 수정 서비스222 : " + imgFiles);

        boolean check = false;

        // 기존에 등록된 이미지가 있는지 확인
        boolean existImg = inspectImageRepository.existsByInspectId(inspect.getId());

        // 기존에 등록된 이미지가 있으면 삭제
        try{
            if(existImg) {
                Optional<List<InspectImage>> exiImg = inspectImageRepository.findByInspectId(inspect.getId());

                // 사용자가 모든 파일 삭제했을 경우
                if(imgId == null){
                    for (InspectImage image : exiImg.get()) {
                        File file = new File(image.getImgFilePath());
                        file.delete();

                        // DB의 파일 삭제
                        inspectImageRepository.deleteById(image.getId());
                    }
                }
                // 경로에 지정된 파일 삭제. 사용자가 삭제하지 않은 파일은 삭제 안 함
                else if(exiImg.get().size() == imgId.size()) {
                    for(InspectImage image : exiImg.get()){
                        inspect.addPhoto(image);
                    }
                }
                else {
                    for (InspectImage image : exiImg.get()) {
                        boolean checkImg = false;
                        for (long tempId : imgId) {
                            if (image.getId().equals(tempId)) {
                                checkImg = true;
                                break;
                            }
                        }
                        // 사용자가 삭제한 파일만 삭제
                        if (!checkImg) {
                            // 경로에 있는 파일 삭제
                            System.out.println("사용자가 삭제한 이미지 삭제");
                            System.out.println(image);
                            File file = new File(image.getImgFilePath());
                            file.delete();

                            // DB의 파일 삭제
                            inspectImageRepository.deleteById(image.getId());
                        }
                        // 사용자가 삭제하지 않은 파일은 다시 저장
                        else {
                            Optional<InspectImage> saveImg = inspectImageRepository.findById(image.getId());
                            InspectImage tempImg = saveImg.get();
                            tempImg.setInspect(inspect);
                            inspect.addPhoto(tempImg);
                        }
                    }
                }
            }

            // 새로운 이미지 파일이 있다면
            if(imgFiles != null){
                // 이미지 파일 지정
                InspectImage tmpImage = new InspectImage();
                List<InspectImage> inspectList = fileHandler.parseFileInfo(imgFiles, tmpImage);

                System.out.println("이미지 있음");

                // 이미지 파일 저장.
                for(InspectImage image : inspectList) {

                    System.out.println("검수 추가 서비스33333 : " + inspectList);
                    System.out.println("검수 추가 서비스44444 : " + inspect);

                    image.setInspect(inspect);
                    inspect.addPhoto(image);

                    System.out.println("image : " + image);

                    // image DB에 저장
                    inspectImageRepository.save(image);
                }
            }

            // inspect 업데이트
            inspectRepository.save(inspect);

            // 저장 성공
            check = true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            check=false;
        }

        return check;
    }

    // 검수 삭제하기
    public Boolean deleteInspect(long id){
        System.out.println("검수 삭제 서비스 : " + id);

        // 검수 데이터 있는지 확인
        boolean check = checkInspect(id);

        // 조회된 데이터 없을 경우
        if(!check){
            System.out.println("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 삭제 진행.");

            // 기존에 등록된 이미지가 있는지 확인
            boolean existImg = inspectImageRepository.existsByInspectId(id);

            // 기존에 등록된 이미지가 있으면 삭제
            if(existImg){
                Optional<List<InspectImage>> deleteImg = inspectImageRepository.findByInspectId(id);
                // 경로에 지정된 파일 삭제
                for(InspectImage image : deleteImg.get()){
                    File file = new File(image.getImgFilePath());
                    file.delete();
                }
                // DB의 파일 삭제
                inspectImageRepository.deleteByInspectId(id);
                inspectRepository.deleteById(id);
            }

            return true;
        }
    }

    // 모든 상품 검색
    public List<Inspect> findProductAll() {
        return (List<Inspect>) inspectRepository.findAll();
    }

    // 조건에 맞는 모든 검수 검색(엑셀용)
    public Optional<List<InspectExcelInterfaceDAO>> findInspectAllDate(Date beforeDate, Date afterDate) {
        return inspectRepository.findByInspectDateBetween(beforeDate, afterDate);
    }

    // 조건에 맞는 모든 검수 검색(엑셀용)
    public Optional<List<InspectExcelInterfaceDAO>> findInspectAllDate(List<Long> products,Date beforeDate, Date afterDate) {
        return inspectRepository.findByProductInspectList(products, beforeDate, afterDate);
    }

    // 일부 상품 조회. 조건에 따른 검색 진행
    public List<Long> findInspect(List<Long> productId, Date beforeDate, Date afterDate) {

        System.out.println("InspectServicrImpl 검수 조회");

        // Optional 빈 객체 생성
        List<Long> inspectList = new ArrayList<>();

        // ProductId 값에 따라 데이터 조회
        for(int i = 0; i< productId.size(); i++){
            // Inspect 테이블에 조회 및 저장
            System.out.println("Inspect 테이블에 값이 있다면 id 값들 가져옴 저장");
            List<Long> tmpInspectList = inspectRepository.findByProductIdInspectList(productId.get(i), beforeDate, afterDate);

            for(int j = 0; j < tmpInspectList.size(); j++){
                inspectList.add(tmpInspectList.get(j));
            }
        }

        return inspectList;
    }

    // 일부 상품 조회. 날짜만 진행(처음 조회)
    public Optional<List<Inspect>> findInspect(Date beforeDate, Date afterDate) {

        System.out.println("처음 조회. 15개만 검수 조회");

        return inspectRepository.findByInspectListAndClassNameFirst(beforeDate, afterDate);
    }

    // 조건에 맞는 상품 조회
    public Optional<List<Inspect>> findInspect(Long[] inspectCurseId) {

        System.out.println("15개만 검수 조회");

        return inspectRepository.findByInspectCurseId(inspectCurseId);
    }

    // 처음 조회 시 아이디만 검색(분류 없음. 날짜만 검색)
    public List<Long> findSelectId(Date beforeDate, Date afterDate){
        return inspectRepository.findById(beforeDate, afterDate);
    }

    // 검수 중복 확인
    public Boolean checkInspect(Product product, Date inspectDate){
        boolean check = inspectRepository.existsByProductAndInspectDate(product, inspectDate);
        return check;
    }
    
    // 검수 검색
    public Optional<Inspect> findInspect(long id){
        return (Optional<Inspect>) inspectRepository.findById(id);
    }

    // 검색양 조회
//    public Long findSelectInspectCnt(Date beforeDate, Date afterDate){
//
//    }

    // 검수 있는지 확인
    public Boolean checkInspect(long id){
        boolean check = inspectRepository.existsById(id);
        return check;
    }

    // 상품id로 된 검수 내용 있는지 확인
    public Boolean findProductInspect(long id){
        return inspectRepository.existsByProductId(id);
    }

    public Optional<List<Inspect>> findInspectProductList(long id){
        return inspectRepository.findByProductId(id);
    }


}
