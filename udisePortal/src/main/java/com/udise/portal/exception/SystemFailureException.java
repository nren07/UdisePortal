package com.udise.portal.exception;

public class SystemFailureException extends RuntimeException{
    private static final long serialVersionUID = -4100219781374708972L;

    public SystemFailureException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
