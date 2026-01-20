package com.andres.demoToBetter.common.exception.custom;

public class NotFoundException extends ApiException {
    public NotFoundException(String code, String message) {
        super(code, message);
    }
}