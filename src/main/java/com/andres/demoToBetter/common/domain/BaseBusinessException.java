package com.andres.demotobetter.common.domain;
/**
 * Base exception class for application-specific errors.
 * @author andres
 */
public class BaseBusinessException extends RuntimeException {

    private final String code;

    /**
     * Creates a new ApiException with a custom error code and message.
     *
     * @param code    a unique identifier for the error 
     * @param message a human-readable description of the error
     */
    public BaseBusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
