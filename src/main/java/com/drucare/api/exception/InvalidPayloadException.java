package com.drucare.api.exception;

import java.util.LinkedHashMap;
import java.util.Map;

public class InvalidPayloadException extends WyzbeeException {

    private static final long serialVersionUID = -8262328645545539862L;

    private Map<String, String> errorMap = new LinkedHashMap<>();

    public InvalidPayloadException(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    @Override
    public Map<String, String> getErrorMap() {
        return this.errorMap;
    }

    public InvalidPayloadException(String message) {
        super(message);

    }

    public InvalidPayloadException(Throwable cause) {
        super(cause);

    }

    public InvalidPayloadException(String message, Throwable cause) {
        super(message, cause);

    }

    public InvalidPayloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

}
