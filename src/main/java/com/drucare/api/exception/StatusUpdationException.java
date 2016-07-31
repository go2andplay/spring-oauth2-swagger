package com.drucare.api.exception;

/**
 * Used to send status messages using AOP advice
 * 
 * @author ThirupathiReddy V
 *
 */
public class StatusUpdationException extends WyzbeeException {

    private static final long serialVersionUID = 8961024902485191474L;

    public StatusUpdationException() {

    }

    public StatusUpdationException(String message) {
        super(message);

    }

    public StatusUpdationException(Throwable cause) {
        super(cause);

    }

    public StatusUpdationException(String message, Throwable cause) {
        super(message, cause);

    }

    public StatusUpdationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

}
