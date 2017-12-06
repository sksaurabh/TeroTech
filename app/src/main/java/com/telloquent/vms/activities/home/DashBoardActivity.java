package com.telloquent.vms.activities.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.telloquent.vms.R;
import com.telloquent.vms.activities.custom.CaptureActivityLandscape;
import com.telloquent.vms.activities.settings.SettingsActivity;
import com.telloquent.vms.base.BaseActivity;
import com.telloquent.vms.base.BaseDisplayActivity;
import com.telloquent.vms.base.IAlertDialogCallback;
import com.telloquent.vms.model.authenticate.AuthenticateRequest;
import com.telloquent.vms.model.authenticate.AuthenticateResponse;
import com.telloquent.vms.model.checkvisitors.VisitorDetails;
import com.telloquent.vms.model.checkvisitors.VisitorDetailsResponse;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.otp.OtpDetails;
import com.telloquent.vms.model.otp.OtpDetailsResponse;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.model.settingsmodel.Theme;
import com.telloquent.vms.servicemanager.NetworkService;
import com.telloquent.vms.servicemanager.ServiceManager;
import com.telloquent.vms.support.ThemeValuesDB;
import com.telloquent.vms.utils.AlertUtils;
import com.telloquent.vms.utils.ApplicationConstants;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.DeviceManager;
import com.telloquent.vms.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SuppressWarnings("deprecation")
public class DashBoardActivity extends BaseDisplayActivity implements View.OnClickListener {
    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    private SettingDetailsResponse mSettingDetailsResponse;
    private Button mVrfIdMobNumButton, mScanQr;
    private Button mRegisterVrf;
    private IntentIntegrator integrator;
    private EditText mPassword;
    private IntentResult mResult = null;
    private LicenseDetails mLicenseDetails;
    private VisitorDetails mVisitorDetails;
    private String mOtpValue, mOtpValueforAuthenticate;
    private List<Theme> themes;
    private ThemeValuesDB dbThemeValuesHelper;
    private char[] mOtpRandomNum;
    private OtpDetails otpDetails;
    private ArrayList<String> mRandomNum;
    private AuthenticateRequest authenticateRequest;
    private String mTereotech, mCompanyName, mCompanyVrfSid;
    private EditText enterMobVrfid, enterMobnumber;
    private ImageView mVrfSettingsDetails;
    private EditText firstEdittext, twoEdittext, threeEdittext, fourEdittext, fiveEdittext, sixEdittext;
    int registerotpCount = 0, authenticateotpCount = 0;
    Button resendButton, mOtpNotRecivedRegistration, resendAuthenticationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
    }

    protected void initializeView() {
        setupActionBar(getResources().getString(R.string.please_check_in), ActionBarActivityLeftAction.WELCOME, BaseActivity.ActionBarActivityRightAction.ACTION_NONE, ActionBarActivityRight2Action.COMPANY_LOGO);
        shouldDiscardChanges = true;
        mVrfIdMobNumButton = (Button) findViewById(R.id.enter_mob_or_vrf_id);
        mRegisterVrf = (Button) findViewById(R.id.register_walking_visitor);
        mScanQr = (Button) findViewById(R.id.qr_code_scanner);
        mVrfSettingsDetails = (ImageView) findViewById(R.id.vrf_settings_details);
        mVrfIdMobNumButton.setOnClickListener(this);
        mScanQr.setOnClickListener(this);
        mRegisterVrf.setOnClickListener(this);
        mVrfSettingsDetails.setOnClickListener(this);
        dbThemeValuesHelper = new ThemeValuesDB(this);
        getData();
    }

    @Override
    public void getData() {
        mRandomNum = new ArrayList<>();
        otpDetails = new OtpDetails();
        mVisitorDetails = new VisitorDetails();
        if (DeviceManager.getInstance().getPassword() == null) {
            popsetSettingPassword();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter_mob_or_vrf_id:
                if (DeviceManager.getInstance().getTokenId() != null) {
                    vrfMobnumberPopup();
                } else {
                    AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.invalid_token));
                }
                break;
            case R.id.qr_code_scanner:
                scanBarcode();
                break;
            case R.id.register_walking_visitor:
                registrationWalkinVrf();
                break;
            case R.id.vrf_settings_details:
                if (DeviceManager.getInstance().getPassword() == null) {
                    popsetSettingPassword();
                } else {
                    popSettingPasswordActivated();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                //  AlertUtils.showInfoDialog(this, getString(R.string.app_name), getString(R.string.cancelled));
            } else {
                if (DeviceManager.getInstance().getTokenId() != null) {
                    mTereotech = result.getContents();
                    try {
                        String[] separated = mTereotech.split("://");
                        mCompanyName = separated[0];
                        mCompanyVrfSid = separated[1];
                        if (mCompanyVrfSid != null) {
                            mLicenseDetails = DeviceManager.getInstance().getTokenId();
                            authenticateRequest = new AuthenticateRequest();
                            authenticateRequest.setVrfsid(mCompanyVrfSid);
                            authenticateVisitors(mLicenseDetails, authenticateRequest);
                        } else {
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.invalid_qr));
                        }
                    } catch (Exception e) {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
                    }
                }
            }
        }
    }
    //------------------------------Qr code scanner--------------------------

    private void scanBarcode() {
        integrator = new IntentIntegrator(DashBoardActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(getString(R.string.scan_msg_default_status));
        integrator.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(CaptureActivityLandscape.class);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }


    public void checkVisitors(LicenseDetails mLicenseDetails, VisitorDetails visitorDetailsList) {
        startBlockRefreshing();
        final NetworkService checkvisitorsService = ServiceManager.getInstance().createService(NetworkService.class);
        Observable<VisitorDetailsResponse> checkVisitorsObservable = checkvisitorsService.visitorDetailsServiceResponse(mLicenseDetails.getToken(), visitorDetailsList);
        checkVisitorsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VisitorDetailsResponse>() {
                    @Override
                    public void onCompleted() {
                        stopBlockRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopBlockRefreshing();
                        handleError(e);
                    }

                    @Override
                    public void onNext(VisitorDetailsResponse checkVisitorsResponse) {
                        if (checkVisitorsResponse.getStatusCode() == 200) {
                            Intent intent = CreateNewVrfActivity.createIntentWithBundle(getApplicationContext(), checkVisitorsResponse);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (checkVisitorsResponse.getStatusCode() == 500) {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.enter_password_wrong));
                        } else {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
                        }
                    }
                });
    }


    //----------------------------------------send otp redirection in android---------------------------------

    private void otpService() {
        if (DeviceManager.getInstance().getTokenId() != null) {
            mOtpRandomNum = OtpRandomNumberGenration(ApplicationConstants.length);
            otpDetails.setPhone(enterMobnumber.getText().toString());
            otpDetails.setOtp(String.valueOf(mOtpRandomNum));
            mRandomNum.add(String.valueOf(mOtpRandomNum));
            mLicenseDetails = DeviceManager.getInstance().getTokenId();
            if (otpDetails.getPhone() != null && otpDetails.getOtp() != null) {
                sendOtp(mLicenseDetails, otpDetails);
            } else {
                AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.activity_mandatory_fields_missing));
            }
        } else {
            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.token_id));
        }
    }

    public void sendOtp(final LicenseDetails mLicenseDetails, OtpDetails otpDetailsList) {
        startBlockRefreshing();
        final NetworkService otpDetailsService = ServiceManager.getInstance().createService(NetworkService.class);
        Observable<OtpDetailsResponse> checkVisitorsObservable = otpDetailsService.otpServiceResponse(mLicenseDetails.getToken(), otpDetailsList);
        checkVisitorsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OtpDetailsResponse>() {
                    @Override
                    public void onCompleted() {
                        stopBlockRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopBlockRefreshing();
                        handleError(e);
                    }

                    @Override
                    public void onNext(OtpDetailsResponse otpDetailsResponse) {
                        if (otpDetailsResponse.getStatusCode().equalsIgnoreCase("200")) {
                            popsetOtp(mLicenseDetails);
                        } else if (otpDetailsResponse.getStatusCode().equalsIgnoreCase("401")) {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), otpDetailsResponse.getMessage());
                        } else {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
                        }
                    }
                });
    }


    private static char[] OtpRandomNumberGenration(int len) {
        String numbers = ApplicationConstants.otpRandomNum;
        Random randomOtp = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(randomOtp.nextInt(numbers.length()));
        }
        return otp;
    }

    //-----------------------------otp genration screen---------------------
    private boolean validationOtp() {
        boolean success = true;
        if (StringUtil.isEmpty(firstEdittext.getText().toString())) {
            firstEdittext.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        } else
            firstEdittext.setError(null);
        if (StringUtil.isEmpty(twoEdittext.getText().toString())) {
            twoEdittext.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        } else
            twoEdittext.setError(null);
        if (StringUtil.isEmpty(threeEdittext.getText().toString())) {
            threeEdittext.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        } else
            threeEdittext.setError(null);
        if (StringUtil.isEmpty(fourEdittext.getText().toString())) {
            fourEdittext.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        } else
            fourEdittext.setError(null);
        if (StringUtil.isEmpty(fiveEdittext.getText().toString())) {
            fiveEdittext.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        } else
            fiveEdittext.setError(null);

        if (StringUtil.isEmpty(sixEdittext.getText().toString())) {
            sixEdittext.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        } else
            sixEdittext.setError(null);

        return success;
    }


    //TODO-----------------------authentivate Service-------------------------------------

    public void authenticateVisitors(LicenseDetails mLicenseDetails, AuthenticateRequest authenticateRequest) {
        startBlockRefreshing();
        final NetworkService authenticateService = ServiceManager.getInstance().createService(NetworkService.class);
        Observable<AuthenticateResponse> authenticateResponseObservable = authenticateService.authenticateServiceResponse(mLicenseDetails.getToken(), authenticateRequest);
        authenticateResponseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AuthenticateResponse>() {
                    @Override
                    public void onCompleted() {
                        stopBlockRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopBlockRefreshing();
                        handleError(e);
                    }

                    @Override
                    public void onNext(AuthenticateResponse authenticateResponse) {
                        if (authenticateResponse.getStatusCode() == 200) {
                            Intent intent = PrimaryVisitorDetailActivity.createIntentWithBundleAuthenticate(getApplicationContext(), authenticateResponse);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);

                        } else if (authenticateResponse.getStatusCode() == 500) {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), authenticateResponse.getMessage());
                        } else {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), authenticateResponse.getMessage());
                        }
                    }
                });
    }

    //-----------------------------------------------Authenticate otp Service call--------------------------------------


    private void otpServiceForAuthenticate() {
        if (DeviceManager.getInstance().getTokenId() != null) {
            mOtpRandomNum = OtpRandomNumberGenration(ApplicationConstants.length);
            otpDetails = new OtpDetails();
            otpDetails.setPhone(enterMobVrfid.getText().toString());
            otpDetails.setOtp(String.valueOf(mOtpRandomNum));
            mRandomNum.add(String.valueOf(mOtpRandomNum));
            mLicenseDetails = DeviceManager.getInstance().getTokenId();
            if (otpDetails.getPhone() != null && otpDetails.getOtp() != null) {
                sendOtpForAuthentication(mLicenseDetails, otpDetails);
            } else {
                AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.activity_mandatory_fields_missing));
            }
        } else {
            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.token_id));
        }
    }

    private void sendOtpForAuthentication(final LicenseDetails mLicenseDetails, OtpDetails otpDetails) {
        startBlockRefreshing();
        final NetworkService otpDetailsService = ServiceManager.getInstance().createService(NetworkService.class);
        Observable<OtpDetailsResponse> checkVisitorsObservable = otpDetailsService.otpServiceResponse(mLicenseDetails.getToken(), otpDetails);
        checkVisitorsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OtpDetailsResponse>() {
                    @Override
                    public void onCompleted() {
                        stopBlockRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopBlockRefreshing();
                        handleError(e);
                    }

                    @Override
                    public void onNext(OtpDetailsResponse otpDetailsResponse) {
                        if (otpDetailsResponse.getStatusCode().equalsIgnoreCase("200")) {
                            popsetOtpForAuthenticate(mLicenseDetails);
                        } else if (otpDetailsResponse.getStatusCode().equalsIgnoreCase("401")) {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), otpDetailsResponse.getMessage());
                        } else {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
                        }
                    }
                });
    }

    //----------------------------------new design vrf and mob number both popup-----------------------------------

    public void vrfMobnumberPopup() {
        final Dialog vrfmobDialog = new Dialog(DashBoardActivity.this, R.style.NewDialog);
        vrfmobDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        vrfmobDialog.setContentView(R.layout.phone_number_vrf);
        vrfmobDialog.setCanceledOnTouchOutside(false);
        final Button submitExistingVrfButton = (Button) vrfmobDialog.findViewById(R.id.submit_existing_vrf);
        enterMobVrfid = (EditText) vrfmobDialog.findViewById(R.id.enter_mob_vrfid);
        ImageView closePopup = (ImageView) vrfmobDialog.findViewById(R.id.close_popup);

        enterMobVrfid.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (enterMobVrfid.getText().length() != 0 && enterMobVrfid.getText() != null) {
                        authenticateRequest = new AuthenticateRequest();
                        mLicenseDetails = DeviceManager.getInstance().getTokenId();
                        if (AlertUtils.isValidPhoneNumber(enterMobVrfid.getText().toString())) {
                            authenticateRequest.setPhone(enterMobVrfid.getText().toString());
                            mRandomNum = new ArrayList<String>();
                            otpServiceForAuthenticate();
                            vrfmobDialog.dismiss();
                        } else if (AlertUtils.isAlphaNumeric((enterMobVrfid.getText().toString()))) {
                            authenticateRequest.setVrfsid(enterMobVrfid.getText().toString());
                            authenticateVisitors(mLicenseDetails, authenticateRequest);
                            vrfmobDialog.dismiss();
                        } else {
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.invalid_num));
                        }
                    } else {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name),getString(R.string.please_scan_qr));
                    }
                }

                return false;
            }
        });

        submitExistingVrfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterMobVrfid.getText().length() != 0 && enterMobVrfid.getText() != null) {
                    authenticateRequest = new AuthenticateRequest();
                    mLicenseDetails = DeviceManager.getInstance().getTokenId();
                    if (AlertUtils.isValidPhoneNumber(enterMobVrfid.getText().toString())) {
                        authenticateRequest.setPhone(enterMobVrfid.getText().toString());
                        mRandomNum = new ArrayList<String>();
                        otpServiceForAuthenticate();
                        vrfmobDialog.dismiss();
                    } else if (AlertUtils.isAlphaNumeric((enterMobVrfid.getText().toString()))) {
                        authenticateRequest.setVrfsid(enterMobVrfid.getText().toString());
                        authenticateVisitors(mLicenseDetails, authenticateRequest);
                        vrfmobDialog.dismiss();
                    } else {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.invalid_num));
                    }
                } else {
                    AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.please_scan_qr));
                }
            }
        });
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vrfmobDialog.dismiss();
            }
        });
        vrfmobDialog.show();
    }

    //TODO---------------create vrf popup------------------------
    private void registrationWalkinVrf() {
        final Dialog registrationDialog = new Dialog(DashBoardActivity.this, R.style.NewDialog);
        registrationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        registrationDialog.setContentView(R.layout.registration_walkin_popup);
        registrationDialog.setCanceledOnTouchOutside(false);
        final Button submitRegistationVrfButton = (Button) registrationDialog.findViewById(R.id.submit_registration_walkin_vrf);
        enterMobnumber = (EditText) registrationDialog.findViewById(R.id.enter_mob_number);
        ImageView closePopup = (ImageView) registrationDialog.findViewById(R.id.close_popup_registration);
//TODO-- keypad click lisnner---------------------
        enterMobnumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (validateforCreateVrf()) {
                        if (AlertUtils.isValidPhoneNumber(enterMobnumber.getText().toString())) {
                            mRandomNum = new ArrayList<String>();
                            otpService();
                            registrationDialog.dismiss();
                        } else {
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.invalid_num));
                        }
                    }
                }
                return false;
            }
        });
//TODO-- submit button for create vrf click lisnner---------------------
        submitRegistationVrfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateforCreateVrf()) {
                    if (AlertUtils.isValidPhoneNumber(enterMobnumber.getText().toString())) {
                        mRandomNum = new ArrayList<String>();
                        otpService();
                        registrationDialog.dismiss();
                    } else {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.invalid_num));
                    }
                }
            }
        });
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationDialog.dismiss();
            }
        });
        registrationDialog.show();
    }

    private boolean validateforCreateVrf() {
        boolean success = true;
        if (StringUtil.isEmpty(enterMobnumber.getText().toString())) {
            enterMobnumber.setError(getResources().getString(R.string.phone_mandatory));
            success = false;
        } else
            enterMobnumber.setError(null);
        return success;
    }

//-----------------------------TODO set password for settings--------------------------------

    public void popsetSettingPassword() {
        final Dialog securitySetDialog = new Dialog(DashBoardActivity.this, R.style.NewDialog);
        securitySetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        securitySetDialog.setContentView(R.layout.security_popup);
        securitySetDialog.setCanceledOnTouchOutside(false);
        TextView mTitleTextview = (TextView) securitySetDialog.findViewById(R.id.popup_title_textview);
        final Button setPasswordButton = (Button) securitySetDialog.findViewById(R.id.submit_button_for_security);
        mTitleTextview.setText(getResources().getString(R.string.set_pass));
        mPassword = (EditText) securitySetDialog.findViewById(R.id.mPassword);
        ImageView closepopup = (ImageView) securitySetDialog.findViewById(R.id.close_popup_security);
        final EditText mReEnterPassword = (EditText) securitySetDialog.findViewById(R.id.re_enter_password);
        setPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText() != null && mPassword.getText().length() >= 4 && mReEnterPassword.getText() != null && mReEnterPassword.getText().length() >= 4) {
                    if (mReEnterPassword.getText().toString().equalsIgnoreCase(mPassword.getText().toString())) {
                        DeviceManager.getInstance().saveSettingPassword(mPassword.getText().toString());
                        securitySetDialog.dismiss();
                    } else {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.pasword_mismatch));
                    }
                } else {
                    AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.set_min_password));
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mPassword.getWindowToken(), 0);
            }
        });
        closepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                securitySetDialog.dismiss();
            }
        });
        securitySetDialog.show();
    }


    private boolean validation() {
        boolean success = true;
        if (StringUtil.isEmpty(mPassword.getText().toString())) {
            mPassword.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        }
        return success;
    }

    public void popSettingPasswordActivated() {
        String mLiscencepassword;
        String numbers = null;
        final Dialog securityDialog = new Dialog(DashBoardActivity.this, R.style.NewDialog);
        securityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        securityDialog.setContentView(R.layout.security_popup);
        securityDialog.setCanceledOnTouchOutside(false);
        final Button otpButton = (Button) securityDialog.findViewById(R.id.submit_button_for_security);
        mPassword = (EditText) securityDialog.findViewById(R.id.mPassword);
        EditText mSubmitButtonforSecurity = (EditText) securityDialog.findViewById(R.id.re_enter_password);
        ImageView closepopup = (ImageView) securityDialog.findViewById(R.id.close_popup_security);
        mSubmitButtonforSecurity.setVisibility(View.GONE);

        if (DeviceManager.getInstance().getLicenseDetails() != null) {
            mLiscencepassword = DeviceManager.getInstance().getLicenseDetails();
            numbers = mLiscencepassword.substring(mLiscencepassword.length() - 6);
        }
        final String finalNumbers = numbers;
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (DeviceManager.getInstance().getPassword().equalsIgnoreCase(mPassword.getText().toString())) {
                        Intent intent = new Intent(DashBoardActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        securityDialog.dismiss();
                    } else if (mPassword.getText().toString().equalsIgnoreCase(finalNumbers)) {
                        Intent intent = new Intent(DashBoardActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        securityDialog.dismiss();
                    } else {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.enter_password_wrong));
                    }
                }
            }
        });
        closepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                securityDialog.dismiss();
            }
        });
        securityDialog.show();
    }


    //-------------------------------otp popup details-----------------------------------------------

    public void popsetOtp(final LicenseDetails licenseDetails) {

        final Dialog otpDialog = new Dialog(DashBoardActivity.this, R.style.NewDialog);
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.setContentView(R.layout.popup_otp_verification);
        otpDialog.setCanceledOnTouchOutside(false);
        final Button otpButton = (Button) otpDialog.findViewById(R.id.submit_button);
        ImageView closeOtpPopup = (ImageView) otpDialog.findViewById(R.id.close_popup_otp);
        resendButton = (Button) otpDialog.findViewById(R.id.resend_button);
        mOtpNotRecivedRegistration = (Button) otpDialog.findViewById(R.id.otp_not_recived_button);
        popupOtpNotRecivedVisiblity();
        firstEdittext = (EditText) otpDialog.findViewById(R.id.editTextone);
        twoEdittext = (EditText) otpDialog.findViewById(R.id.editTexttwo);
        threeEdittext = (EditText) otpDialog.findViewById(R.id.editTextthree);
        fourEdittext = (EditText) otpDialog.findViewById(R.id.editTextfour);
        fiveEdittext = (EditText) otpDialog.findViewById(R.id.editTextfive);
        sixEdittext = (EditText) otpDialog.findViewById(R.id.editTextsix);


//TODO:---------editetext done create vrf otp click lisnner----------------

        sixEdittext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (validationOtp()) {
                        mOtpValue = firstEdittext.getText().toString() + twoEdittext.getText().toString() + threeEdittext.getText().toString() +
                                fourEdittext.getText().toString() + fiveEdittext.getText().toString() + sixEdittext.getText().toString();
                        String oTtp = mOtpValue;
                        if (mRandomNum.contains(oTtp)) {
                            mRandomNum = new ArrayList<String>();
                            mVisitorDetails.setPhone(enterMobnumber.getText().toString());
                            checkVisitors(licenseDetails, mVisitorDetails);
                            otpDialog.dismiss();
                        } else {
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.otp_mismatch));
                        }
                    } else {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.enter_otp));
                    }
                }
                return false;
            }
        });
        TextView title = (TextView) otpDialog.findViewById(R.id.popup_title_textview);

        title.setText(getString(R.string.enter_otp_details) + otpDetails.getPhone());

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showYesNoDialog(DashBoardActivity.this, getString(R.string.resend_otp), getString(R.string.are_you_sure_you_want_to_resend_otp),
                        getString(R.string.yes), getString(R.string.no), new IAlertDialogCallback() {
                            @Override
                            public void positiveButtonClicked(DialogInterface dialog, int which) {
                                registerotpCount++;
                                mOtpRandomNum = OtpRandomNumberGenration(ApplicationConstants.length);
                                mRandomNum.add(String.valueOf(mOtpRandomNum));
                                otpDetails = new OtpDetails();
                                otpDetails.setPhone(enterMobnumber.getText().toString());
                                otpDetails.setOtp(String.valueOf(mOtpRandomNum));
                                sendOtp(mLicenseDetails, otpDetails);
                                popupOtpNotRecivedVisiblity();
                                dialog.dismiss();
                                otpDialog.dismiss();
                            }

                            @Override
                            public void cancelButtonClicked(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                            @Override
                            public void negativeButtonClicked(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        });
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationOtp()) {
                    mOtpValue = firstEdittext.getText().toString() + twoEdittext.getText().toString() + threeEdittext.getText().toString() +
                            fourEdittext.getText().toString() + fiveEdittext.getText().toString() + sixEdittext.getText().toString();
                    String oTtp = mOtpValue;
                    if (mRandomNum.contains(oTtp)) {
                        mRandomNum = new ArrayList<String>();
                        mVisitorDetails.setPhone(enterMobnumber.getText().toString());
                        checkVisitors(licenseDetails, mVisitorDetails);
                        otpDialog.dismiss();
                    } else {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.otp_mismatch));
                    }
                } else {
                    AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.enter_otp));
                }
            }
        });
        closeOtpPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showYesNoDialog(DashBoardActivity.this, getString(R.string.resend_otp), getString(R.string.are_you_sure_you_want_to_cancel),
                        getString(R.string.yes), getString(R.string.no), new IAlertDialogCallback() {
                            @Override
                            public void positiveButtonClicked(DialogInterface dialog, int which) {
                                registerotpCount = 0;
                                otpDialog.dismiss();
                                dialog.dismiss();
                            }

                            @Override
                            public void cancelButtonClicked(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                            @Override
                            public void negativeButtonClicked(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
            }
        });

        mOtpNotRecivedRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showYesNoDialog(DashBoardActivity.this, getString(R.string.resend_otp), getString(R.string.are_you_sure_you_want_to_continue_without_otp),
                        getString(R.string.yes), getString(R.string.no), new IAlertDialogCallback() {
                            @Override
                            public void positiveButtonClicked(DialogInterface dialog, int which) {
                                if (validateforCreateVrf()) {
                                    if (AlertUtils.isValidPhoneNumber(enterMobnumber.getText().toString())) {
                                        mLicenseDetails = DeviceManager.getInstance().getTokenId();
                                        mVisitorDetails.setPhone(enterMobnumber.getText().toString());
                                        checkVisitors(mLicenseDetails, mVisitorDetails);
                                        otpDialog.dismiss();
                                    } else {
                                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.invalid_num));
                                    }
                                } else {
                                    otpDialog.dismiss();
                                }
                            }

                            @Override
                            public void cancelButtonClicked(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                            @Override
                            public void negativeButtonClicked(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
            }
        });
        otpDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  popupOtpNotRecivedVisiblity();


    }

    private void popupOtpNotRecivedVisiblity() {
        if (registerotpCount >= 2) {
            resendButton.setVisibility(View.GONE);
            mOtpNotRecivedRegistration.setVisibility(View.VISIBLE);
        } else {
            resendButton.setVisibility(View.VISIBLE);
            mOtpNotRecivedRegistration.setVisibility(View.GONE);
        }
    }

    //----------------------------------------otp in authentication side-----------------------------

    public void popsetOtpForAuthenticate(final LicenseDetails licenseDetails) {
        final Dialog otpAuthenticationDialog = new Dialog(DashBoardActivity.this, R.style.NewDialog);
        otpAuthenticationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpAuthenticationDialog.setContentView(R.layout.popup_otp_verification);
        otpAuthenticationDialog.setCanceledOnTouchOutside(false);
        final Button otpButton = (Button) otpAuthenticationDialog.findViewById(R.id.submit_button);
        ImageView closeOtpAuthenticatePopup = (ImageView) otpAuthenticationDialog.findViewById(R.id.close_popup_otp);
        Button mOtpNotRecivedButton = (Button) otpAuthenticationDialog.findViewById(R.id.otp_not_recived_button);
        mOtpNotRecivedButton.setVisibility(View.GONE);

        TextView title = (TextView) otpAuthenticationDialog.findViewById(R.id.popup_title_textview);
        resendAuthenticationButton = (Button) otpAuthenticationDialog.findViewById(R.id.resend_button);
        authenticateResendVisiblity();
        firstEdittext = (EditText) otpAuthenticationDialog.findViewById(R.id.editTextone);
        twoEdittext = (EditText) otpAuthenticationDialog.findViewById(R.id.editTexttwo);
        threeEdittext = (EditText) otpAuthenticationDialog.findViewById(R.id.editTextthree);
        fourEdittext = (EditText) otpAuthenticationDialog.findViewById(R.id.editTextfour);
        fiveEdittext = (EditText) otpAuthenticationDialog.findViewById(R.id.editTextfive);
        sixEdittext = (EditText) otpAuthenticationDialog.findViewById(R.id.editTextsix);

        sixEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if (validationOtp()) {
                        mOtpValueforAuthenticate = firstEdittext.getText().toString() + twoEdittext.getText().toString() + threeEdittext.getText().toString() +
                                fourEdittext.getText().toString() + fiveEdittext.getText().toString() + sixEdittext.getText().toString();
                        String oTtp = mOtpValueforAuthenticate;
                        if (mRandomNum.contains(oTtp)) {
                            mRandomNum = new ArrayList<String>();
                            authenticateRequest = new AuthenticateRequest();
                            authenticateRequest.setPhone(enterMobVrfid.getText().toString());
                            authenticateVisitors(licenseDetails, authenticateRequest);
                            otpAuthenticationDialog.dismiss();
                        } else {
                            AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.otp_mismatch));
                        }
                    } else {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
                    }
                }

                return false;
            }
        });
        title.setText(getString(R.string.enter_otp_details) + otpDetails.getPhone());
        resendAuthenticationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showYesNoDialog(DashBoardActivity.this, getString(R.string.resend_otp), getString(R.string.are_you_sure_you_want_to_resend_otp),
                        getString(R.string.yes), getString(R.string.no), new IAlertDialogCallback() {
                            @Override
                            public void positiveButtonClicked(DialogInterface dialog, int which) {
                                authenticateotpCount++;
                                mOtpRandomNum = OtpRandomNumberGenration(ApplicationConstants.length);
                                mRandomNum.add(String.valueOf(mOtpRandomNum));
                                otpDetails = new OtpDetails();
                                otpDetails.setPhone(enterMobVrfid.getText().toString());
                                otpDetails.setOtp(String.valueOf(mOtpRandomNum));
                                sendOtpForAuthentication(mLicenseDetails, otpDetails);
                                authenticateResendVisiblity();
                                dialog.dismiss();
                                otpAuthenticationDialog.dismiss();
                            }

                            @Override
                            public void cancelButtonClicked(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                            @Override
                            public void negativeButtonClicked(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
        });
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationOtp()) {
                    mOtpValueforAuthenticate = firstEdittext.getText().toString() + twoEdittext.getText().toString() + threeEdittext.getText().toString() +
                            fourEdittext.getText().toString() + fiveEdittext.getText().toString() + sixEdittext.getText().toString();
                    String oTtp = mOtpValueforAuthenticate;
                    if (mRandomNum.contains(oTtp)) {
                        mRandomNum = new ArrayList<String>();
                        authenticateRequest = new AuthenticateRequest();
                        authenticateRequest.setPhone(enterMobVrfid.getText().toString());
                        authenticateVisitors(licenseDetails, authenticateRequest);
                        otpAuthenticationDialog.dismiss();
                    } else {
                        AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.otp_mismatch));
                    }
                } else {
                    AlertUtils.showInfoDialog(DashBoardActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
                }
            }
        });
        closeOtpAuthenticatePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showYesNoDialog(DashBoardActivity.this, getString(R.string.resend_otp), getString(R.string.are_you_sure_you_want_to_cancel),
                        getString(R.string.yes), getString(R.string.no), new IAlertDialogCallback() {
                            @Override
                            public void positiveButtonClicked(DialogInterface dialog, int which) {
                                authenticateotpCount = 0;
                                otpAuthenticationDialog.dismiss();
                                dialog.dismiss();
                            }

                            @Override
                            public void cancelButtonClicked(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                            @Override
                            public void negativeButtonClicked(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
            }
        });
        otpAuthenticationDialog.show();
    }

    private void authenticateResendVisiblity() {
        if (authenticateotpCount >= 2) {
            resendAuthenticationButton.setVisibility(View.GONE);
        } else {
            resendAuthenticationButton.setVisibility(View.VISIBLE);
        }
    }




}
