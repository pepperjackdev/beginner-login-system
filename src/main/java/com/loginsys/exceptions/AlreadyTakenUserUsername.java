package com.loginsys.exceptions;

public class AlreadyTakenUserUsername extends RuntimeException {
    public AlreadyTakenUserUsername(String message) {
        super(message);
    }
}
