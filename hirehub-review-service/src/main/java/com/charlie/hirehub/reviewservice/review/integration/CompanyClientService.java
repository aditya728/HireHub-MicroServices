package com.charlie.hirehub.reviewservice.review.integration;

import com.charlie.hirehub.reviewservice.review.client.CompanyClient;
import com.charlie.hirehub.reviewservice.review.exceptionHandling.CompanyNotFoundException;
import com.charlie.hirehub.reviewservice.review.exceptionHandling.CompanyServiceUnavailableException;
import com.charlie.hirehub.reviewservice.review.external.Company;
import com.charlie.hirehub.reviewservice.review.impl.ReviewServiceImpl;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyClientService {

    private final CompanyClient companyClient;

    private static final Logger logger =
            LoggerFactory.getLogger(CompanyClientService.class);

    public CompanyClientService(CompanyClient companyClient){
        this.companyClient = companyClient;
    }

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
