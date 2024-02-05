package com.thewildoasis.exception;

public class GlobalAppException extends RuntimeException {
    public GlobalAppException(String message) {
        super(message);
    }

    public GlobalAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
