package com.charlie.hirehub.jobservice.job.integration;

import com.charlie.hirehub.jobservice.job.clients.CompanyClient;
import com.charlie.hirehub.jobservice.job.exception.CompanyNotFoundException;
import com.charlie.hirehub.jobservice.job.exception.CompanyServiceUnavailableException;
import com.charlie.hirehub.jobservice.job.external.Company;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CompanyClientService {

    private final CompanyClient companyClient;

    private static final Logger logger =
            LoggerFactory.getLogger(CompanyClientService.class);

    public CompanyClientService(CompanyClient companyClient){
        this.companyClient = companyClient;
    }

    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "getCompanyFallback")
    @Retry(name = "companyRetry")
    public Company getCompany(Long companyId){

        logger.debug("Fetching company details for company id {}.", companyId);

        return companyClient.getCompany(companyId);
    }

    public Company getCompanyFallback(Long companyId, Exception e){

        logger.warn("Company unavailable while fetching company id {}. Returning degraded response.",
                companyId);

        return null;
    }

    @CircuitBreaker(name = "companyBreaker", fallbackMethod = "validateCompanyFallback")
    @Retry(name = "companyRetry")
    public Company validateCompany(Long companyId){

        logger.debug("Validating company with id {}.", companyId);

        return companyClient.getCompany(companyId);
    }

    public Company validateCompanyFallback(Long companyId, Exception e){
        if(e instanceof FeignException.NotFound){

            logger.info("Company validation failed. Company id {} does not exist.", companyId);

            throw new CompanyNotFoundException("Company with id: " + companyId + " does not exist");
        }

        logger.warn("Company Service unavailable while validating company id {}.", companyId);

        throw new CompanyServiceUnavailableException();
    }
}
