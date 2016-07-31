/**
 *
 */
package com.drucare.api.exception;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ThirupathiReddy V
 *
 */
public class WyzbeeException extends Exception {

    private static final long serialVersionUID = 2256701638974528822L;

    private final Map<String, String> errorMap = new LinkedHashMap<>();

    public Map<String, String> getErrorMap() {
        return this.errorMap;
    }

    /**
     *
     */
    public WyzbeeException() {

    }

    /**
     * @param message
     *            errorMessage
     */
    public WyzbeeException(String message) {
        super(message);

    }

    /**
     * @param cause
     *            rootCause
     */
    public WyzbeeException(Throwable cause) {
        super(cause);

    }

    /**
     * @param message
     *            errorMessage
     * @param cause
     *            cause
     */
    public WyzbeeException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * @param message
     *            errorMessage
     * @param cause
     *            rootCause
     * @param enableSuppression
     *            suppressionFlag
     * @param writableStackTrace
     *            writableStackTraceFlag
     */
    public WyzbeeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
