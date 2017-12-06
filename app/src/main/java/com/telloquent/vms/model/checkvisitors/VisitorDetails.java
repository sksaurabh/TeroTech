
package com.telloquent.vms.model.checkvisitors;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitorDetails {

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
