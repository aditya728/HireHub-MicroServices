package com.charlie.hirehub.jobservice.job.integration;

import com.charlie.hirehub.jobservice.job.clients.ReviewClient;
import com.charlie.hirehub.jobservice.job.external.Review;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewClientService {

   private final ReviewClient reviewClient;

   private static final Logger logger =
        LoggerFactory.getLogger(ReviewClientService.class);

    public ReviewClientService(ReviewClient reviewClient){
        this.reviewClient = reviewClient;
    }

    @CircuitBreaker(name = "reviewBreaker", fallbackMethod = "getReviewsFallback")
    @Retry(name = "reviewRetry")
    public List<Review> getReviews(Long companyId){

        logger.debug("Fetching reviews for companyId {}", companyId);

        return reviewClient.getReviews(companyId);
    }

    public List<Review> getReviewsFallback(Long companyId, Exception e) {

        logger.warn("Review unavailable while fetching for company id {}. Returning degraded response.",
                companyId);

        return Collections.emptyList();
    }
}
