package com.loginsys.exceptions;

public class InvalidUserEmailException extends RuntimeException {
    public InvalidUserEmailException(String message) {
        super(message);
    }
}
