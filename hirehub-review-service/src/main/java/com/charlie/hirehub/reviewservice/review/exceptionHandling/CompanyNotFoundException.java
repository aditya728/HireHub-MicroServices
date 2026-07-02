package com.charlie.hirehub.reviewservice.review.exceptionHandling;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException() {
        super("Company not found.");
    }

    public CompanyNotFoundException(String message) {
        super(message);
    }

    public CompanyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
