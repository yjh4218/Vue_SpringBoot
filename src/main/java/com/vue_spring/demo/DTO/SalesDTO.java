package com.vue_spring.demo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

public class SalesDTO {
    static public class SalesData{
        private Long id;
        private String skuNo;
        private String productName;
        private long salesVolumn;

        public Long getId() {
            return id;
        }

        public String getSkuNo() {
            return skuNo;
        }

        public String getProductName() {
            return productName;
        }

        public long getSalesVolumn() {
            return salesVolumn;
        }
    }

    // 판매량 날짜
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date salesMonth;

    private List<SalesData> salesData;

    public Date getSalesMonth() {
        return salesMonth;
    }

    public void setSalesMonth(Date salesMonth) {
        this.salesMonth = salesMonth;
    }

    public List<SalesData> getSalesData() {
        return salesData;
    }

    public void setSalesData(List<SalesData> salesData) {
        this.salesData = salesData;
    }
}
