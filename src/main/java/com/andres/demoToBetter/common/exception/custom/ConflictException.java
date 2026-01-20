package com.andres.demoToBetter.common.exception.custom;

public class ConflictException extends ApiException {
    public ConflictException(String code, String message) {
        super(code, message);
    }
}