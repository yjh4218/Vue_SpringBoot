package com.vue_spring.demo.DAO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public interface InspectExcelInterfaceDAO {
    String getSkuNo();
    String getProductName();
    String getClassName();
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date getInspectDate();
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date getLotDate();
    String getDecideResult();
    Float getMoisture();
    String getAppearance();
    String getInspectContent();
    String getColor();
    String getSensuality();
    String getSize();
    String getDamage();
    String getFinishState();
    String getCheckWork();
    String getUsability();
    String getWeight();
    String getCheckPacking();
    String getForeignBody();
    String getSpecialReport();
//    String getNote();
}
