package com.charlie.hirehub.companyservice.company.exception;

public class JobServiceUnavailableException extends RuntimeException {

    public JobServiceUnavailableException() {
        super("Job Service Unavailable!");
    }

    public JobServiceUnavailableException(String message) {
        super(message);
    }

    public JobServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
