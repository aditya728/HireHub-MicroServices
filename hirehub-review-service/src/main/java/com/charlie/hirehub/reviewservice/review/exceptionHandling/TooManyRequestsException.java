package com.charlie.hirehub.reviewservice.review.exceptionHandling;

public class TooManyRequestsException extends RuntimeException{

    public TooManyRequestsException() {
        super("Too many requests, please slow down");
    }

    public TooManyRequestsException(String message) {
        super(message);
    }

    public TooManyRequestsException(String message, Throwable cause) {
        super(message, cause);
    }
}
