package com.vue_spring.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {

    private Long id;

    private String imgFileName;

    private String imgFilePath;

    private long imgFileSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
    }

    public long getImgFileSize() {
        return imgFileSize;
    }

    public void setImgFileSize(long imgFileSize) {
        this.imgFileSize = imgFileSize;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }
}
