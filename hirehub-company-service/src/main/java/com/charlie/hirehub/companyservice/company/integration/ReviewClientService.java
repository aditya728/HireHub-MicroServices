package com.charlie.hirehub.companyservice.company.integration;

import com.charlie.hirehub.companyservice.company.client.ReviewClient;
import com.charlie.hirehub.companyservice.company.exception.ReviewServiceUnavailableException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReviewClientService {

    private final ReviewClient reviewClient;

    private static final Logger logger =
            LoggerFactory.getLogger(ReviewClientService.class);

    public ReviewClientService(ReviewClient reviewClient) {
        this.reviewClient = reviewClient;
    }

    @CircuitBreaker(name = "reviewBreaker", fallbackMethod = "reviewsExistsByCompanyIdFallback")
    @Retry(name = "reviewRetry")
    public boolean reviewsExistsByCompanyId(Long companyId) {

        logger.info("Calling Review Service to check whether review(s) exist for company {}.",
                companyId);

        boolean exists = reviewClient.reviewsExistsByCompanyId(companyId);

        logger.info("Review Service responded successfully for company {}.", companyId);

        return exists;
    }

    public boolean reviewsExistsByCompanyIdFallback(Long companyId, Exception e) {

        logger.warn("Review Service unavailable while checking if reviews exist for company id {}.",
                companyId, e);

        throw new ReviewServiceUnavailableException();
    }
}
