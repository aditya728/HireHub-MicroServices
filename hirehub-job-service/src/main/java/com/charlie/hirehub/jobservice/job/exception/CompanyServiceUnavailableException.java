package com.charlie.hirehub.jobservice.job.exception;

public class CompanyServiceUnavailableException extends RuntimeException {

    public CompanyServiceUnavailableException() {
        super("Company Service Unavailable!");
    }

    public CompanyServiceUnavailableException(String message) {
        super(message);
    }

    public CompanyServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}