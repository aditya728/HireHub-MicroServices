package com.charlie.hirehub.reviewservice.review.exception;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException() {
        super("Review not found.");
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }

    public ReviewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}