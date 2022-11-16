package com.vue_spring.demo.DAO;

import java.util.Date;

public interface InspectExcelInterfaceDAO {
    String getSkuNo();
    String getProductName();
    Date getInspectDate();
    Date getLotDate();
    String getDecideResult();
    Float getMoisture();
    String getInspectContent();
    String getSpecialReport();
    String getNote();
}
