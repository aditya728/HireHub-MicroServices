package com.charlie.hirehub.reviewservice.review;

import com.charlie.hirehub.reviewservice.review.dto.request.PostReviewRequest;
import com.charlie.hirehub.reviewservice.review.dto.response.ReviewDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ReviewDTO>> getAllReviewsForCompany(@RequestParam Long companyId){
        List<ReviewDTO> reviews = reviewService.getAllReviewsForCompany(companyId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> postReviewForCompany(@RequestParam Long companyId, @RequestBody PostReviewRequest reviewRequest){

        ReviewDTO reviewPosted = reviewService.postReviewForCompany(companyId, reviewRequest);
        return new ResponseEntity<>(reviewPosted, HttpStatus.CREATED);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewById( @PathVariable Long reviewId){
        Review review = reviewService.getReviewById(reviewId);

        if(review != null){
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReviewById(@PathVariable Long reviewId,
                                    @RequestBody Review review){
        boolean reviewUpdated = reviewService.updateReviewById(reviewId, review);

        if(reviewUpdated){
            return new ResponseEntity<>("Review updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review Not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReviewById(@PathVariable Long reviewId){
        boolean reviewDeleted = reviewService.deleteReviewById(reviewId);

        if(reviewDeleted){
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review Or Company Not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/company/{companyId}/exists")
    public ResponseEntity<Boolean> reviewsExistsByCompanyId(@PathVariable Long companyId){
        return new ResponseEntity<>(reviewService.reviewsExistsByCompanyId(companyId), HttpStatus.OK);
    }
}
