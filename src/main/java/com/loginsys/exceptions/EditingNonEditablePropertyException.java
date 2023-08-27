package com.loginsys.exceptions;

public class EditingNonEditablePropertyException extends RuntimeException {
    public EditingNonEditablePropertyException(String message) {
        super(message);
    }
}
