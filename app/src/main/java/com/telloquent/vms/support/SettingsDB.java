package com.telloquent.vms.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.model.settingsmodel.Settings;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.DBHelper;

import java.util.Calendar;

/**
 * Created by Telloquent-DM6M on 9/12/2017.
 */

public class SettingsDB {
    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    //Table Name
    public static final String SETTINGS = "settings";
    //Table Field Namese
    private static final String ID = "ID";
    private static final String TOKEN = "TOKEN";
    private static final String ALLOW_CAPTURE_IMAGE = "ALLOW_CAPTURE_IMAGE";
    private static final String DISABLE_PERSONAL_VISIT = "DISABLE_PERSONAL_VISIT";
    private static final String REQUIRED_PRIMARY_COMPANY = "REQUIRED_PRIMARY_COMPANY";
    private static final String REQUIRED_PRIMARY_COMPANY_ADDRESS = "REQUIRED_PRIMARY_COMPANY_ADDRESS";
    private static final String REQUIRED_SECONDARY_COMPANY = "REQUIRED_SECONDARY_COMPANY";
    private static final String REQUIRED_SECONDARY_COMPANY_ADDRESS = "REQUIRED_SECONDARY_COMPANY_ADDRESS";
    private static final String SECURITY_IMAGE_OVERRIDE = "SECURITY_IMAGE_OVERRIDE";
    private static final String LOGO_URL = "LOGO_URL";
    private static final String CURRENT_DATE_TIME = "CURRENT_DATE_TIME";
    private static final String SECONDARY_VISITORS_DETAILS = "SECONDARY_VISITORS_DETAILS";
    private DBHelper dbh;
    private Context ctx;

    public SettingsDB(Context ctx) {
        this.ctx = ctx;
        dbh = new DBHelper(ctx);
    }

    public static String createSettingsSQL() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT,%S INTEGER,%s VARCHAR,%S INTEGER,%S INTEGER,%S INTEGER,%S INTEGER,%S INTEGER,%S INTEGER,%s VARCHAR,%S INTEGER)", SETTINGS, ID, TOKEN, ALLOW_CAPTURE_IMAGE, LOGO_URL, DISABLE_PERSONAL_VISIT, REQUIRED_PRIMARY_COMPANY, REQUIRED_PRIMARY_COMPANY_ADDRESS, REQUIRED_SECONDARY_COMPANY, REQUIRED_SECONDARY_COMPANY_ADDRESS, SECURITY_IMAGE_OVERRIDE, CURRENT_DATE_TIME, SECONDARY_VISITORS_DETAILS);
    }

    public long insertSettings(LicenseDetails licenseDetails, SettingDetailsResponse settingDetails, String formatedDate) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        long mTotalInsertedValues = 0;
        try {
            db.execSQL("DELETE FROM settings");
            Log.i(TAG, "Done");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error");
        }
        try {
            if (settingDetails != null) {
                ContentValues values = new ContentValues();
                values.put(TOKEN, licenseDetails.getToken());
                values.put(ALLOW_CAPTURE_IMAGE, settingDetails.getSettings().getAllowCaptureImage());
                values.put(DISABLE_PERSONAL_VISIT, settingDetails.getSettings().getDisablePersonalVisits());
                values.put(REQUIRED_PRIMARY_COMPANY, settingDetails.getSettings().getRequirePrimaryCompany());
                values.put(REQUIRED_PRIMARY_COMPANY_ADDRESS, settingDetails.getSettings().getRequirePrimaryCompanyAddress());
                values.put(REQUIRED_SECONDARY_COMPANY, settingDetails.getSettings().getRequireSecondaryCompany());
                values.put(REQUIRED_SECONDARY_COMPANY_ADDRESS, settingDetails.getSettings().getRequireSecondaryCompanyAddress());
                values.put(SECURITY_IMAGE_OVERRIDE, settingDetails.getSettings().getSecurityImageOverride());
                values.put(SECONDARY_VISITORS_DETAILS, settingDetails.getSettings().getSecondaryVisitorDetail());
                values.put(LOGO_URL, settingDetails.getLogoUrl());
                values.put(CURRENT_DATE_TIME, formatedDate);
                long rowId = db.insert(SETTINGS, null, values);
                if (rowId != 0) {
                    mTotalInsertedValues++;
                }
            } else {
                Log.i(TAG, "value Null or Empty");
            }
            Log.i(TAG, "Rows " + mTotalInsertedValues);
        } catch (Exception ex) {
        } finally {
            dbh.exportDB(ctx);
            dbh.closeDB();
        }
        return mTotalInsertedValues;
    }

    public LicenseDetails getTokenKeyDetail() {
        LicenseDetails tokenId = null;
        try {
            SQLiteDatabase db = dbh.getReadableDatabase();
            String selectQuery = String.format("SELECT  * FROM settings");
            Cursor c = db.rawQuery(selectQuery, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    tokenId = new LicenseDetails();
                    tokenId.setToken(c.getString(c.getColumnIndex(TOKEN)));
                    c.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbh.closeDB();
        }
        return tokenId;
    }

    public SettingDetailsResponse getSettingDetails() {
        SettingDetailsResponse settingDetails = null;
        SQLiteDatabase db = dbh.getReadableDatabase();
        try {
            String selectQuery = String.format("SELECT  * FROM settings");
            Cursor c = db.rawQuery(selectQuery, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    settingDetails = new SettingDetailsResponse();
                    Settings settings = new Settings();
                    settingDetails.setLogoUrl(c.getString(c.getColumnIndex(LOGO_URL)));
                    settings.setAllowCaptureImage(c.getInt(c.getColumnIndex(ALLOW_CAPTURE_IMAGE)));
                    settings.setDisablePersonalVisits(c.getInt(c.getColumnIndex(DISABLE_PERSONAL_VISIT)));
                    settings.setRequirePrimaryCompany(c.getInt(c.getColumnIndex(REQUIRED_PRIMARY_COMPANY)));
                    settings.setRequirePrimaryCompanyAddress(c.getInt(c.getColumnIndex(REQUIRED_PRIMARY_COMPANY_ADDRESS)));
                    settings.setRequireSecondaryCompany(c.getInt(c.getColumnIndex(REQUIRED_SECONDARY_COMPANY)));
                    settings.setRequireSecondaryCompanyAddress(c.getInt(c.getColumnIndex(REQUIRED_SECONDARY_COMPANY_ADDRESS)));
                    settings.setSecurityImageOverride(c.getInt(c.getColumnIndex(SECURITY_IMAGE_OVERRIDE)));
                    settings.setSecondaryVisitorDetail(c.getInt(c.getColumnIndex(SECONDARY_VISITORS_DETAILS)));
                    settingDetails.setDateTime(c.getString(c.getColumnIndex(CURRENT_DATE_TIME)));
                    settingDetails.setSettings(settings);
                    c.moveToNext();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbh.closeDB();
        }
        return settingDetails;
    }
}

