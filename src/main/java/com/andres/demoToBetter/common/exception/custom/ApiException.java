package com.andres.demoToBetter.common.exception.custom;
/**
 * Base exception class for application-specific errors.
 * @author andres
 */
public class ApiException extends RuntimeException {

    private final String code;

    /**
     * Creates a new ApiException with a custom error code and message.
     *
     * @param code    a unique identifier for the error 
     * @param message a human-readable description of the error
     */
    public ApiException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
