package com.drucare.api.advice;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = 4908507324821258832L;

    private String status = "failed";

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getErrors() {
        return Collections.unmodifiableMap(this.errors);
    }

    public void addError(String field, String message) {
        this.errors.put(field, message);
    }

    private final Map<String, String> errors = new LinkedHashMap<String, String>();

}
