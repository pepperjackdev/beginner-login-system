package com.loginsys.exceptions;

public class SearchWithNonSearchablePropertyException extends RuntimeException {
    public SearchWithNonSearchablePropertyException(String message) {
        super(message);
    }
}
