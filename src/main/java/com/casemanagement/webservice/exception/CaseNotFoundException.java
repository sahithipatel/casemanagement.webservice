package com.casemanagement.webservice.exception;

public class CaseNotFoundException extends RuntimeException {

    public CaseNotFoundException(String message) {
        super(message);
    }

    public CaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
