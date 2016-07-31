package com.drucare.api.exception;

public class CoreServiceConnectionException extends WyzbeeException {

    private static final long serialVersionUID = 7181725794304179399L;

    public CoreServiceConnectionException() {

    }

    public CoreServiceConnectionException(String message) {
        super(message);

    }

    public CoreServiceConnectionException(Throwable cause) {
        super(cause);

    }

    public CoreServiceConnectionException(String message, Throwable cause) {
        super(message, cause);

    }

    public CoreServiceConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

}
