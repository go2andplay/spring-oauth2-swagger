package com.drucare.api.advice;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.drucare.util.WyzbeeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author ThirupathiReddy V
 *
 */
public class ErrorJson implements Serializable {

    private static final long serialVersionUID = -7985108499119014111L;
    private Integer status;
    private String error;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = WyzbeeUtil.FORMAT)
    private Date timeStamp;

    private String trace;

    public ErrorJson(int status, Map<String, Object> errorAttributes) {
        this.status = status;
        this.error = (String) errorAttributes.get("error");
        this.message = (String) errorAttributes.get("message");
        this.timeStamp = (Date) errorAttributes.get("timestamp");
        this.trace = (String) errorAttributes.get("trace");
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = WyzbeeUtil.FORMAT)
    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    @Override
    public String toString() {
        return "ErrorJson [status=" + status + ", error=" + error + ", message=" + message + ", timeStamp=" + timeStamp + ", trace=" + trace + "]";
    }

}