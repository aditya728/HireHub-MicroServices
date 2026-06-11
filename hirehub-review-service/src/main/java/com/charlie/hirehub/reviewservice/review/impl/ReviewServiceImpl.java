package com.charlie.hirehub.reviewservice.review.impl;

import com.charlie.hirehub.reviewservice.review.Review;
import com.charlie.hirehub.reviewservice.review.ReviewRepository;
import com.charlie.hirehub.reviewservice.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepo;

    public ReviewServiceImpl(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Override
    public List<Review> getAllReviewsForCompany(Long companyId) {
        return reviewRepo.findAllByCompanyId(companyId);
    }

    @Override
    public boolean postReviewForCompany(Long companyId, Review review) {

        if(companyId != null && review != null){
            review.setCompanyId(companyId);
            reviewRepo.save(review);
            return true;
        }
        return false;
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


}
