package com.charlie.hirehub.reviewservice.review.impl;

import com.charlie.hirehub.reviewservice.review.Review;
import com.charlie.hirehub.reviewservice.review.ReviewRepository;
import com.charlie.hirehub.reviewservice.review.ReviewService;
import com.charlie.hirehub.reviewservice.review.dto.request.PostReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.response.ReviewDTO;
import com.charlie.hirehub.reviewservice.review.external.Company;
import com.charlie.hirehub.reviewservice.review.integration.CompanyClientService;
import com.charlie.hirehub.reviewservice.review.mapper.ReviewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final CompanyClientService companyClientService;
    private static final Logger logger =
            LoggerFactory.getLogger(ReviewServiceImpl.class);

    public ReviewServiceImpl(ReviewRepository reviewRepo, CompanyClientService companyClientService) {
        this.reviewRepo = reviewRepo;
        this.companyClientService = companyClientService;
    }

    @Override
    public List<ReviewDTO> getAllReviewsForCompany(Long companyId) {

        logger.info("Fetching all reviews for company with id {}", companyId);

        List<Review> reviews = reviewRepo.findAllByCompanyId(companyId);

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewMapper::toReviewDTO).toList();

        logger.info("Successfully fetched {} reviews for company with id {}", reviewDTOs.size(), companyId);

        return reviewDTOs;
    }

    @Override
    public ReviewDTO postReviewForCompany(Long companyId, PostReviewRequest reviewRequest) {

        logger.info("Posting a review for company with id {}", companyId);

        //validate if company exists
        Company company = companyClientService.validateCompany(companyId);

        Review review = ReviewMapper.toReview(reviewRequest);
        review.setCompanyId(companyId);

        Review savedReview = reviewRepo.save(review);

        logger.info("Successfully posted a review for company with id {}", companyId);
        return ReviewMapper.toReviewDTO(savedReview);
    }

    @Override
    public Review getReviewById( Long reviewId) {
        return reviewRepo.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReviewById(Long reviewId, Review updatedReview) {
        Review currentReview = reviewRepo.findById(reviewId).orElse(null);

        if(currentReview != null){
            currentReview.setTitle(updatedReview.getTitle());
            currentReview.setReviewDescription(updatedReview.getReviewDescription());
            currentReview.setRating(updatedReview.getRating());
            currentReview.setCompanyId(updatedReview.getCompanyId());

            reviewRepo.save(currentReview);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReviewById(Long reviewId) {
        Review review = reviewRepo.findById(reviewId).orElse(null);

        if(review != null){
            reviewRepo.delete(review);
            return true;
        }
        return false;
    }

    @Override
    public boolean reviewsExistsByCompanyId(Long companyId) {
        return reviewRepo.existsByCompanyId(companyId);
    }


}
