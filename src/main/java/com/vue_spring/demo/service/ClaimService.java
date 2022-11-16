package com.vue_spring.demo.service;

import com.vue_spring.demo.model.Claim;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public interface ClaimService {


    public Optional<List<Claim>> findClaim(List<Long> productId, Date beforeDate, Date afterDate);

    public Optional<List<Claim>> findClaim(Date beforeDate, Date afterDate);

    public Optional<Claim> findClaim(long id);

    public Boolean insertClaim(Claim claim, List<MultipartFile> imgFiles) throws Exception;

    public Boolean updateClaim(Claim claim, List<MultipartFile> imgFiles, List<Long> imgId) throws Exception;

    public Boolean checkClaim(long id);


    public Boolean deleteClaim(long id);

    public Boolean findProductClaim(long id);

}
