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

import java.io.File;
import java.util.*;

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
                Inspect inspectSelect = inspectTemp.get();
                
                for(InspectImage image : inspectList) {

                    System.out.println("검수 추가 서비스33333 : " + inspectList);
                    System.out.println("검수 추가 서비스44444 : " + inspect);

                    // 이미지 데이터가 존재할 경우 진행
                    if(!inspectTemp.isEmpty()){
                        
                        System.out.println("inspectTemp : " + inspectSelect);

                        // 이미지 파일들에 검수정보 저장(inspect_id)
                        image.setInspect(inspectSelect);

                        System.out.println("image : " + image);

                        // image DB에 저장
                        inspectImageRepository.save(image);

                        // DB에 저장된 검수 내용에 사진 정보 저장
                        inspectSelect.addPhoto(image);
                    }
                }

                // DB에 사진정보 추가한 상태로 다시 저장
                inspectRepository.save(inspectSelect);

                // 저장 성공
                check = true;
            }
            // 이미지 파일이 없을 경우 검수 내용만 저장
            else{
                inspectRepository.save(inspect);
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
    public Boolean updateInspect(Inspect inspect, List<MultipartFile> imgFiles) throws Exception {
        System.out.println("검수 수정 서비스 : " + inspect);
        System.out.println("검수 수정 서비스222 : " + imgFiles);

        boolean check = false;

        // 기존에 등록된 이미지가 있는지 확인
        boolean existImg = inspectImageRepository.existsByInspectId(inspect.getId());

        // 새로운 이미지 파일이 있다면
        if(imgFiles != null){
            // 이미지 파일 지정
            List<InspectImage> inspectList = fileHandler.parseFileInfo(imgFiles);

            System.out.println("이미지 있음");

            // 기존에 등록된 이미지가 있으면 삭제
            if(existImg){
                Optional<List<InspectImage>> deleteImg = inspectImageRepository.findByInspectId(inspect.getId());
                // 경로에 지정된 파일 삭제
                for(InspectImage image : deleteImg.get()){
                    File file = new File(image.getImgFilePath());
                    file.delete();
                }
                // DB의 파일 삭제
                inspectImageRepository.deleteByInspectId(inspect.getId());
            }
            try{
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

                // inspect 업데이트
                inspectRepository.save(inspect);

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
                Optional<List<InspectImage>> inspectList = inspectImageRepository.findByInspectId(inspect.getId());
                for(InspectImage image : inspectList.get()){
                    inspect.addPhoto(image);
                }
            }
            // inspect 업데이트
            inspectRepository.save(inspect);
            // 저장 성공
            check = true;
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

    // 일부 상품 조회. 조건에 따른 검색 진행
    public Optional<List<Inspect>> findInspect(List<Long> productId, Date beforeDate, Date afterDate) {

        System.out.println("InspectServicrImpl 검수 조회");

        // Optional 빈 객체 생성
        Optional<List<Inspect>> tmpInspectList = Optional.empty();
        List<Inspect> inspectList = new ArrayList<>();

        // ProductId 값에 따라 데이터 조회
        for(int i = 0; i< productId.size(); i++){
            // Inspect 테이블에 조회 및 저장
            System.out.println("Inspect 테이블에 값이 있다면 데이터 저장");
            tmpInspectList = inspectRepository.findByProductIdAndInspectDateBetween(productId.get(i), beforeDate, afterDate);

            inspectList.addAll(tmpInspectList.orElse(null));
        }

        return Optional.of(inspectList);
    }

    // 제품 중복 확인
    public Boolean checkInspect(Product product, Date inspectDate){
        boolean check = inspectRepository.existsByProductAndInspectDate(product, inspectDate);
        return check;
    }
    
    // 제품 검색
    public Optional<Inspect> findInspect(long id){
        return (Optional<Inspect>) inspectRepository.findById(id);
    }

    // 제품 있는지 확인
    public Boolean checkInspect(long id){
        boolean check = inspectRepository.existsById(id);
        return check;
    }
}
