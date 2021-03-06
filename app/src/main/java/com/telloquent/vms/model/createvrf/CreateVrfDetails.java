
package com.telloquent.vms.model.createvrf;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateVrfDetails {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("company")
    @Expose
    private String company;
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
    @SerializedName("vrf_type")
    @Expose
    private String vrfType;
    @SerializedName("employee_email")
    @Expose
    private Object employeeEmail;
    @SerializedName("employee_phone")
    @Expose
    private Object employeePhone;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("duration")
    @Expose
    private String duration;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getVrfType() {
        return vrfType;
    }

    public void setVrfType(String vrfType) {
        this.vrfType = vrfType;
    }

    public Object getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(Object employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public Object getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(Object employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

}