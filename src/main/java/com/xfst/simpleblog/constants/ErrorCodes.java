package com.xfst.simpleblog.constants;

public enum ErrorCodes {
    ERROR_UNEXPECTED                 ("something went wrong", 500),
    ERROR_USER_INVALID_ID            ("bad user id", 400),
    ERROR_USER_NOT_FOUND             ("user not found", 404),
    ERROR_USER_INVALID_USERNAME      ("user not found", 400),
    ERROR_POST_INVALID_TITLE         ("bad post title", 400),
    ERROR_POST_NOT_FOUND             ("bad post title", 400),
    ERROR_POST_INVALID_ID            ("bad post id", 400);

    ErrorCodes(String message, int status) {
        this.message = message;
        this.status = status;
    }

    private final int status;
    private final String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
