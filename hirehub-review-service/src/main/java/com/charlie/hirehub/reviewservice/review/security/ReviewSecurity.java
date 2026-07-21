package com.charlie.hirehub.reviewservice.review.security;

import com.charlie.hirehub.reviewservice.review.Review;
import com.charlie.hirehub.reviewservice.review.ReviewRepository;
import com.charlie.hirehub.reviewservice.review.exception.ReviewNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ReviewSecurity {

    private final ReviewRepository reviewRepository;

    public ReviewSecurity(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public boolean isOwner(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id " + reviewId));

        return review.getCreatedBy().equals(getCurrentUserId());
    }

    public Long getCurrentUserId(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        AuthenticatedPrincipal principal =
                (AuthenticatedPrincipal) authentication.getPrincipal();

        return principal.getUserId();
    }
}
