package com.charlie.hirehub.companyservice.company.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "JOB-SERVICE")
public interface JobClient {

    @GetMapping("/jobs/company/{companyId}/exists")
    public boolean jobWithCompanyIdExists(@PathVariable Long companyId);
}
