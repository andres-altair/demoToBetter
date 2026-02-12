package com.andres.demotobetter.common.exception.custom;
/**
 * Exception thrown when a request cannot be completed due to a conflict
 * @author andres
 */
public class ConflictException extends ApiException {
    /**
     * Creates a new ConflictException with a custom error code and message.
     *
     * @param code    a unique identifier for the error 
     * @param message a human-readable description of the conflict
     */
    public ConflictException(String code, String message) {
        super(code, message);
    }
}