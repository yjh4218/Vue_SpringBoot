package com.vue_spring.demo.service;

import com.vue_spring.demo.DTO.ReplyDTO;
import com.vue_spring.demo.Repository.MakerAuditFileRepository;
import com.vue_spring.demo.Repository.MakerAuditRepository;
import com.vue_spring.demo.Repository.MakerChangeReplyRepository;
import com.vue_spring.demo.Repository.MakerRepository;
import com.vue_spring.demo.component.FileHandler;
import com.vue_spring.demo.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
@Service
@Slf4j
public class MakerAuditServiceImpl implements MakerAuditService {

    private MakerRepository makerRepository;
    private MakerAuditRepository makerAuditRepository;
    private MakerAuditFileRepository makerAuditFileRepository;


    private final FileHandler fileHandler;

    public MakerAuditServiceImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Autowired
    public void setMakerRepository(MakerRepository makerRepository) {
        this.makerRepository = makerRepository;
    }

    @Autowired
    public void setMakerAuditRepository(MakerAuditRepository makerAuditRepository) {
        this.makerAuditRepository = makerAuditRepository;
    }

    @Autowired
    public void setMakerAuditFileRepository(MakerAuditFileRepository makerAuditFileRepository) {
        this.makerAuditFileRepository = makerAuditFileRepository;
    }

//     제조사 점검 추가
    public Boolean insertMaker(MakerAudit makerAudit, List<MultipartFile> fileData, Long makerId){
        log.info("제조사 점검 추가 서비스");
        log.info(String.valueOf(makerAudit));
        log.info(String.valueOf(fileData));

        // 이미지 파일 경로 지정
        MakerAuditFile makerAuditFile = new MakerAuditFile();
        List<MakerAuditFile> makerAuditFileList = null;
        try {
            makerAuditFileList = fileHandler.parseFileInfo(fileData, makerAuditFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // JPA에 저장되는지 확인. 기본 값은 저장 실패로 해놓는다.
        boolean check = false;

        try{
            // DB에 저장된 제조사 조회
            Maker maker = makerRepository.findById(makerId).get();

            // MakerAudit 저장되어 있는 검수 데이터들 갖고 옴.
            makerAudit.setMaker(maker);
            List<MakerAudit> exiMakerAudit = makerAuditRepository.findByMakerId(makerId).get();

            // 제조사 검수 정보 저장 후 id 가져옴
            long makerAuditId = makerAuditRepository.save(makerAudit).getId();

            // 파일이 있을 때만 실행함.
            if(!makerAuditFileList.isEmpty()) {

                // 제조사 검수 정보 저장 데이터 가져옴
                MakerAudit saveMakerAudit = makerAuditRepository.findById(makerAuditId).get();

                for(MakerAuditFile makerFile : makerAuditFileList) {

                    // 파일들에 검수정보 저장(saveMakerAudit)
                    makerFile.setMakerAudit(saveMakerAudit);

                    // image DB에 저장
                    makerAuditFileRepository.save(makerFile);

                    // DB에 저장된 검수 내용에 사진 정보 저장
                    saveMakerAudit.addFile(makerFile);
                }
                // DB에 사진정보 추가한 상태로 다시 저장
                makerAuditRepository.save(saveMakerAudit);

                // 제조사 점검 정보를 maker에 저장
                maker.addMakerAudit(saveMakerAudit);

            }

            // MakerAudit에 저장되어 있는 데이터들 maker에 다시 저장
            if(exiMakerAudit.size() > 0){
                for(MakerAudit tmpMakerAudit: exiMakerAudit){
                    maker.addMakerAudit(tmpMakerAudit);
                }
            }

            // 제조사 점검 정보 업데이트
            makerRepository.save(maker);

            // 저장 성공
            check = true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            //저장 실패
            check=false;
        }

        return check;
//        return false;
    }
//
//    // 제조사 정보 수정
//    public Boolean updateMaker(Maker maker, String makerChangeContent){
//        System.out.println("제조사 정보 수정 서비스 : " + maker);
//
//        // 제조사 조회 확인
//        boolean check = checkId(maker.getId());
//
//        // 조회된 데이터 없을 경우
//        if(!check){
//            System.out.println("데이터 없음. 수정 불가");
//            return false;
//        }
//        else{
//            System.out.println("데이터 있음. 수정 진행.");
//
//            // 상품 리플 정보 있는지 확인
//            boolean existReply = makerChangeReplyRepository.existsByMakerId(maker.getId());
//
//            // 상품 변경 정보 있으면 저장
//            if(existReply){
//                Optional<List<MakerChangeReply>> makerChangeReplies = makerChangeReplyRepository.findByMakerId(maker.getId());
//                List<MakerChangeReply> tmpMakerChangeReply = makerChangeReplies.get();
//                for (MakerChangeReply reply: tmpMakerChangeReply) {
//                    maker.addReply(reply);
//                }
//            }
//
//            // 제품 변경 내역 존재
//            if(!makerChangeContent.isEmpty()){
//                MakerChangeReply makerChangeReply = new MakerChangeReply();
//
//                // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
//                LocalDate now = LocalDate.now();
//
//                makerChangeReply.setId(null);
//                makerChangeReply.setMaker(maker);
//                makerChangeReply.setMakerChangeReply(makerChangeContent);
//                makerChangeReply.setReplyDate(now);
//                makerChangeReplyRepository.save(makerChangeReply);
//                maker.addReply(makerChangeReply);
//            }
//            makerRepository.save(maker);
//            return true;
//        }
//    }
//
//    // 제조사 삭제하기
//    public Boolean deleteMaker(long id){
//        System.out.println("제조사 삭제 서비스: " + id);
//
//        // 제조사 조회 확인
//        boolean check = checkId(id);
//
//        // 조회된 데이터 없을 경우
//        if(!check){
//            System.out.println("데이터 없음. 삭제 불가");
//            return false;
//        }
//        else{
//            System.out.println("데이터 있음. 삭제 진행.");
//            // 제조사 히스토리 삭제(리플)
//            makerChangeReplyRepository.deleteByMakerId(id);
//            // 제조사 정보 삭제
//            makerRepository.deleteById(id);
//            return true;
//        }
//    }
//
//    // 제조사 조회. 조건에 따른 검색 진행
//    public Optional<List<Maker>> findMaker(String makerName,
//                                           String makerAddress,
//                                           String makerPerson,
//                                           String makerPhone,
//                                           Set<String> className){
//
//        System.out.println("MakerServiceImpl 제조사 조회");
//        System.out.println("makerName : " + makerName );
//        System.out.println("tempBusinessType : " + className );
//
//        // 분류 검색 구분에 따른 조회 방법을 다르게 함
//        if(className.size() < 1){
//            System.out.println("제품분류 없음.");
//            // 분류 조회 하지 않을 경우
//            return (Optional<List<Maker>>) makerRepository
//                .findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCase(
//                        makerName, makerAddress, makerPerson, makerPhone);
//        }else{
//            System.out.println("제품분류 있음.");
//            // 분류 조회할 경우
//            return (Optional<List<Maker>>) makerRepository
//                    .findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCaseAndtempBusinessType(
//                            makerName, makerAddress, makerPerson, makerPhone, className);
//        }
//    }
//
//    // 제품 중복 확인
//    public Boolean checkId(long makerId){
//        boolean check = makerRepository.existsById(makerId);
//        return check;
//    }
//
//    // 제품 변경 리플 내용 수정
//    public Boolean updateMakerReply(ReplyDTO replyDTO) throws Exception{
//
//        System.out.println("ProductServicrImpl");
//        System.out.println("제품 변경 리플 내용 수정");
//        Optional<Maker> optMaker = makerRepository.findById(replyDTO.getId());
//        Maker maker = optMaker.get();
//
//        Optional<List<MakerChangeReply>> tmpMakerChangeReply = makerChangeReplyRepository.findByMakerId(replyDTO.getId());
//
//        List<MakerChangeReply> makerChangeReplyList = tmpMakerChangeReply.get();
//
//        int proReplySize = makerChangeReplyList.size();
//        int chReplySize = replyDTO.getReplyDataList().size();
//
//        // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
//        LocalDate now = LocalDate.now();
//
//        // 등록된 변경내역 리플들을 갖고옴
//        for (MakerChangeReply makerChangeReply: makerChangeReplyList) {
//            for(int j = 0; j<chReplySize; j++){
//                // 수정된 리플의 데이터만 변경해서 저장함.
//                if(makerChangeReply.getId().equals(replyDTO.getReplyDataList().get(j).getReplyId())){
//                    makerChangeReply.setMaker(maker);
//                    makerChangeReply.setReplyDate(now);
//                    makerChangeReply.setMakerChangeReply(replyDTO.getReplyDataList().get(j).getChangeReplyData());
//
//                    break;
//                }
//            }
//            maker.addReply(makerChangeReply);
//            makerChangeReplyRepository.save(makerChangeReply);
//        }
//
//        makerRepository.save(maker);
//        return true;
//    }
//
//
//    // 제품 변경 리플 내용 삭제
//    public Boolean deleteMakerReply(Long makerId, Long[] makerReplyId) throws Exception{
//
//        System.out.println("ProductServicrImpl");
//        System.out.println("제품 변경 리플 내용 삭제");
//        Optional<Maker> optMaker = makerRepository.findById(makerId);
//        Maker maker = optMaker.get();
//
//        Optional<List<MakerChangeReply>> tmpMakerChangeReply = makerChangeReplyRepository.findByMakerId(makerId);
//
//        List<MakerChangeReply> makerChangeReplyList = tmpMakerChangeReply.get();
//
//        int chReplySize = makerReplyId.length;
//
//        // 등록된 변경내역 리플들을 갖고옴
//        for (MakerChangeReply makerChangeReply: makerChangeReplyList) {
//            boolean chkDelData = false;
//            for(int j = 0; j<chReplySize; j++){
//                // 수정된 리플의 데이터만 삭제
//                if(makerChangeReply.getId().equals(makerReplyId[j])){
//                    chkDelData = true;
//                    makerChangeReplyRepository.deleteById(makerChangeReply.getId());
//                    break;
//                }
//            }
//
//            // 삭제되지 않은 데이터 저장
//            if(!chkDelData){
//                maker.addReply(makerChangeReply);
//                makerChangeReplyRepository.save(makerChangeReply);
//            }
//        }
//
//        makerRepository.save(maker);
//        return true;
//    }
}
