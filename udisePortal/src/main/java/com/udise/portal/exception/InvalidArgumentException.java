package com.udise.portal.exception;

public class InvalidArgumentException extends CoreRuntimeException {

    private static final String INVALID_ARGUMENT = "Invalid argument supplied";

    private static final long serialVersionUID = -2422800408081166716L;

    private String paramName;

    public InvalidArgumentException(String errorCode) {
        super(errorCode);
    }

    public InvalidArgumentException(final String errorCode, final String paramName) {
        super(errorCode, INVALID_ARGUMENT);
        this.paramName = paramName;
    }

    public InvalidArgumentException(final String errorCode, final Throwable cause) {
        super(errorCode, cause);
    }

    public String getParamName() {
        return paramName;
    }

}
