package com.charlie.hirehub.jobservice.job.integration;

import com.charlie.hirehub.jobservice.job.clients.CompanyClient;
import com.charlie.hirehub.jobservice.job.external.Company;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CompanyClientService {

    private final CompanyClient companyClient;

    public CompanyClientService(CompanyClient companyClient){
        this.companyClient = companyClient;
    }

    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "getCompanyFallback")
    @Retry(name = "companyRetry")
    public Company getCompany(Long companyId){
        System.out.println("Calling Company Service");
        return companyClient.getCompany(companyId);
    }

    public Company getCompanyFallback(Long companyId, Exception e){
        if(e instanceof FeignException.NotFound){
            throw (FeignException.NotFound) e;
        }

        System.out.println("Company Service unavailable, returning null");
        return null;
    }
}
