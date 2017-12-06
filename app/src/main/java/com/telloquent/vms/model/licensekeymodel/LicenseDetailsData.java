
package com.telloquent.vms.model.licensekeymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LicenseDetailsData {

    @SerializedName("license_key")
    @Expose
    private String licenseKey;
    @SerializedName("device_configuration")
    @Expose
    private String deviceConfiguration;

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getDeviceConfiguration() {
        return deviceConfiguration;
    }

    public void setDeviceConfiguration(String deviceConfiguration) {
        this.deviceConfiguration = deviceConfiguration;
    }

}
