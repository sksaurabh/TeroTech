package com.telloquent.vms.model.additionalvisitors;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdditionalVrfResponse {

    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}