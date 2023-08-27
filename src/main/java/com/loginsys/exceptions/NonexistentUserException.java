package com.loginsys.exceptions;

public class NonexistentUserException extends RuntimeException {
    public NonexistentUserException(String message) {
        super(message);
    }
}
