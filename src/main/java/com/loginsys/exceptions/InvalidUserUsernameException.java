package com.loginsys.exceptions;

public class InvalidUserUsernameException extends RuntimeException {
    public InvalidUserUsernameException(String message) {
        super(message);
    }
}
