package com.charlie.hirehub.jobservice.job.integration;

import com.charlie.hirehub.jobservice.job.clients.CompanyClient;
import com.charlie.hirehub.jobservice.job.external.Company;
import feign.FeignException;
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

    @Retry(name = "companyRetry",
            fallbackMethod = "getCompanyFallback")
    @CircuitBreaker(name = "companyBreaker",
            fallbackMethod = "getCompanyFallback")
    public Company getCompany(Long companyId){
        try{
            System.out.println("Calling Company Service");
            return companyClient.getCompany(companyId);
        }catch(FeignException.NotFound e){
            e.printStackTrace();
        }
        return null;
    }

    public Company getCompanyFallback(Long companyId, Exception e){

        System.out.println("COMPANY FALLBACK");
        System.out.println(e.getClass());

        return null;
    }
}
