
package com.telloquent.vms.model.authenticate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticateResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("is_primary")
    @Expose
    private Integer isPrimary;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("vrfid")
    @Expose
    private String vrfid;
    @SerializedName("vrfsid")
    @Expose
    private String vrfsid;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("meeting_room")
    @Expose
    private String meetingRoom;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;
    @SerializedName("entry_time")
    @Expose
    private String entryTime;
    @SerializedName("exit_time")
    @Expose
    private String exitTime;
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
    @SerializedName("id_proof_url")
    @Expose
    private Integer idProofUrl;
    @SerializedName("vrf_type")
    @Expose
    private String vrfType;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Integer isPrimary) {
        this.isPrimary = isPrimary;
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

    public String getVrfid() {
        return vrfid;
    }

    public void setVrfid(String vrfid) {
        this.vrfid = vrfid;
    }

    public String getVrfsid() {
        return vrfsid;
    }

    public void setVrfsid(String vrfsid) {
        this.vrfsid = vrfsid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(String meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
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

    public Integer getIdProofUrl() {
        return idProofUrl;
    }

    public void setIdProofUrl(Integer idProofUrl) {
        this.idProofUrl = idProofUrl;
    }

    public String getVrfType() {
        return vrfType;
    }

    public void setVrfType(String vrfType) {
        this.vrfType = vrfType;
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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}