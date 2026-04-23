package com.cscb869.MedicalRecord.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
