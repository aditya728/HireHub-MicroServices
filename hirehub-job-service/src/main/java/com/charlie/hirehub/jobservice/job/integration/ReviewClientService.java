package com.charlie.hirehub.jobservice.job.integration;

import com.charlie.hirehub.jobservice.job.clients.ReviewClient;
import com.charlie.hirehub.jobservice.job.external.Review;
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

    @CircuitBreaker(name = "reviewBreaker", fallbackMethod = "getReviewsFallback")
    @Retry(name = "reviewRetry")
    public List<Review> getReviews(Long companyId){
        return reviewClient.getReviews(companyId);
    }

    public List<Review> getReviewsFallback(Long companyId, Exception e) {
        System.out.println("REVIEW FALLBACK");
        System.out.println(e.getClass());

        return Collections.emptyList();
    }
}
