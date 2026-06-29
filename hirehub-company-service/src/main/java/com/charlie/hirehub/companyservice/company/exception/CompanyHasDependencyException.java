package com.charlie.hirehub.companyservice.company.exception;

public class CompanyHasDependencyException extends RuntimeException{

    public CompanyHasDependencyException() {
        super("Cannot delete company because jobs or reviews exist for this company.");
    }

    public CompanyHasDependencyException(String message) {
        super(message);
    }

    public CompanyHasDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
