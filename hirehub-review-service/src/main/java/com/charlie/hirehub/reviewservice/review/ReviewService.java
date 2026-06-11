package com.charlie.hirehub.reviewservice.review;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReviewService {

    List<Review> getAllReviewsForCompany(Long companyId);

    boolean postReviewForCompany(Long companyId, Review review);

    Review getReviewById(Long reviewId);

    boolean updateReviewById(Long reviewId, Review review);

    boolean deleteReviewById(Long reviewId);
}
