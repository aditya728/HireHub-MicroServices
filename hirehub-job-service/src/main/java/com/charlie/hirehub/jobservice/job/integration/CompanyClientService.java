package com.charlie.hirehub.jobservice.job.integration;

import com.charlie.hirehub.jobservice.job.clients.CompanyClient;
import com.charlie.hirehub.jobservice.job.exception.CompanyNotFoundException;
import com.charlie.hirehub.jobservice.job.exception.CompanyServiceUnavailableException;
import com.charlie.hirehub.jobservice.job.external.Company;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
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
        System.out.println("Company unavailable, returning NULL, " + e.getClass());
        return null;
    }

    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "validateCompanyFallback")
    @Retry(name = "companyRetry")
    public Company validateCompany(Long companyId){
        System.out.println("Calling Company Service to validate if Company exists");
        return companyClient.getCompany(companyId);
    }

    public Company validateCompanyFallback(Long companyId, Exception e){
        System.out.println("Validate Company Fallback");
        if(e instanceof FeignException.NotFound){
            throw new CompanyNotFoundException("Company with id: " + companyId + " does not exist");
        }

        throw new CompanyServiceUnavailableException();
    }
}
