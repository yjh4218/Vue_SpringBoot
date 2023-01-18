package com.vue_spring.demo.service;

import com.vue_spring.demo.DTO.ReplyDTO;
import com.vue_spring.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface ProductService {
    public List<Product> findProduct(String sku_no, String productName,
                                     String brandName, String maker, Set<String> tempSelectChk, Set<String> tempOperation);

    public Optional<List<Product>> findCurseProduct(Long[] productCurseId);

    public List<Long> findAllProductId(String skuNo, String productName,
                                     String brandName, String maker, Set<String> tempClassName);

    public Optional<Product> findProduct(long productId);

//    public Boolean insertProduct(Product product);

    public Boolean insertProduct(Product product, List<MultipartFile> imgFiles, String productChangeReply) throws Exception;

//    public Boolean updateProduct(Product product);
    public Boolean updateProduct(Product product, List<MultipartFile> imgFiles, List<Long> imgId, String productChangeReply) throws Exception;

    public Boolean deleteProduct(long id);

    public Boolean checkSkuNo(String skuNo);

    public Boolean checkId(long productId);

    public Boolean checkReplyId(long replyId);

    public Boolean updateProductReply(ReplyDTO productReplyDTO) throws Exception;

    public Boolean deleteProductReply(Long productId, Long[] productReplyId) throws Exception;
}
