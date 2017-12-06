package com.telloquent.vms.activities.custom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Telloquent-DM6M on 9/21/2017.
 */

public class OperationStatus {
    @SerializedName("status_code")
    @Expose
    private String status_code;
    @SerializedName("messages")
    @Expose
    private List<String> messages = null;

    public OperationStatus() {
    }

    public String getStatusCode() {
        return this.status_code;
    }

    public void setStatusCode(String status_code) {
        this.status_code = status_code;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}

