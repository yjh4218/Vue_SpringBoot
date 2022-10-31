package com.vue_spring.demo.service;

import com.vue_spring.demo.Repository.ClaimImageRepository;
import com.vue_spring.demo.Repository.ClaimRepository;
import com.vue_spring.demo.component.FileHandler;
import com.vue_spring.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Service
public class ClaimServiceImpl implements ClaimService {

    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private ClaimImageRepository claimImageRepository;

    private final FileHandler fileHandler;

    public ClaimServiceImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Autowired
     public void setClaimRepository(ClaimRepository claimRepository) {
         this.claimRepository = claimRepository;
     }

     // 클레임 추가
    public Boolean insertClaim(Claim claim, List<MultipartFile> imgFiles) throws Exception {
        System.out.println("클레임 추가 서비스 : " + claim);
        System.out.println("클레임 추가 서비스222 : " + imgFiles);

        // 이미지 파일 경로 지정
        ClaimImage tmpImage = new ClaimImage();
        List<ClaimImage> claimImageList = fileHandler.parseFileInfo(imgFiles, tmpImage);

        System.out.println(claim);
        System.out.println(claimImageList);

        // JPA에 저장되는지 확인. 기본 값은 저장 실패로 해놓는다.
        boolean check = false;

        try{
            // 클레임 데이터 저장 DB에 저장
            Long id = claimRepository.save(claim).getId();
            // DB에 저장된 클레임 내용 조회
            Optional<Claim> claimTemp = claimRepository.findById(id);

            // 이미지 파일이 있을 때만 실행함.
            if(!claimImageList.isEmpty()) {
                Claim claimSelect = claimTemp.get();
                
                for(ClaimImage image : claimImageList) {

                    // 이미지 데이터가 존재할 경우 진행
                    if(!claimTemp.isEmpty()){

                        // 이미지 파일들에 클레임정보 저장
                        image.setClaim(claimSelect);

                        // image DB에 저장
                        claimImageRepository.save(image);

                        // DB에 저장된 클레임 내용에 사진 정보 저장
                        claimSelect.addPhoto(image);
                    }
                }
                // DB에 사진정보 추가한 상태로 다시 저장
                claimRepository.save(claimSelect);

                // 저장 성공
                check = true;
            }
            // 이미지 파일이 없을 경우 클레임 내용만 저장
            else{
                claimRepository.save(claim);
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

    // 클레임 수정
    public Boolean updateClaim(Claim claim, List<MultipartFile> imgFiles, List<Long> imgId) throws Exception {
        System.out.println("클레임 수정 서비스 : " + claim);
        System.out.println("클레임 수정 서비스222 : " + imgFiles);

        boolean check = false;

        // 기존에 등록된 이미지가 있는지 확인
        boolean existImg = claimImageRepository.existsByClaimId(claim.getId());

        // 새로운 이미지 파일이 있다면
        if(imgFiles != null){
            // 이미지 파일 지정
            ClaimImage tmpImage = new ClaimImage();
            List<ClaimImage> claimImageList = fileHandler.parseFileInfo(imgFiles, tmpImage);

            System.out.println("이미지 있음");

            // 기존에 등록된 이미지가 있으면 삭제
            if(existImg){
                Optional<List<ClaimImage>> deleteImg = claimImageRepository.findByClaimId(claim.getId());
                // 경로에 지정된 파일 삭제
                for(ClaimImage image : deleteImg.get()){
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
                        claimImageRepository.deleteById(image.getId());
                    }
                    // 사용자가 삭제하지 않은 파일은 다시 저장
                    else{
                        Optional<ClaimImage> saveImg = claimImageRepository.findById(image.getId());
                        ClaimImage tempImg = saveImg.get();
                        tempImg.setClaim(claim);
                        claim.addPhoto(tempImg);
                    }
                }
            }
            try{
                // 이미지 파일 저장.
                for(ClaimImage image : claimImageList) {

                    System.out.println("클레임 수정 서비스33333 : " + claimImageList);
                    System.out.println("클레임 수정 서비스44444 : " + claim);

                    image.setClaim(claim);
                    claim.addPhoto(image);

                    System.out.println("image : " + image);

                    // image DB에 저장
                    claimImageRepository.save(image);
                }

                // claim 업데이트
                claimRepository.save(claim);

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
                Optional<List<ClaimImage>> claimImageList = claimImageRepository.findByClaimId(claim.getId());
                for(ClaimImage image : claimImageList.get()){
                    claim.addPhoto(image);
                }
            }
            // claim 업데이트
            claimRepository.save(claim);
            // 저장 성공
            check = true;
        }

        return check;
    }

    // 클레임 삭제하기
    public Boolean deleteClaim(long id){
        System.out.println("클레임 삭제 서비스 : " + id);

        // 클레임 데이터 있는지 확인
        boolean check = checkClaim(id);

        // 조회된 데이터 없을 경우
        if(!check){
            System.out.println("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 삭제 진행.");

            // 기존에 등록된 이미지가 있는지 확인
            boolean existImg = claimImageRepository.existsByClaimId(id);

            // 기존에 등록된 이미지가 있으면 삭제
            if(existImg){
                Optional<List<ClaimImage>> deleteImg = claimImageRepository.findByClaimId(id);
                // 경로에 지정된 파일 삭제
                for(ClaimImage image : deleteImg.get()){
                    File file = new File(image.getImgFilePath());
                    file.delete();
                }
                // DB의 파일 삭제
                claimImageRepository.deleteByClaimId(id);
            }
            claimRepository.deleteById(id);
            return true;
        }
    }

    // 모든 클레임 검색
    public List<Claim> findProductAll() {
        return (List<Claim>) claimRepository.findAll();
    }

    // 일부 클레임 조회. 조건에 따른 검색 진행
    public Optional<List<Claim>> findClaim(List<Long> productId, Date beforeDate, Date afterDate) {

        System.out.println("ClaimServicrImpl 클레임 조회");

        // Optional 빈 객체 생성
        Optional<List<Claim>> tmpClaimList = Optional.empty();
        List<Claim> claimList = new ArrayList<>();

        // ProductId 값에 따라 데이터 조회
        for(int i = 0; i< productId.size(); i++){
            // Claim 테이블에 조회 및 저장
            System.out.println("claim 테이블에 값이 있다면 데이터 저장");
            tmpClaimList = claimRepository.findByProductIdAndClaimDateBetween(productId.get(i), beforeDate, afterDate);

            claimList.addAll(tmpClaimList.orElse(null));
        }

        return Optional.of(claimList);
    }

    // 일부 상품 조회. 날짜만 진행
    public Optional<List<Claim>> findClaim(Date beforeDate, Date afterDate) {

        System.out.println("Clai,ServicrImpl 클레임 조회");

        return claimRepository.findByClaimDateBetween(beforeDate, afterDate);
    }
    
    // 클레임 검색
    public Optional<Claim> findClaim(long id){
        return (Optional<Claim>) claimRepository.findById(id);
    }

    // 클레임 있는지 확인
    public Boolean checkClaim(long id){
        boolean check = claimRepository.existsById(id);
        return check;
    }

    // 상품id로 된 클레임 내용 있는지 확인
    public Boolean findProductClaim(long id){
        return claimRepository.existsByProductId(id);
    }

    // 상품에 해당하는 클레임 내용 검색
    public Optional<List<Claim>> findCalimProductList(long id){
        return claimRepository.findByProductId(id);
    }


}
