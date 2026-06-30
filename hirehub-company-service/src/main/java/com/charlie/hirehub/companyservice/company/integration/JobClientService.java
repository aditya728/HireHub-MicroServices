package com.charlie.hirehub.companyservice.company.integration;

import com.charlie.hirehub.companyservice.company.client.JobClient;
import com.charlie.hirehub.companyservice.company.exception.JobServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobClientService {

    private final JobClient jobClient;

    private static final Logger logger =
            LoggerFactory.getLogger(JobClientService.class);

    public JobClientService(JobClient jobClient) {
        this.jobClient = jobClient;
    }

    @CircuitBreaker(name = "jobBreaker", fallbackMethod = "jobWithCompanyIdExistsFallbackCB")
    @Retry(name = "jobRetry")
    public boolean jobWithCompanyIdExists(Long companyId){

        logger.info("Calling Job Service to check whether jobs exist for company {}.",
                companyId);

        return jobClient.jobWithCompanyIdExists(companyId);
    }

    public boolean jobWithCompanyIdExistsFallbackCB(Long companyId, Exception e){

        logger.warn("Job Service unavailable while checking if jobs exist for company id {}", companyId);

        throw new JobServiceUnavailableException();
    }
}
