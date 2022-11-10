package com.vue_spring.demo.service;

import com.vue_spring.demo.Repository.MakerAuditFileRepository;
import com.vue_spring.demo.Repository.MakerAuditRepository;
import com.vue_spring.demo.Repository.MakerRepository;
import com.vue_spring.demo.component.FileHandler;
import com.vue_spring.demo.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

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
    public Boolean insertMakerAudit(MakerAudit makerAudit, List<MultipartFile> fileData, Long makerId){
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
            Maker maker = makerRepository.findById(makerId).orElse(null);

            // MakerAudit 저장되어 있는 검수 데이터들 갖고 옴.
            makerAudit.setMaker(maker);
            List<MakerAudit> exiMakerAudit = makerAuditRepository.findByMakerId(makerId).orElse(null);

            // 제조사 검수 정보 저장 후 id 가져옴
            long makerAuditId = makerAuditRepository.save(makerAudit).getId();

            // 파일이 있을 때만 실행함.
            if(makerAuditFileList != null && !makerAuditFileList.isEmpty()) {

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
            if(exiMakerAudit != null && exiMakerAudit.size() > 0){
                for(MakerAudit tmpMakerAudit: exiMakerAudit){
                    maker.addMakerAudit(tmpMakerAudit);
                }
            }

            // 제조사 점검 정보 업데이트
            makerRepository.save(maker);

            // 저장 성공
            check = true;

        }catch (Exception e){
            log.info(e.getMessage());
            //저장 실패
            check=false;
        }

        return check;
    }

    // 제조사 점검 수정
    public Boolean updateMakerAudit(MakerAudit makerAudit, List<MultipartFile> fileData, Long makerId, List<Long> fileId){
        log.info("제조사 점검 수정 서비스");
        log.info(String.valueOf(makerAudit));
        log.info(String.valueOf(fileData));

        // JPA에 저장되는지 확인. 기본 값은 저장 실패로 해놓는다.
        boolean check = false;

        try{
            // DB에 저장된 제조사 조회
            Maker maker = makerRepository.findById(makerId).orElse(null);

            // 제조사별 검수 정보 갖고 옴(MakerAudit 저장되어 있는 검수 데이터들 갖고 옴.)
            makerAudit.setMaker(maker);
            List<MakerAudit> exiMakerAudit = makerAuditRepository.findByMakerId(makerId).orElse(null);

            // MakerAudit에 저장되어 있는 데이터들 maker에 다시 저장
            if(exiMakerAudit != null && exiMakerAudit.size() > 0){
                for(MakerAudit tmpMakerAudit: exiMakerAudit){
                    // 현재 수정중인 데이터 제외
                    if(tmpMakerAudit.getId() != makerAudit.getId()){
                        maker.addMakerAudit(tmpMakerAudit);
                    }
                }
            }

            // 점검기록에 있는 기존에 등록된 파일 확인
            List<MakerAuditFile> exiMakerAuditFile = makerAuditFileRepository.findByMakerAuditId(makerAudit.getId()).orElse(null);

            // 사용자가 모든 파일 삭제했을 경우
            if(fileId == null){
                for (MakerAuditFile tmpFile : exiMakerAuditFile) {
                    File file = new File(tmpFile.getFilePath());
                    file.delete();

                    // DB의 파일 삭제
                    makerAuditFileRepository.deleteById(tmpFile.getId());
                }
            }
            // 사용자가 파일 삭제를 하지 않았을 경우
            else if(exiMakerAuditFile.size() == fileId.size()){
                for(MakerAuditFile tmpFile : exiMakerAuditFile){
                    makerAudit.addFile(tmpFile);
                }
            }
            // 사용자가 일부 파일만 삭제하였을 경우
            else{
                // 경로에 지정된 파일 삭제. 사용자가 삭제하지 않은 파일은 삭제 안 함
                for(MakerAuditFile tmpFile : exiMakerAuditFile){
                    boolean checkFile = false;
                    for(long tmpFileId : fileId){
                        if(tmpFile.getId().equals(tmpFileId)){
                            checkFile = true;
                            break;
                        }
                    }
                    // 사용자가 삭제한 파일만 삭제
                    if(!checkFile){
                        // 경로에 있는 파일 삭제
                        log.info("사용자가 삭제한 파일 삭제");
                        log.info("tmpFile : {}", tmpFile);
                        File file = new File(tmpFile.getFilePath());
                        file.delete();

                        // DB의 파일 삭제
                        makerAuditFileRepository.deleteById(tmpFile.getId());
                    }
                    // 사용자가 삭제하지 않은 파일은 다시 저장
                    else{
//                        tmpFile.setMakerAudit(makerAudit);
                        makerAudit.addFile(tmpFile);
                    }
                }
            }

            // 이미지 파일 경로 지정
            MakerAuditFile makerAuditFile = new MakerAuditFile();
            List<MakerAuditFile> makerAuditFileList = null;
            try {
                makerAuditFileList = fileHandler.parseFileInfo(fileData, makerAuditFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 파일이 있을 때만 실행함.
            if(makerAuditFileList != null && !makerAuditFileList.isEmpty()) {

                for(MakerAuditFile makerFile : makerAuditFileList) {

                    // 파일들에 검수정보 저장(saveMakerAudit)
                    makerFile.setMakerAudit(makerAudit);

                    // image DB에 저장
                    makerAuditFileRepository.save(makerFile);

                    // DB에 저장된 검수 내용에 사진 정보 저장
                    makerAudit.addFile(makerFile);
                }
                // DB에 사진정보 추가한 상태로 다시 저장
                makerAuditRepository.save(makerAudit);

                // 제조사 점검 정보를 maker에 저장
                maker.addMakerAudit(makerAudit);
            }

            // 제조사 점검 정보 업데이트
            makerRepository.save(maker);

            // 저장 성공
            check = true;

        }catch (Exception e){
            e.printStackTrace();
            check = false;
        }
        return check;
    }

    // 제조사 점검 삭제하기
    public Boolean deleteMakerAudit(long id){
        log.info("제조사 점검 삭제 서비스: " + id);

        // 제조사 점검 조회 확인
        boolean check = checkId(id);

        // 조회된 데이터 없을 경우
        if(!check){
            log.info("데이터 없음. 삭제 불가");
            return false;
        }
        else{
            log.info("데이터 있음. 삭제 진행.");

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
            makerAuditRepository.deleteById(id);

            return true;
        }
    }

    // 제품 중복 확인
    public Boolean checkId(long makerAuditId){
        boolean check = makerAuditRepository.existsById(makerAuditId);
        return check;
    }
}
