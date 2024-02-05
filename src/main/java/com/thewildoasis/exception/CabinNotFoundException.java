package com.thewildoasis.exception;

public class CabinNotFoundException extends RuntimeException {
    public CabinNotFoundException(String message) {
        super(message);
    }
}
