package com.charlie.hirehub.reviewservice.review;

import com.charlie.hirehub.reviewservice.review.dto.request.PostReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.request.UpdateReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.response.ReviewDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
GET    /reviews?companyId={companyId}
POST   /reviews?companyId={companyId}
GET    /reviews/{reviewId}
PUT    /reviews/{reviewId}
DELETE /reviews/{reviewId}
 */
@RestController
@RequestMapping("/reviews")
public class ReviewController{

    ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsForCompany(@RequestParam Long companyId){
        List<ReviewDTO> reviews = reviewService.getAllReviewsForCompany(companyId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDATE')")
    public ResponseEntity<ReviewDTO> postReviewForCompany(@RequestParam Long companyId, @RequestBody PostReviewRequest reviewRequest){

        ReviewDTO reviewPosted = reviewService.postReviewForCompany(companyId, reviewRequest);
        return new ResponseEntity<>(reviewPosted, HttpStatus.CREATED);
    }

    @GetMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewDTO> getReviewById( @PathVariable Long reviewId){

        ReviewDTO review = reviewService.getReviewById(reviewId);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('ADMIN') || @reviewSecurity.isOwner(#reviewId)")
    public ResponseEntity<ReviewDTO> updateReviewById(@PathVariable Long reviewId,
                                    @RequestBody UpdateReviewRequest updateReviewRequest){

        ReviewDTO reviewUpdated = reviewService.updateReviewById(reviewId, updateReviewRequest);
        return new ResponseEntity<>(reviewUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('ADMIN') || @reviewSecurity.isOwner(#reviewId)")
    public ResponseEntity<Void> deleteReviewById(@PathVariable Long reviewId){

        reviewService.deleteReviewById(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/company/{companyId}/exists")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> reviewsExistsByCompanyId(@PathVariable Long companyId){
        return new ResponseEntity<>(reviewService.reviewsExistsByCompanyId(companyId), HttpStatus.OK);
    }
}
