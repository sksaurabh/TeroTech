
package com.telloquent.vms.model.createvrf;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateVrfDetailsResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("request")
    @Expose
    private Request request;
    @SerializedName("is_primary")
    @Expose
    private Integer isPrimary;
    @SerializedName("id_proof_path")
    @Expose
    private Integer idProofPath;
    @SerializedName("vrfid")
    @Expose
    private String vrfid;
    @SerializedName("vrfsid")
    @Expose
    private String vrfsid;
    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("to_meet")
    @Expose
    private String toMeet;
    @SerializedName("entry_time")
    @Expose
    private String entryTime;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Integer getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Integer isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Integer getIdProofPath() {
        return idProofPath;
    }

    public void setIdProofPath(Integer idProofPath) {
        this.idProofPath = idProofPath;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getToMeet() {
        return toMeet;
    }

    public void setToMeet(String toMeet) {
        this.toMeet = toMeet;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}
