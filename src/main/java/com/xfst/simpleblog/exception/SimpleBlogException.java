package com.xfst.simpleblog.exception;

import com.xfst.simpleblog.constants.ErrorCodes;

public class SimpleBlogException extends RuntimeException {

    private ErrorCodes errorCodes;
    public SimpleBlogException() {
    }

    public SimpleBlogException(String message) {
        super(message);
    }

    public SimpleBlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpleBlogException(ErrorCodes errorCodes) {
        this.errorCodes = errorCodes;
    }

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }
}
