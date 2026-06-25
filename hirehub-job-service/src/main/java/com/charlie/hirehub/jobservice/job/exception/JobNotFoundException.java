package com.charlie.hirehub.jobservice.job.exception;

public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException() {
        super("Job not found.");
    }

    public JobNotFoundException(String message) {
        super(message);
    }

    public JobNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
