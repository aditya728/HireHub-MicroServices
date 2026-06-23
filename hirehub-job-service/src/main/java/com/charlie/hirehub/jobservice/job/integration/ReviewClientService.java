package com.charlie.hirehub.jobservice.job.integration;

import com.charlie.hirehub.jobservice.job.clients.ReviewClient;
import com.charlie.hirehub.jobservice.job.external.Review;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewClientService {

   private final ReviewClient reviewClient;

    public ReviewClientService(ReviewClient reviewClient){
        this.reviewClient = reviewClient;
    }

    @Retry(name = "reviewRetry")
    @CircuitBreaker(name = "reviewBreaker", fallbackMethod = "getReviewsFallback")
    public List<Review> getReviews(Long companyId){
        System.out.println("Calling Review Service");
        return reviewClient.getReviews(companyId);
    }

    public List<Review> getReviewsFallback(Long companyId, Exception e) {
        if(e instanceof FeignException.NotFound){
            throw (FeignException.NotFound) e;
        }

        System.out.println("Review Service unavailable, returning empty reviews");
        return Collections.emptyList();
    }
}
