
package com.telloquent.vms.model.additionalvisitors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdditionalVrfRequest {

    @SerializedName("vrfid")
    @Expose
    private String vrfid;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("scan_file")
    @Expose
    private String scanFile;

    public String getVrfid() {
        return vrfid;
    }

    public void setVrfid(String vrfid) {
        this.vrfid = vrfid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getScanFile() {
        return scanFile;
    }

    public void setScanFile(String scanFile) {
        this.scanFile = scanFile;
    }

}