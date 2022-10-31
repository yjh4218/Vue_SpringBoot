package com.vue_spring.demo.DTO;

import java.util.List;

public class SalesProductComponentDTO {

    static public class ComponentProduct{
        private long id;
        private String skuNo;
        private String productName;
        private long quantity;

        public long getId() {
            return id;
        }

        public String getSkuNo() {
            return skuNo;
        }

        public String getProductName() {
            return productName;
        }

        public long getQuantity() {
            return quantity;
        }

    }

    private long id;

    private String skuNo;

    private String productName;

    private List<ComponentProduct> componentProduct;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ComponentProduct> getComponentProduct() {
        return componentProduct;
    }

    public void setComponentProduct(List<ComponentProduct> componentProduct) {
        this.componentProduct = componentProduct;
    }
}
