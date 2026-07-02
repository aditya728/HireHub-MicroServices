package com.charlie.hirehub.reviewservice.review;

import com.charlie.hirehub.reviewservice.review.dto.request.PostReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.response.ReviewDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getAllReviewsForCompany(Long companyId);

    ReviewDTO postReviewForCompany(Long companyId, PostReviewRequest reviewRequest);

    Review getReviewById(Long reviewId);

    boolean updateReviewById(Long reviewId, Review review);

    boolean deleteReviewById(Long reviewId);

    boolean reviewsExistsByCompanyId(Long companyId);
}
