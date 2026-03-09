package com.andres.demotobetter.common.domain;
/**
 * Exception thrown when a request contains invalid or malformed data.
 * @author andres
 */
public class BadRequestException extends BaseBusinessException {
    /**
     * Creates a new BadRequestException with a custom error code and message.
     *
     * @param code    a unique identifier for the error 
     * @param message a human-readable description of the validation or request issue
     */
    public BadRequestException(String code, String message) {
        super(code, message);
    }
}