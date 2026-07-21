package com.charlie.hirehub.reviewservice.review.impl;

import com.charlie.hirehub.reviewservice.review.Review;
import com.charlie.hirehub.reviewservice.review.ReviewRepository;
import com.charlie.hirehub.reviewservice.review.ReviewService;
import com.charlie.hirehub.reviewservice.review.dto.request.PostReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.request.UpdateReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.response.ReviewDTO;
import com.charlie.hirehub.reviewservice.review.exception.ReviewNotFoundException;
import com.charlie.hirehub.reviewservice.review.exception.TooManyRequestsException;
import com.charlie.hirehub.reviewservice.review.external.Company;
import com.charlie.hirehub.reviewservice.review.integration.CompanyClientService;
import com.charlie.hirehub.reviewservice.review.mapper.ReviewMapper;
import com.charlie.hirehub.reviewservice.review.security.ReviewSecurity;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final CompanyClientService companyClientService;

    private final ReviewSecurity reviewSecurity;

    private static final Logger logger =
            LoggerFactory.getLogger(ReviewServiceImpl.class);

    public ReviewServiceImpl(ReviewRepository reviewRepo, CompanyClientService companyClientService, ReviewSecurity reviewSecurity) {
        this.reviewRepo = reviewRepo;
        this.companyClientService = companyClientService;
        this.reviewSecurity = reviewSecurity;
    }

    @Override
    @RateLimiter(name = "readReviewRateLimiter", fallbackMethod = "getAllReviewsForCompanyRateLimitFallback")
    public List<ReviewDTO> getAllReviewsForCompany(Long companyId) {

        logger.info("Fetching all reviews for company with id {}", companyId);

        List<Review> reviews = reviewRepo.findAllByCompanyId(companyId);

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewMapper::toReviewDTO).toList();

        logger.info("Successfully fetched {} reviews for company with id {}", reviewDTOs.size(), companyId);

        return reviewDTOs;
    }

    @Override
    @RateLimiter(name = "writeReviewRateLimiter", fallbackMethod = "postReviewForCompanyRateLimitFallback")
    public ReviewDTO postReviewForCompany(Long companyId, PostReviewRequest reviewRequest) {

        logger.info("Posting a review for company with id {}", companyId);

        //validate if company exists
        Company company = companyClientService.validateCompany(companyId);

        Review review = ReviewMapper.toReview(reviewRequest);

        review.setCompanyId(companyId);
        Long userId = reviewSecurity.getCurrentUserId();
        review.setCreatedBy(userId);

        Review savedReview = reviewRepo.save(review);

        logger.info("Successfully posted a review for company with id {}", companyId);
        return ReviewMapper.toReviewDTO(savedReview);
    }

    @Override
    @RateLimiter(name = "readReviewRateLimiter", fallbackMethod = "getReviewByIdRateLimitFallback")
    public ReviewDTO getReviewById(Long reviewId) {

        logger.info("Fetching a review with id {}", reviewId);

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + reviewId + " not found"));

        logger.info("Successfully fetched a review with id {}", reviewId);

        return ReviewMapper.toReviewDTO(review);
    }

    @Override
    @RateLimiter(name = "writeReviewRateLimiter", fallbackMethod = "updateReviewByIdRateLimitFallback")
    public ReviewDTO updateReviewById(Long reviewId, UpdateReviewRequest updatedReview) {

        logger.info("Updating a review with id {}", reviewId);

        Review currentReview = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + reviewId + " not found"));

        currentReview.setTitle(updatedReview.getTitle());
        currentReview.setReviewDescription(updatedReview.getReviewDescription());
        currentReview.setRating(updatedReview.getRating());

        Review savedReview = reviewRepo.save(currentReview);

        logger.info("Successfully updated review with id {}", reviewId);

        return ReviewMapper.toReviewDTO(savedReview);
    }

    @Override
    @RateLimiter(name = "writeReviewRateLimiter", fallbackMethod = "deleteReviewByIdRateLimitFallback")
    public void deleteReviewById(Long reviewId) {

        logger.info("Deleting review with id {}", reviewId);

        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with id " + reviewId + " not found"));

        reviewRepo.delete(review);

        logger.info("Review with id {} deleted successfully", reviewId);
    }

    @Override
    @RateLimiter(name = "readReviewRateLimiter", fallbackMethod = "reviewsExistsByCompanyIdRateLimitFallback")
    public boolean reviewsExistsByCompanyId(Long companyId) {

        logger.info("Checking if reviews exist for company with id {}", companyId);

        boolean exists = reviewRepo.existsByCompanyId(companyId);

        if (exists) {
            logger.info("Reviews exist for company with id {}", companyId);
        } else {
            logger.info("No reviews exist for company with id {}", companyId);
        }
        return exists;
    }

    // Fallback Methods

    public List<ReviewDTO> getAllReviewsForCompanyRateLimitFallback(
            Long companyId,
            RequestNotPermitted e) {

        logger.warn("Rate limit exceeded while fetching reviews for company with id {}", companyId);

        throw new TooManyRequestsException(
                "Too many requests to fetch reviews. Please try again later.", e);
    }

    public ReviewDTO postReviewForCompanyRateLimitFallback(
            Long companyId,
            PostReviewRequest reviewRequest,
            RequestNotPermitted e) {

        logger.warn("Rate limit exceeded while posting review for company with id {}", companyId);

        throw new TooManyRequestsException(
                "Too many requests to post review. Please try again later.", e);
    }

    public ReviewDTO getReviewByIdRateLimitFallback(
            Long reviewId,
            RequestNotPermitted e) {

        logger.warn("Rate limit exceeded while fetching review with id {}", reviewId);

        throw new TooManyRequestsException(
                "Too many requests to fetch review. Please try again later.", e);
    }

    public ReviewDTO updateReviewByIdRateLimitFallback(
            Long reviewId,
            UpdateReviewRequest updatedReview,
            RequestNotPermitted e) {

        logger.warn("Rate limit exceeded while updating review with id {}", reviewId);

        throw new TooManyRequestsException(
                "Too many requests to update review. Please try again later.", e);
    }

    public void deleteReviewByIdRateLimitFallback(
            Long reviewId,
            RequestNotPermitted e) {

        logger.warn("Rate limit exceeded while deleting review with id {}", reviewId);

        throw new TooManyRequestsException(
                "Too many requests to delete review. Please try again later.", e);
    }

    public boolean reviewsExistsByCompanyIdRateLimitFallback(
            Long companyId,
            RequestNotPermitted e) {

        logger.warn("Rate limit exceeded while checking reviews for company with id {}", companyId);

        throw new TooManyRequestsException(
                "Too many requests to check reviews. Please try again later.", e);
    }
}
