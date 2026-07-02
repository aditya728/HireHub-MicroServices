package com.charlie.hirehub.reviewservice.review.mapper;

import com.charlie.hirehub.reviewservice.review.Review;
import com.charlie.hirehub.reviewservice.review.dto.request.PostReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.response.ReviewDTO;

public class ReviewMapper {

    public static ReviewDTO toReviewDTO(Review review) {

        ReviewDTO dto = new ReviewDTO();

        dto.setId(review.getId());
        dto.setTitle(review.getTitle());
        dto.setReviewDescription(review.getReviewDescription());
        dto.setRating(review.getRating());
        dto.setCompanyId(review.getCompanyId());

        return dto;
    }

    public static Review toReview(PostReviewRequest reviewRequest) {

        Review review = new Review();

        review.setTitle(reviewRequest.getTitle());
        review.setReviewDescription(reviewRequest.getReviewDescription());
        review.setRating(reviewRequest.getRating());

        return review;
    }
}
