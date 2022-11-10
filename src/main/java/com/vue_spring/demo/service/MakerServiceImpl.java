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

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
@Service
public class MakerServiceImpl implements MakerService {

    private MakerRepository makerRepository;
    private MakerChangeReplyRepository makerChangeReplyRepository;
    private MakerAuditRepository makerAuditRepository;
    private MakerAuditFileRepository makerAuditFileRepository;

    @Autowired
    public void setMakerAuditRepository(MakerAuditRepository makerAuditRepository) {
        this.makerAuditRepository = makerAuditRepository;
    }

    @Autowired
    public void setMakerAuditFileRepository(MakerAuditFileRepository makerAuditFileRepository) {
        this.makerAuditFileRepository = makerAuditFileRepository;
    }

    @Autowired
    public void setMakerRepository(MakerRepository makerRepository) {
        this.makerRepository = makerRepository;
    }

    @Autowired
    public void setMakerChangeReplyRepository(MakerChangeReplyRepository makerChangeReplyRepository) {
        this.makerChangeReplyRepository = makerChangeReplyRepository;
    }

    // 신규 제조사 추가
    public Boolean insertMaker(Maker maker, String makerChangeContent) {
        log.info("제조사 추가 서비스 : " + maker);

        try{
            // 제품 변경내역이 있으면 제품 변경내역 저장


            if(!makerChangeContent.isEmpty()){
                // 제조사 데이터 저장 DB에 저장
                Long id = makerRepository.save(maker).getId();
                // DB에 저장된 상품의 id 갖고옴
                Optional<Maker> makerTmp = makerRepository.findById(id);
                Maker makerSel = makerTmp.get();

                MakerChangeReply makerChangeReply = new MakerChangeReply();

                // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
                LocalDate now = LocalDate.now();

                makerChangeReply.setMaker(makerSel);
                makerChangeReply.setMakerChangeReply(makerChangeContent);
                makerChangeReply.setReplyDate(now);
                makerChangeReplyRepository.save(makerChangeReply);
                makerSel.addReply(makerChangeReply);
                makerRepository.save(makerSel);
            } else{
                makerRepository.save(maker);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 제조사 정보 수정
    public Boolean updateMaker(Maker maker, String makerChangeContent){
        log.info("제조사 정보 수정 서비스 : " + maker);

        // 제조사 조회 확인
        boolean check = checkId(maker.getId());

        // 조회된 데이터 없을 경우
        if(!check){
            log.info("데이터 없음. 수정 불가");
            return false;
        }
        else{
            log.info("데이터 있음. 수정 진행.");

            // 상품 리플 정보 있는지 확인
            boolean existReply = makerChangeReplyRepository.existsByMakerId(maker.getId());

            // 상품 변경 정보 있으면 저장
            if(existReply){
                Optional<List<MakerChangeReply>> makerChangeReplies = makerChangeReplyRepository.findByMakerId(maker.getId());
                List<MakerChangeReply> tmpMakerChangeReply = makerChangeReplies.get();
                for (MakerChangeReply reply: tmpMakerChangeReply) {
                    maker.addReply(reply);
                }
            }
            
            // 제품 변경 내역 존재
            if(!makerChangeContent.isEmpty()){
                MakerChangeReply makerChangeReply = new MakerChangeReply();

                // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
                LocalDate now = LocalDate.now();

                makerChangeReply.setId(null);
                makerChangeReply.setMaker(maker);
                makerChangeReply.setMakerChangeReply(makerChangeContent);
                makerChangeReply.setReplyDate(now);
                makerChangeReplyRepository.save(makerChangeReply);
                maker.addReply(makerChangeReply);
            }
            makerRepository.save(maker);
            return true;
        }
    }

    // 제조사 삭제하기
    public Boolean deleteMaker(long id){
        log.info("제조사 삭제 서비스: " + id);

        // 제조사 조회 확인
        boolean check = checkId(id);

        // 조회된 데이터 없을 경우
        if(!check){
            log.info("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            log.info("데이터 있음. 삭제 진행.");
            // 제조사 히스토리 삭제(리플)
            makerChangeReplyRepository.deleteByMakerId(id);

            // 점검기록에 있는 기존에 등록된 파일 확인
            List<MakerAuditFile> exiMakerAuditFile = makerAuditFileRepository.findByMakerAuditId(id).orElse(null);

            // 파일 데이터들 삭제
            for (MakerAuditFile tmpFile : exiMakerAuditFile) {
                File file = new File(tmpFile.getFilePath());
                file.delete();

                // DB의 파일 삭제
                makerAuditFileRepository.deleteById(tmpFile.getId());
            }

            // 제조사 점검 내역 삭제
            makerAuditRepository.deleteByMakerId(id);
            // 제조사 정보 삭제
            makerRepository.deleteById(id);
            return true;
        }
    }

    // 제조사 조회. 조건에 따른 검색 진행
    public Optional<List<Maker>> findMaker(String makerName,
                                           String makerAddress,
                                           String makerPerson,
                                           String makerPhone,
                                           Set<String> className){

        log.info("MakerServiceImpl 제조사 조회");
        log.info("makerName : " + makerName );
        log.info("tempBusinessType : " + className );

        // 분류 검색 구분에 따른 조회 방법을 다르게 함
        if(className.size() < 1){
            log.info("제품분류 없음.");
            // 분류 조회 하지 않을 경우
            return (Optional<List<Maker>>) makerRepository
                .findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCase(
                        makerName, makerAddress, makerPerson, makerPhone);
        }else{
            log.info("제품분류 있음.");
            // 분류 조회할 경우
            return (Optional<List<Maker>>) makerRepository
                    .findByMakerNameContainingAndMakerAddressContainingAndMakerPersonContainingAndMakerPhoneContainingIgnoreCaseAndtempBusinessType(
                            makerName, makerAddress, makerPerson, makerPhone, className);
        }
    }

    // 제품 중복 확인
    public Boolean checkId(long makerId){
        boolean check = makerRepository.existsById(makerId);
        return check;
    }

    // 제품 변경 리플 내용 수정
    public Boolean updateMakerReply(ReplyDTO replyDTO) throws Exception{

        log.info("ProductServicrImpl");
        log.info("제품 변경 리플 내용 수정");
        Optional<Maker> optMaker = makerRepository.findById(replyDTO.getId());
        Maker maker = optMaker.get();

        Optional<List<MakerChangeReply>> tmpMakerChangeReply = makerChangeReplyRepository.findByMakerId(replyDTO.getId());

        List<MakerChangeReply> makerChangeReplyList = tmpMakerChangeReply.get();

        int proReplySize = makerChangeReplyList.size();
        int chReplySize = replyDTO.getReplyDataList().size();

        // 현재 날짜 구하기 (시스템 시계, 시스템 타임존)
        LocalDate now = LocalDate.now();

        // 등록된 변경내역 리플들을 갖고옴
        for (MakerChangeReply makerChangeReply: makerChangeReplyList) {
            for(int j = 0; j<chReplySize; j++){
                // 수정된 리플의 데이터만 변경해서 저장함.
                if(makerChangeReply.getId().equals(replyDTO.getReplyDataList().get(j).getReplyId())){
                    makerChangeReply.setMaker(maker);
                    makerChangeReply.setReplyDate(now);
                    makerChangeReply.setMakerChangeReply(replyDTO.getReplyDataList().get(j).getChangeReplyData());

                    break;
                }
            }
            maker.addReply(makerChangeReply);
            makerChangeReplyRepository.save(makerChangeReply);
        }

        makerRepository.save(maker);
        return true;
    }


    // 제품 변경 리플 내용 삭제
    public Boolean deleteMakerReply(Long makerId, Long[] makerReplyId) throws Exception{

        log.info("ProductServicrImpl");
        log.info("제품 변경 리플 내용 삭제");
        Optional<Maker> optMaker = makerRepository.findById(makerId);
        Maker maker = optMaker.get();

        Optional<List<MakerChangeReply>> tmpMakerChangeReply = makerChangeReplyRepository.findByMakerId(makerId);

        List<MakerChangeReply> makerChangeReplyList = tmpMakerChangeReply.get();

        int chReplySize = makerReplyId.length;

        // 등록된 변경내역 리플들을 갖고옴
        for (MakerChangeReply makerChangeReply: makerChangeReplyList) {
            boolean chkDelData = false;
            for(int j = 0; j<chReplySize; j++){
                // 수정된 리플의 데이터만 삭제
                if(makerChangeReply.getId().equals(makerReplyId[j])){
                    chkDelData = true;
                    makerChangeReplyRepository.deleteById(makerChangeReply.getId());
                    break;
                }
            }

            // 삭제되지 않은 데이터 저장
            if(!chkDelData){
                maker.addReply(makerChangeReply);
                makerChangeReplyRepository.save(makerChangeReply);
            }
        }

        makerRepository.save(maker);
        return true;
    }
}
