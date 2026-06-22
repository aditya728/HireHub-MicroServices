package com.charlie.hirehub.jobservice.job.integration;

import com.charlie.hirehub.jobservice.job.clients.CompanyClient;
import com.charlie.hirehub.jobservice.job.external.Company;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CompanyClientService {

    private final CompanyClient companyClient;

    public CompanyClientService(CompanyClient companyClient){
        this.companyClient = companyClient;
    }

    @Retry(name = "companyRetry")
    @CircuitBreaker(name = "companyBreaker",
            fallbackMethod = "getCompanyFallback")
    public Company getCompany(Long companyId){
       return companyClient.getCompany(companyId);
    }

    public Company getCompanyFallback(Long companyId, Exception e){
        return null;
    }
}
