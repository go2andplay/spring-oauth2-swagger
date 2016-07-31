package com.drucare.api.exception;

public class ChangeNotAllowedException extends WyzbeeException {

    private static final long serialVersionUID = -4384728182356697855L;

    public ChangeNotAllowedException() {

    }

    public ChangeNotAllowedException(String message) {
        super(message);

    }

    public ChangeNotAllowedException(Throwable cause) {
        super(cause);

    }

    public ChangeNotAllowedException(String message, Throwable cause) {
        super(message, cause);

    }

    public ChangeNotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

}
