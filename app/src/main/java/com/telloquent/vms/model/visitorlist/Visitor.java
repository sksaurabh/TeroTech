
package com.telloquent.vms.model.visitorlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Visitor {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("vrfsid")
    @Expose
    private String vrfsid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getVrfsid() {
        return vrfsid;
    }

    public void setVrfsid(String vrfsid) {
        this.vrfsid = vrfsid;
    }

}
