package com.casemanagement.webservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CaseExceptionHandler {

    @ExceptionHandler(value = {CaseNotFoundException.class})
    public ResponseEntity<Object> handleCaseNotFoundException(CaseNotFoundException caseNotFoundException) {
        CaseException caseException = new CaseException(
                caseNotFoundException.getMessage(),
                caseNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(caseException, HttpStatus.NOT_FOUND);
    }
}
