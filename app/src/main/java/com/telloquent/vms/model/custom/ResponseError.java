package com.telloquent.vms.model.custom;

/**
 * Created by Telloquent-DM6M on 9/20/2017.
 */

public class ResponseError {
    private String error;
    private String error_description;
    private Long timestamp;
    private Number status;
    private String message;
    private String path;

    public ResponseError() {
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return this.error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Number getStatus() {
        return this.status;
    }

    public void setStatus(Number status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

