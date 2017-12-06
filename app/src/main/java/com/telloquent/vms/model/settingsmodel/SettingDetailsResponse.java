
package com.telloquent.vms.model.settingsmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingDetailsResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("logo_url")
    @Expose
    private String logoUrl;
    @SerializedName("departments")
    @Expose
    private List<Department> departments = null;
    @SerializedName("purposes")
    @Expose
    private List<Purpose> purposes = null;
    @SerializedName("theme")
    @Expose
    private List<Theme> theme = null;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Purpose> getPurposes() {
        return purposes;
    }

    public void setPurposes(List<Purpose> purposes) {
        this.purposes = purposes;
    }

    public List<Theme> getTheme() {
        return theme;
    }

    public void setTheme(List<Theme> theme) {
        this.theme = theme;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

}
