package com.charlie.hirehub.companyservice.company.exception;

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
