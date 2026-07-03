package com.charlie.hirehub.companyservice.company.exception;

public class ReviewServiceUnavailableException extends RuntimeException {

    public ReviewServiceUnavailableException() {
        super("Job Service Unavailable!");
    }

    public ReviewServiceUnavailableException(String message) {
        super(message);
    }

    public ReviewServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
