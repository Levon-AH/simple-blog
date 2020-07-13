package com.xfst.simpleblog.exception;

public class SimpleBlogException extends RuntimeException {
    public SimpleBlogException() {
    }

    public SimpleBlogException(String message) {
        super(message);
    }

    public SimpleBlogException(String message, Throwable cause) {
        super(message, cause);
    }
}
