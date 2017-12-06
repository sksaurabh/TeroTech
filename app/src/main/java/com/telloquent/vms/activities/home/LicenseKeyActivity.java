package com.telloquent.vms.activities.home;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.telloquent.vms.R;
import com.telloquent.vms.activities.custom.SchedulerServiceCall;
import com.telloquent.vms.base.BaseActivity;
import com.telloquent.vms.base.BaseDisplayActivity;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.licensekeymodel.LicenseDetailsData;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.servicemanager.NetworkService;
import com.telloquent.vms.servicemanager.ServiceManager;
import com.telloquent.vms.support.SettingsDB;
import com.telloquent.vms.support.ThemeValuesDB;
import com.telloquent.vms.utils.AlertUtils;
import com.telloquent.vms.utils.ApplicationConstants;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.DeviceManager;
import com.telloquent.vms.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LicenseKeyActivity extends BaseDisplayActivity implements View.OnClickListener {
    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    private EditText mLicensekeyText;
    private Button mLicenceButton;
    private SettingsDB dbHelper;
    private TelephonyManager mManager;
    private String mImeiNum;
    private String mDeviceAuthorisation;
    private ThemeValuesDB dbThemeValuesHelper;
    private SettingDetailsResponse mSettingDetailsResponse;
    SimpleDateFormat df = ApplicationConstants.mDateTimeFormat;
    private String formattedDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_key);
    }

    protected void initializeView() {
        setupActionBar(getResources().getString(R.string.vms_screen), ActionBarActivityLeftAction.ACTION_NONE, BaseActivity.ActionBarActivityRightAction.ACTION_CLOSE, BaseActivity.ActionBarActivityRight2Action.ACTION_NONE);
        shouldDiscardChanges = true;
        mLicensekeyText = (EditText) findViewById(R.id.edittext_licensekey);
        mLicenceButton = (Button) findViewById(R.id.licence_button);
        mLicenceButton.setOnClickListener(this);
        Calendar c = Calendar.getInstance();
        formattedDate = df.format(c.getTime());
        setThemeValues();
    }

    private void setThemeValues() {
        if (DeviceManager.getInstance().getThemeValues() != null) {
            mSettingDetailsResponse = new SettingDetailsResponse();
            mSettingDetailsResponse = DeviceManager.getInstance().getThemeValues();
            if (mSettingDetailsResponse != null && mSettingDetailsResponse.getTheme().size() != 0) {
                for (int i = 0; i < mSettingDetailsResponse.getTheme().size(); i++) {
                    String identifire = mSettingDetailsResponse.getTheme().get(i).getIdentifier();
                    if (identifire.contains("button")) {
                        mLicenceButton.setBackgroundColor(Color.parseColor(mSettingDetailsResponse.getTheme().get(i).getValue().getBackgroundColor()));
                    }
                }
            }
        }
    }

    public void getData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionsAndShowPopup();
        } else {
            getDeviceAuthorisation();
        }
        dbHelper = new SettingsDB(this);
        dbThemeValuesHelper = new ThemeValuesDB(this);

    }

    private void getDeviceAuthorisation() {
        String deviceId = getDeviceid();
        mManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mImeiNum = mManager.getDeviceId();
        if (mImeiNum != null && deviceId != null) {
            mDeviceAuthorisation = mImeiNum + ":" + deviceId;
        } else {
            return;
        }
    }

    private void checkPermissionsAndShowPopup() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : ApplicationConstants.mPermissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), ApplicationConstants.MULTIPLE_PERMISSIONS);
            }
        } else
            getDeviceAuthorisation();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.licence_button:
                getData();
                if (validation() && formattedDate != null) {
                    serviceLicenseKey();
                }
                break;
        }
    }

    public String getDeviceid() {
        return Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private boolean validation() {
        boolean success = true;
        if (StringUtil.isEmpty(mLicensekeyText.getText().toString())) {
            mLicensekeyText.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        }
        return success;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        mLicensekeyText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (validation() && formattedDate != null) {
                        serviceLicenseKey();
                    }
                }

                return false;
            }
        });

    }

    public void serviceLicenseKey() {
        startBlockRefreshing();
        final NetworkService driverAccountService = ServiceManager.getInstance().createService(NetworkService.class);
        LicenseDetailsData licenseDetailsData = new LicenseDetailsData();
        licenseDetailsData.setLicenseKey(mLicensekeyText.getText().toString());
        licenseDetailsData.setDeviceConfiguration(mDeviceAuthorisation);
        Observable<LicenseDetails> driverAccountDetailResponseObservable = driverAccountService.getClassResponse(licenseDetailsData);
        driverAccountDetailResponseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LicenseDetails>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopBlockRefreshing();
                        handleError(e);
                    }

                    @Override
                    public void onNext(LicenseDetails licenseDetailsResponse) {
                        if (licenseDetailsResponse.getStatusCode() == 200) {
                            getVmsSettings(licenseDetailsResponse);
                        } else if (licenseDetailsResponse.getStatusCode() == 401) {
                            AlertUtils.showInfoDialog(LicenseKeyActivity.this, getString(R.string.app_name), getString(R.string.license_activity__key_error));
                            stopBlockRefreshing();
                        } else {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(LicenseKeyActivity.this, getString(R.string.app_name), licenseDetailsResponse.getMessage().toString());

                        }
                    }

                });
    }

    private void goToNextActivity() {
        if(mLicensekeyText.getText()!=null) {
            DeviceManager.getInstance().saveLicenseDetails(mLicensekeyText.getText().toString());
        }
        Intent intent = new Intent(LicenseKeyActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    private void getVmsSettings(final LicenseDetails licenseDetails) {
        startBlockRefreshing();
        final NetworkService networkService = ServiceManager.getInstance().createService(NetworkService.class);
        final Observable<SettingDetailsResponse> driverTripServiceObservable = networkService.getSettingDetailsByTokenId(licenseDetails.getToken());
        driverTripServiceObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SettingDetailsResponse>() {
                    @Override
                    public void onCompleted() {
                        backgroundServiceCall();
                        goToNextActivity();
                        stopBlockRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopBlockRefreshing();
                        handleError(e);
                    }

                    @Override
                    public void onNext(SettingDetailsResponse settingDetailsResponse) {
                        if (settingDetailsResponse.getStatusCode() == 200) {
                            dbHelper.insertSettings(licenseDetails, settingDetailsResponse, formattedDate);
                            dbThemeValuesHelper.insertTheme(settingDetailsResponse.getTheme());
                            DeviceManager.getInstance().saveTokenId(licenseDetails);

                        } else if (settingDetailsResponse.getStatusCode() == 401) {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(LicenseKeyActivity.this, getString(R.string.app_name), settingDetailsResponse.getMessage());
                        } else {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(LicenseKeyActivity.this, getString(R.string.app_name), settingDetailsResponse.getMessage());
                        }
                    }
                });
    }

    private void backgroundServiceCall() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(this, SchedulerServiceCall.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //for 24 hours
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                ApplicationConstants.mOneday, pintent);
        startService(new Intent(getBaseContext(), SchedulerServiceCall.class));
    }
}








