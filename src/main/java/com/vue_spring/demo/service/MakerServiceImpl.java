package com.vue_spring.demo.service;

import com.vue_spring.demo.Repository.MakerRepository;
import com.vue_spring.demo.model.Maker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
@Service
public class MakerServiceImpl implements MakerService {

    @Autowired
    private MakerRepository makerRepository;

     @Autowired
     public void setProductRepository(MakerRepository makerRepository) {
         this.makerRepository = makerRepository;
     }


     // 신규 제조사 추가
    public Boolean insertMaker(Maker maker){
        System.out.println("제조사 추가 서비스 : " + maker);

        try{
            makerRepository.save(maker);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 제조사 정보 수정
    public Boolean updateMaker(Maker maker){
        System.out.println("제조사 정보 수정 서비스 : " + maker);

        // 제조사 조회 확인
        boolean check = checkId(maker.getId());

        // 조회된 데이터 없을 경우
        if(!check){
            System.out.println("데이터 없음. 수정 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 수정 진행.");
            makerRepository.save(maker);
            return true;
        }
    }

    // 제조사 삭제하기
    public Boolean deleteMaker(long id){
        System.out.println("제조사 삭제 서비스: " + id);

        // 제조사 조회 확인
        boolean check = checkId(id);

        // 조회된 데이터 없을 경우
        if(!check){
            System.out.println("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            System.out.println("데이터 있음. 삭제 진행.");
            makerRepository.deleteById(id);
            return true;
        }
    }

    // 제조사 조회. 조건에 따른 검색 진행
    public Optional<List<Maker>> findMaker(String makerName,
                                           String makerAddress,
                                           String makerPerson,
                                           String makerPhone,
                                           Set<String> businessType){

        System.out.println("MakerServiceImpl 제조사 조회");
        System.out.println("makerName : " + makerName );
        System.out.println("tempBusinessType : " + businessType );

        // 분류 검색 구분에 따른 조회 방법을 다르게 함
        if(businessType.size() < 1){
            System.out.println("제품분류 없음.");
            // 분류 조회 하지 않을 경우
            return (Optional<List<Maker>>) makerRepository
                .findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCase(
                        makerName, makerAddress, makerPerson, makerPhone);
        }else{
            System.out.println("제품분류 있음.");
            // 분류 조회할 경우
            return (Optional<List<Maker>>) makerRepository
                    .findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCaseAndtempBusinessType(
                            makerName, makerAddress, makerPerson, makerPhone, businessType);
        }
    }

    // 제품 중복 확인
    public Boolean checkId(long makerId){
        boolean check = makerRepository.existsById(makerId);
        return check;
    }
}
