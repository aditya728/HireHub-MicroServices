package com.charlie.hirehub.reviewservice.review;

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
public class ReviewController {

    ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviewsForCompany(@RequestParam Long companyId){
        List<Review> reviews = reviewService.getAllReviewsForCompany(companyId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postReviewForCompany(@RequestParam Long companyId, @RequestBody Review review){
        boolean reviewPosted = reviewService.postReviewForCompany(companyId, review);
        if(reviewPosted)
            return new ResponseEntity<>("Review added successfully", HttpStatus.CREATED);
        return new ResponseEntity<>("Review not added", HttpStatus.NOT_FOUND);
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
}
