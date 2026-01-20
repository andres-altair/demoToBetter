package com.andres.demoToBetter.common.exception.custom;

public class BadRequestException extends ApiException {
    public BadRequestException(String code, String message) {
        super(code, message);
    }
}