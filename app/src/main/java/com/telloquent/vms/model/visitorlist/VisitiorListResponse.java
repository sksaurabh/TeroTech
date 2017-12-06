
package com.telloquent.vms.model.visitorlist;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitiorListResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("visitors")
    @Expose
    private List<Visitor> visitors = null;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<Visitor> visitors) {
        this.visitors = visitors;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}
