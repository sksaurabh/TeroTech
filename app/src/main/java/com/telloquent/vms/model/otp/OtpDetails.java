package com.telloquent.vms.model.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpDetails {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("otp")
    @Expose
    private String otp;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}