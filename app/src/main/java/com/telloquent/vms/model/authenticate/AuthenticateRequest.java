
package com.telloquent.vms.model.authenticate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticateRequest {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("vrfsid")
    @Expose
    private String vrfsid;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVrfsid() {
        return vrfsid;
    }

    public void setVrfsid(String vrfsid) {
        this.vrfsid = vrfsid;
    }

}