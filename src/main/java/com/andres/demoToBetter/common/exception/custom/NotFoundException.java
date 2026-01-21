package com.andres.demoToBetter.common.exception.custom;
/**
 * Exception thrown when a requested resource cannot be found.
 * @author andres
 */
public class NotFoundException extends ApiException {
    /**
     * Creates a new NotFoundException with a custom error code and message.
     *
     * @param code    a unique identifier for the error 
     * @param message a human-readable description of the missing resource
     */
    public NotFoundException(String code, String message) {
        super(code, message);
    }
}