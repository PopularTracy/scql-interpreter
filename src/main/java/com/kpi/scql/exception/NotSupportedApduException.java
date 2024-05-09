package com.kpi.scql.exception;

public class NotSupportedApduException extends Exception {

    public NotSupportedApduException() {
    }

    public NotSupportedApduException(String message) {
        super(message);
    }
}
