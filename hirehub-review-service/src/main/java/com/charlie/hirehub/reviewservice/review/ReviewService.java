package com.charlie.hirehub.reviewservice.review;

import com.charlie.hirehub.reviewservice.review.dto.request.PostReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.request.UpdateReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.response.ReviewDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getAllReviewsForCompany(Long companyId);

    ReviewDTO postReviewForCompany(Long companyId, PostReviewRequest reviewRequest);

    ReviewDTO getReviewById(Long reviewId);

    ReviewDTO updateReviewById(Long reviewId, UpdateReviewRequest updateReviewRequest);

    void deleteReviewById(Long reviewId);

    boolean reviewsExistsByCompanyId(Long companyId);
}
