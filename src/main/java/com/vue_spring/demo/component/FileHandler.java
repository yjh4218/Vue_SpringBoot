package com.vue_spring.demo.component;

import com.vue_spring.demo.DTO.FileDTO;
import com.vue_spring.demo.model.ClaimImage;
import com.vue_spring.demo.model.InspectImage;
import com.vue_spring.demo.model.MakerAuditFile;
import com.vue_spring.demo.model.ProductFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FileHandler {

    public <T> List <T> parseFileInfo(
            List<MultipartFile> multipartFiles, T t
    )throws Exception {
        // 반환할 파일 리스트
        List<T> fileList = new ArrayList<>();

        // 전달되어 온 파일이 존재할 경우
        if(!CollectionUtils.isEmpty(multipartFiles)) {
            // 파일명을 업로드 한 날짜로 변환하여 저장
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);

            // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
            // 경로 구분자 File.separator 사용
//            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
            String absolutePath = new File("").getAbsolutePath() + File.separator;
//            String absolutePath = new File("").getAbsolutePath() + "/";

            // 파일을 저장할 세부 경로 지정
//            String inPath = "vue_front\\src\\assets\\fileData" + File.separator + current_date;

//            String inPath = "vue_front\\public\\fileData" + File.separator + current_date;

//            String path = "E:\\fileData\\" + current_date;

            // 서버 파일용
            String path = "C:\\QCFolder\\fileData\\" + current_date;

//            String path = "vue_front" + File.separator + "src" + File.separator + "assets" + File.separator + "images" + File.separator + current_date;
            File file = new File(path);

            // 디렉터리가 존재하지 않을 경우
            log.info("파일 디렉토리 확인");
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                // 디렉터리 생성에 실패했을 경우
                if(!wasSuccessful)
                    System.out.println("file: was not successful");
            }

            // 다중 파일 처리
            for(MultipartFile multipartFile : multipartFiles) {

                // 파일의 확장자 추출
                String originalFileExtension;
                String contentType = multipartFile.getContentType();
                log.info("파일 확장자 저장");
                // 확장자명이 존재하지 않을 경우 처리 x
                if(ObjectUtils.isEmpty(contentType)) {
                    break;
                }
                else {  // 확장자가 jpeg, png인 파일들만 받아서 처리\]
                    switch (contentType){
                        case "image/jpeg":
                            originalFileExtension = ".jpg";
                            break;
                        case "image/png":
                            originalFileExtension = ".png";
                            break;
                        case "application/vnd.ms-excel":
                            originalFileExtension = ".xls";
                            break;
                        case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                            originalFileExtension = ".xlsx";
                            break;
                        case "application/vnd.ms-powerpoint":
                            originalFileExtension = ".ppt";
                            break;
                        case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                            originalFileExtension = ".pptx";
                            break;
                        case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                            originalFileExtension = ".docx";
                            break;
                        case "application/msword":
                            originalFileExtension = ".doc";
                            break;
                        case "application/haansofthwp":
                            originalFileExtension = ".hwp";
                            break;
                        case "application/haansofthwpx":
                            originalFileExtension = ".hwpx";
                            break;
                        case "application/pdf":
                            originalFileExtension = ".pdf";
                            break;
                        default:
                            originalFileExtension = null;
                            break;
                    }
                    if(originalFileExtension.equals(null)){
                        break;
                    }
                }

                // 파일명 중복 피하고자 나노초까지 얻어와 지정
                String new_file_name = System.nanoTime() + originalFileExtension;

                // 파일 DTO 생성
                FileDTO fileDTO = FileDTO.builder()
                        .fileName(multipartFile.getOriginalFilename())
                        .filePath(path + File.separator + new_file_name)
                        .fileSize(multipartFile.getSize())
                        .build();

                if(t instanceof ProductFile){
                    ProductFile fileData = ProductFile.builder()
                            .fileName(fileDTO.getFileName())
                            .filePath(fileDTO.getFilePath())
                            .fileSize(fileDTO.getFileSize())
                            .build();

                    fileList.add((T) fileData);
                } else if(t instanceof InspectImage){
                    InspectImage fileData = InspectImage.builder()
                            .imgFileName(fileDTO.getFileName())
                            .imgFilePath(fileDTO.getFilePath())
                            .imgFileSize(fileDTO.getFileSize())
                            .build();
                    fileList.add((T) fileData);
                } else if(t instanceof ClaimImage){
                    ClaimImage fileData = ClaimImage.builder()
                            .imgFileName(fileDTO.getFileName())
                            .imgFilePath(fileDTO.getFilePath())
                            .imgFileSize(fileDTO.getFileSize())
                            .build();
                    fileList.add((T) fileData);
                } else if(t instanceof MakerAuditFile){
                    MakerAuditFile fileData = MakerAuditFile.builder()
                            .fileName(fileDTO.getFileName())
                            .filePath(fileDTO.getFilePath())
                            .fileSize(fileDTO.getFileSize())
                            .build();
                    fileList.add((T) fileData);
                }

                log.info("파일 데이터 저장");
                // 업로드 한 파일 데이터를 지정한 파일에 저장
//                file = new File(absolutePath + path + File.separator + new_file_name);
                file = new File(  path +  "\\" + new_file_name);
                multipartFile.transferTo(file);

                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);

            }
        }
        return fileList;
    }
}
