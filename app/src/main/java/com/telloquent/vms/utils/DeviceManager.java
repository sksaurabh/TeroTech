package com.telloquent.vms.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.Map;
import com.google.gson.Gson;
import com.telloquent.vms.VmsApplication;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.servicemanager.ServiceManager;

/**
 * Created by Telloquent-DM6M on 9/8/2017.
 */

public class DeviceManager {
    private static DeviceManager instance;
    private final static String TAG = DeviceManager.class.getName();
    private final static String kTOKEN = "kTOKEN";
    private final static String kTHEMEVALUES="kThemeValues";
    private final static String kPASSWORD="kPassword";
    private final static String kLICENSEKEY="kLicensekey";

    public DeviceManager() {

    }

    private void printPreferences() {
        Map<String, ?> keys = getSharedPreferences().getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d(TAG, "Preferences values" + entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }

    public static synchronized DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    private SharedPreferences getSharedPreferences() {
        return VmsApplication.getApplication().getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    private synchronized SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }
    public void saveTokenId(LicenseDetails licenseDetails) {
        if (licenseDetails != null) {
            ServiceManager.getInstance().setTokenId(licenseDetails);
            String oauthJSON = new Gson().toJson(licenseDetails);
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putString(kTOKEN, oauthJSON);
            editor.commit();
        }
        printPreferences();
    }

    public LicenseDetails getTokenId() {
        LicenseDetails licenseDetails = null;
        if (getSharedPreferences().contains(kTOKEN)) {
            licenseDetails = new Gson().fromJson(getSharedPreferences().getString(kTOKEN, ""), LicenseDetails.class);
        }
        return licenseDetails;
    }


    public void saveThemeValues(SettingDetailsResponse settingDetailsResponse) {
        if (settingDetailsResponse != null) {
            ServiceManager.getInstance().setThemeValues(settingDetailsResponse);
            String themeJSON = new Gson().toJson(settingDetailsResponse);
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putString(kTHEMEVALUES, themeJSON);
            editor.commit();
        }
        printPreferences();
    }


    public SettingDetailsResponse getThemeValues() {
        SettingDetailsResponse settingDetailsResponse = null;
        if (getSharedPreferences().contains(kTHEMEVALUES)) {
            settingDetailsResponse = new Gson().fromJson(getSharedPreferences().getString(kTHEMEVALUES, ""), SettingDetailsResponse.class);
        }
        return settingDetailsResponse;
    }


    public void saveSettingPassword(String password) {
        if (password != null) {
            ServiceManager.getInstance().setPassword(password);
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putString(kPASSWORD ,password);
            editor.commit();
        }
        printPreferences();
    }

    public String getPassword() {
      String password=null;
        if (getSharedPreferences().contains(kPASSWORD)) {
            password=getSharedPreferences().getString(kPASSWORD, "");
        }
        return password;
    }

    public void saveLicenseDetails(String licenseKey) {
        if (licenseKey != null) {
            ServiceManager.getInstance().saveLicenseKey(licenseKey);
            SharedPreferences.Editor editor = getSharedPreferencesEditor();
            editor.putString(kLICENSEKEY ,licenseKey);
            editor.commit();
        }
        printPreferences();
    }

    public String getLicenseDetails() {
        String licenseDetails=null;
        if (getSharedPreferences().contains(kLICENSEKEY)) {
            licenseDetails=getSharedPreferences().getString(kLICENSEKEY, "");
        }
        return licenseDetails;
    }

    private void clearToken() {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.remove(kTOKEN);
        editor.commit();
    }

}

