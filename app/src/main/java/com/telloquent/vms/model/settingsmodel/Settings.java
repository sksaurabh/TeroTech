
package com.telloquent.vms.model.settingsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {

    @SerializedName("allow_capture_image")
    @Expose
    private Integer allowCaptureImage;
    @SerializedName("security_image_override")
    @Expose
    private Integer securityImageOverride;
    @SerializedName("disable_personal_visits")
    @Expose
    private Integer disablePersonalVisits;
    @SerializedName("require_primary_company")
    @Expose
    private Integer requirePrimaryCompany;
    @SerializedName("require_primary_company_address")
    @Expose
    private Integer requirePrimaryCompanyAddress;
    @SerializedName("require_secondary_company")
    @Expose
    private Integer requireSecondaryCompany;
    @SerializedName("require_secondary_company_address")
    @Expose
    private Integer requireSecondaryCompanyAddress;
    @SerializedName("secondary_visitor_detail")
    @Expose
    private Integer secondaryVisitorDetail;

    public Integer getAllowCaptureImage() {
        return allowCaptureImage;
    }

    public void setAllowCaptureImage(Integer allowCaptureImage) {
        this.allowCaptureImage = allowCaptureImage;
    }

    public Integer getSecurityImageOverride() {
        return securityImageOverride;
    }

    public void setSecurityImageOverride(Integer securityImageOverride) {
        this.securityImageOverride = securityImageOverride;
    }

    public Integer getDisablePersonalVisits() {
        return disablePersonalVisits;
    }

    public void setDisablePersonalVisits(Integer disablePersonalVisits) {
        this.disablePersonalVisits = disablePersonalVisits;
    }

    public Integer getRequirePrimaryCompany() {
        return requirePrimaryCompany;
    }

    public void setRequirePrimaryCompany(Integer requirePrimaryCompany) {
        this.requirePrimaryCompany = requirePrimaryCompany;
    }

    public Integer getRequirePrimaryCompanyAddress() {
        return requirePrimaryCompanyAddress;
    }

    public void setRequirePrimaryCompanyAddress(Integer requirePrimaryCompanyAddress) {
        this.requirePrimaryCompanyAddress = requirePrimaryCompanyAddress;
    }

    public Integer getRequireSecondaryCompany() {
        return requireSecondaryCompany;
    }

    public void setRequireSecondaryCompany(Integer requireSecondaryCompany) {
        this.requireSecondaryCompany = requireSecondaryCompany;
    }

    public Integer getRequireSecondaryCompanyAddress() {
        return requireSecondaryCompanyAddress;
    }

    public void setRequireSecondaryCompanyAddress(Integer requireSecondaryCompanyAddress) {
        this.requireSecondaryCompanyAddress = requireSecondaryCompanyAddress;
    }

    public Integer getSecondaryVisitorDetail() {
        return secondaryVisitorDetail;
    }

    public void setSecondaryVisitorDetail(Integer secondaryVisitorDetail) {
        this.secondaryVisitorDetail = secondaryVisitorDetail;
    }

}
