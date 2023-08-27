package com.loginsys.exceptions;

public class AlreadyTakenUserEmail extends RuntimeException {
    public AlreadyTakenUserEmail(String message) {
        super(message);
    }
}
