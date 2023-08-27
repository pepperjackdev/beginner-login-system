package com.loginsys.exceptions;

public class InvalidUserPasswordException extends RuntimeException {
    public InvalidUserPasswordException(String message) {
        super(message);
    }
}
