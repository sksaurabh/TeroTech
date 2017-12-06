
package com.telloquent.vms.model.licensekeymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LicenseDetailsvalues {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}
