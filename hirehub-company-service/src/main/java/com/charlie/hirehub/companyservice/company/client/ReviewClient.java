package com.charlie.hirehub.companyservice.company.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "REVIEW-SERVICE")
public interface ReviewClient {

    @GetMapping("/reviews/company/{companyId}/exists")
    public boolean reviewsExistsByCompanyId(@PathVariable Long companyId);
}
