package com.kpi.scql.exception;

public class NoSuchOperationException extends RuntimeException {

    public NoSuchOperationException() {
    }

    public NoSuchOperationException(String message) {
        super(message);
    }

    public NoSuchOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
