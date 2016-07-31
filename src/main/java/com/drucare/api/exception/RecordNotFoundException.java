package com.drucare.api.exception;

/**
 *
 * @author ThirupathiReddy V
 *
 */
public class RecordNotFoundException extends WyzbeeException {

    private static final long serialVersionUID = -5453616459647055740L;

    public RecordNotFoundException() {

    }

    public RecordNotFoundException(String message) {
        super(message);

    }

    public RecordNotFoundException(Throwable cause) {
        super(cause);

    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);

    }

    public RecordNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

}
