package com.telloquent.vms.utils;
import com.telloquent.vms.VmsApplication;
import com.telloquent.vms.base.DataManager;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;

public class ApplicationStorage extends BaseStorage{
    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    private static ApplicationStorage instance;
    private LicenseDetails licenseDetailsResponse;
    private DataManager mDataManager;

    public ApplicationStorage() {
        this.mDataManager = new DataManager(VmsApplication.getApplication());
    }

    public void setLicenseDetailsResponse(LicenseDetails licenseDetailsResponse) {
        this.licenseDetailsResponse = licenseDetailsResponse;
    }

    public static synchronized  ApplicationStorage getInstance(){
        if (instance == null) {
            instance = new ApplicationStorage();
            VmsApplication.getApplication().setProperty(TAG, instance);
        }
        return instance;
    }

    public void saveToken(LicenseDetails licenseDetails) {
        DeviceManager.getInstance().saveTokenId(licenseDetails);
    }


    public LicenseDetails getLicenseDetailsResponse() {
        if (licenseDetailsResponse == null) {
            licenseDetailsResponse = DeviceManager.getInstance().getTokenId();
        }
        return licenseDetailsResponse;
    }

    public LicenseDetails geLicenseDetails() {
        if (getLicenseDetailsResponse() != null && getLicenseDetailsResponse().getToken() != null) {
            return licenseDetailsResponse;
        }
        return null;
    }



}
