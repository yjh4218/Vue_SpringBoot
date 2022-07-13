package com.vue_spring.demo.service;

import com.vue_spring.demo.model.Inspect;
import com.vue_spring.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface InspectService {


    public Optional<List<Inspect>> findInspect(List<Long> productId, Date beforeDate, Date afterDate);

    public Optional<List<Inspect>> findInspect(Date beforeDate, Date afterDate);

    public Boolean insertInspect(Inspect inspect, List<MultipartFile> imgFiles) throws Exception;

    public Boolean updateInspect(Inspect inspect, List<MultipartFile> imgFiles) throws Exception;

    public Boolean checkInspect(long id);

    public Boolean checkInspect(Product product, Date inspectDate);

    public Boolean deleteInspect(long id);

    public Boolean findProductInspect(long id);

}
