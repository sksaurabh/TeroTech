
package com.telloquent.vms.model.createvrf;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Request {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;
    @SerializedName("vrf_type")
    @Expose
    private String vrfType;
    @SerializedName("employee_email")
    @Expose
    private String employeeEmail;
    @SerializedName("employee_phone")
    @Expose
    private String employeePhone;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("scan_file")
    @Expose
    private ScanFile scanFile;

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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
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

    public String getVrfType() {
        return vrfType;
    }

    public void setVrfType(String vrfType) {
        this.vrfType = vrfType;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ScanFile getScanFile() {
        return scanFile;
    }

    public void setScanFile(ScanFile scanFile) {
        this.scanFile = scanFile;
    }

}
