package com.cscb869.MedicalRecord.exception;

import org.aspectj.weaver.ast.Not;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
