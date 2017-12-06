package com.telloquent.vms.activities.home;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.gson.Gson;
import com.telloquent.vms.R;
import com.telloquent.vms.base.BaseActivity;
import com.telloquent.vms.base.BaseDisplayActivity;
import com.telloquent.vms.model.additionalvisitors.AdditionalVrfResponse;
import com.telloquent.vms.model.authenticate.AuthenticateResponse;
import com.telloquent.vms.model.createvrf.CreateVrfDetailsResponse;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.servicemanager.NetworkService;
import com.telloquent.vms.servicemanager.ServiceManager;
import com.telloquent.vms.support.SettingsDB;
import com.telloquent.vms.utils.AlertUtils;
import com.telloquent.vms.utils.ApplicationConstants;
import com.telloquent.vms.utils.DeviceManager;
import com.telloquent.vms.utils.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SuppressWarnings("deprecation")
public class AdditionalVisitorActivity extends BaseDisplayActivity implements View.OnClickListener {
    private Button mVisitorScanButton;
    private Button mAdditionalVisitorResetButton, mAddSubmitVrfButton;
    private Bitmap bitmapFrontCam;
    private ImageView mAdditionalScanVisitorsImage;
    private Dialog mImageDialog;
    private ImageView mCameraPreviewImage;
    private EditText addVisitorsEmailId, mAddMobileNumber, addVisitorName, mVisitorCompanyName, mVisitorArea,
            mVisitorCity, mVisitorState, mVisiorCountry, mVisitorPinCode;
    private SettingsDB dbSettingsHelper;
    private SettingDetailsResponse mSettingDetails;
    private int mSecondaryCompanyname, mSecondaryCompanyAddress, mAllowCaptureImage, mSecurityImageOverride;
    private CreateVrfDetailsResponse createVrfDetailsResponse;
    private AuthenticateResponse authenticateResponse;
    private Uri mImageUri;
    private LicenseDetails mLicenseDetails;
    private LinearLayout mAdditionalVisitorsImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_visitor);
    }

    @Override
    protected void initializeView() {
        setupActionBar(getResources().getString(R.string.vms_screen), ActionBarActivityLeftAction.ACTION_NONE, BaseActivity.ActionBarActivityRightAction.ACTION_CLOSE, BaseActivity.ActionBarActivityRight2Action.ACTION_NONE);
        shouldDiscardChanges = true;
        mVisitorScanButton = (Button) findViewById(R.id.additional_visitor_scan_button);
        mAdditionalVisitorResetButton = (Button) findViewById(R.id.add_visitors_reset_vrf_form);
        mAdditionalScanVisitorsImage = (ImageView) findViewById(R.id.scan_id_additional_visitor);
        mAddSubmitVrfButton = (Button) findViewById(R.id.add_visitors_submit_vrf_button);
        addVisitorsEmailId = (EditText) findViewById(R.id.additional_visitors_email_id);
        mAddMobileNumber = (EditText) findViewById(R.id.mobile_number_for_additional);
        addVisitorName = (EditText) findViewById(R.id.additional_visitor_name);
        mVisitorCompanyName = (EditText) findViewById(R.id.add_visitor_company_name);
        mVisitorArea = (EditText) findViewById(R.id.add_visitor_area);
        mVisitorCity = (EditText) findViewById(R.id.add_visitor_city);
        mVisitorState = (EditText) findViewById(R.id.add_visitor_state);
        mVisiorCountry = (EditText) findViewById(R.id.add_vitior_country);
        mVisitorPinCode = (EditText) findViewById(R.id.add_visitor_pin_code);
        mAdditionalVisitorsImageLayout = (LinearLayout) findViewById(R.id.additional_visitors_image_layouts);
        mAdditionalVisitorResetButton.setOnClickListener(this);
        mAddSubmitVrfButton.setOnClickListener(this);
        mVisitorScanButton.setOnClickListener(this);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            Gson gson = new Gson();
            createVrfDetailsResponse = gson.fromJson(bundle.getString(ApplicationConstants.kCreateVrfDetailsResponse), CreateVrfDetailsResponse.class);
            authenticateResponse = gson.fromJson(bundle.getString(ApplicationConstants.kAuthenticateResponse), AuthenticateResponse.class);
        }
        getData();
        setValueuesAutopopulate();
    }

    private void setValueuesAutopopulate() {
        try {
            if (createVrfDetailsResponse != null) {
                if (createVrfDetailsResponse.getRequest().getCompany() != null) {
                    mVisitorCompanyName.setText(createVrfDetailsResponse.getRequest().getCompany());
                }
                if (createVrfDetailsResponse.getRequest().getLocality() != null) {
                    mVisitorArea.setText(createVrfDetailsResponse.getRequest().getLocality());
                }
                if (createVrfDetailsResponse.getRequest().getCity() != null) {
                    mVisitorCity.setText(createVrfDetailsResponse.getRequest().getCity());
                }
                if (createVrfDetailsResponse.getRequest().getState() != null) {
                    mVisitorState.setText(createVrfDetailsResponse.getRequest().getState());
                }
                if (createVrfDetailsResponse.getRequest().getCountry() != null) {
                    mVisiorCountry.setText(createVrfDetailsResponse.getRequest().getCountry());
                }
                if (createVrfDetailsResponse.getRequest().getPinCode() != null) {
                    mVisitorPinCode.setText(createVrfDetailsResponse.getRequest().getPinCode());
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (authenticateResponse != null) {
            if (authenticateResponse.getCompanyName() != null) {
                mVisitorCompanyName.setText(authenticateResponse.getCompanyName());
            }
            if (authenticateResponse.getLocality() != null) {
                mVisitorArea.setText(authenticateResponse.getLocality());
            }
            if (authenticateResponse.getCity() != null) {
                mVisitorCity.setText(authenticateResponse.getCity());
            }
            if (authenticateResponse.getState() != null) {
                mVisitorState.setText(authenticateResponse.getState());
            }
            if (authenticateResponse.getCountry() != null) {
                mVisiorCountry.setText(authenticateResponse.getCountry());
            }
            if (authenticateResponse.getPinCode() != null) {
                mVisitorPinCode.setText(authenticateResponse.getPinCode());
            }
        }
        if (mAllowCaptureImage == 0) {
            mAdditionalVisitorsImageLayout.setVisibility(View.GONE);
        } else {
            mAdditionalVisitorsImageLayout.setVisibility(View.VISIBLE);
        }
    }

    private void mFromResetValue() {
        addVisitorsEmailId.getText().clear();
        mAddMobileNumber.getText().clear();
        addVisitorName.getText().clear();
        mVisitorCompanyName.getText().clear();
        mVisitorArea.getText().clear();
        mVisitorCity.getText().clear();
        mVisitorState.getText().clear();
        mVisiorCountry.getText().clear();
        mVisitorPinCode.getText().clear();
    }

    @Override
    public void getData() {
        dbSettingsHelper = new SettingsDB(this);
        mSettingDetails = new SettingDetailsResponse();
        mSettingDetails = dbSettingsHelper.getSettingDetails();
        if (mSettingDetails.getSettings().getRequireSecondaryCompany() != null) {
            mSecondaryCompanyname = mSettingDetails.getSettings().getRequireSecondaryCompany();
        }
        if (mSettingDetails.getSettings().getRequireSecondaryCompany() != null) {
            mSecondaryCompanyAddress = mSettingDetails.getSettings().getRequireSecondaryCompanyAddress();
        }
        if (mSettingDetails.getSettings().getAllowCaptureImage() != null) {
            mAllowCaptureImage = mSettingDetails.getSettings().getAllowCaptureImage();
        }
        if (mSettingDetails.getSettings().getSecurityImageOverride() != null) {
            mSecurityImageOverride = mSettingDetails.getSettings().getSecurityImageOverride();
        }
    }

    private boolean validation() {
        boolean success = true;
        if (mAddMobileNumber.getText().toString() != null && mAddMobileNumber.getText().toString().length() != 0) {
            if (StringUtil.isEmpty(mAddMobileNumber.getText().toString())) {
                mAddMobileNumber.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                success = false;
            } else
                mAddMobileNumber.setError(null);
        } else {
            if (StringUtil.isEmpty(addVisitorsEmailId.getText().toString())) {
                addVisitorsEmailId.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                success = false;
            } else
                addVisitorsEmailId.setError(null);
        }
        if (StringUtil.isEmpty(addVisitorName.getText().toString())) {
            addVisitorName.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        } else
            addVisitorName.setError(null);

        if (mSecondaryCompanyname == 1) {
            if (StringUtil.isEmpty(mVisitorCompanyName.getText().toString())) {
                mVisitorCompanyName.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                success = false;
            } else
                mVisitorCompanyName.setError(null);
        }
        if (mSecondaryCompanyAddress == 1) {
            if (createVrfDetailsResponse != null) {
                if (createVrfDetailsResponse.getRequest().getVrfType().equalsIgnoreCase("Official")) {

                    if (StringUtil.isEmpty(mVisitorArea.getText().toString())) {
                        mVisitorArea.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                        success = false;
                    } else
                        mVisitorArea.setError(null);
                    if (StringUtil.isEmpty(mVisitorCity.getText().toString())) {
                        mVisitorCity.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                        success = false;
                    } else
                        mVisitorCity.setError(null);
                    if (StringUtil.isEmpty(mVisitorState.getText().toString())) {
                        mVisitorState.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                        success = false;
                    } else
                        mVisitorState.setError(null);
                    if (StringUtil.isEmpty(mVisiorCountry.getText().toString())) {
                        mVisiorCountry.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                        success = false;
                    } else
                        mVisiorCountry.setError(null);
                    if (StringUtil.isEmpty(mVisitorPinCode.getText().toString())) {
                        mVisitorPinCode.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                        success = false;
                    } else
                        mVisitorPinCode.setError(null);
                }
            }
            if (authenticateResponse != null) {
                try {
                    if (authenticateResponse.getVrfType().equalsIgnoreCase("Official")) {
                        if (StringUtil.isEmpty(mVisitorArea.getText().toString())) {
                            mVisitorArea.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                            success = false;
                        } else
                            mVisitorArea.setError(null);
                        if (StringUtil.isEmpty(mVisitorCity.getText().toString())) {
                            mVisitorCity.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                            success = false;
                        } else
                            mVisitorCity.setError(null);
                        if (StringUtil.isEmpty(mVisitorState.getText().toString())) {
                            mVisitorState.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                            success = false;
                        } else
                            mVisitorState.setError(null);
                        if (StringUtil.isEmpty(mVisiorCountry.getText().toString())) {
                            mVisiorCountry.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                            success = false;
                        } else
                            mVisiorCountry.setError(null);
                        if (StringUtil.isEmpty(mVisitorPinCode.getText().toString())) {
                            mVisitorPinCode.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                            success = false;
                        } else
                            mVisitorPinCode.setError(null);
                    }
                } catch (Exception e) {
                    AlertUtils.showInfoDialog(AdditionalVisitorActivity.this, getString(R.string.app_name), getString(R.string.please_check));
                }
            }
        }
        return success;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.additional_visitor_scan_button:
                openCamera();
                break;
            case R.id.add_visitors_reset_vrf_form:
                mFromResetValue();
                break;
            case R.id.add_visitors_submit_vrf_button:
                if (validation()) {
                    if (DeviceManager.getInstance().getTokenId() != null) {
                        mLicenseDetails = DeviceManager.getInstance().getTokenId();
                        if (mAllowCaptureImage == 1) {
                            if (mSecurityImageOverride == 0) {
                                if (mAdditionalScanVisitorsImage.getDrawable() != null) {
                                    additionalVisitorsDetails(mLicenseDetails);
                                } else {
                                    AlertUtils.showInfoDialog(AdditionalVisitorActivity.this, getString(R.string.app_name), getString(R.string.upload_pic));
                                }
                            } else {
                                additionalVisitorsDetails(mLicenseDetails);
                            }
                        } else {
                            additionalVisitorsDetails(mLicenseDetails);
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ApplicationConstants.OPEN_CAMERA) {
                if (data == null) {
                    AlertUtils.showInfoDialog(AdditionalVisitorActivity.this, getString(R.string.app_name), getString(R.string.upload_pic_err));
                } else {
                    updateDocumentPic(data);
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            AlertUtils.showInfoDialog(AdditionalVisitorActivity.this, getString(R.string.app_name), getString(R.string.pic));
        }
    }

    private void updateDocumentPic(Intent data) {
        Bundle extras = data.getExtras();
        bitmapFrontCam = (Bitmap) extras.get("data");
        mAdditionalScanVisitorsImage.setImageBitmap(bitmapFrontCam);
        mImageUri = getImageUri(getApplicationContext(), bitmapFrontCam);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(
                    "android.intent.extras.CAMERA_FACING",
                    Camera.CameraInfo.CAMERA_FACING_FRONT);
            startActivityForResult(takePictureIntent, ApplicationConstants.OPEN_CAMERA);
        }
    }

    public static Intent createIntentWithBundle(Context context, @NonNull CreateVrfDetailsResponse createVrfDetailsResponse, @NonNull AuthenticateResponse authenticateResponse) {
        Intent intent = new Intent(context, AdditionalVisitorActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String myJsonResp = gson.toJson(createVrfDetailsResponse);
        String myJsonAuthen = gson.toJson(authenticateResponse);
        bundle.putString(ApplicationConstants.kCreateVrfDetailsResponse, myJsonResp);
        bundle.putString(ApplicationConstants.kAuthenticateResponse, myJsonAuthen);
        intent.putExtras(bundle);
        return intent;
    }


//------------------------------------Additional visitors details service call----------------------------------------------


    private void additionalVisitorsDetails(final LicenseDetails licenseDetails) {
        startBlockRefreshing();
        File file = null;
        MultipartBody.Part mVrfid = null;
        MultipartBody.Part mScanfileBody = null;
        RequestBody mFileForViewAdditional = null;
        MultipartBody.Part mAddEmailId = null;
        MultipartBody.Part mPhone = null;
        MultipartBody.Part mVisitorName = null;
        MultipartBody.Part mCompanyName = null;
        MultipartBody.Part mLocality = null;
        MultipartBody.Part mCity = null;
        MultipartBody.Part mAddState = null;
        MultipartBody.Part mCountry = null;
        MultipartBody.Part mPinCode = null;
        if (mImageUri != null) {
            file = new File(getRealPathFromURI(mImageUri));
        }
        if (file != null) {
            mFileForViewAdditional = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        if (mFileForViewAdditional != null) {
            mScanfileBody = MultipartBody.Part.createFormData(ApplicationConstants.kScanfile, file.getName(), mFileForViewAdditional);
        }

        if (createVrfDetailsResponse != null) {
            mVrfid = MultipartBody.Part.createFormData(ApplicationConstants.kVrfid, createVrfDetailsResponse.getVrfid());
        } else if (authenticateResponse != null) {
            mVrfid = MultipartBody.Part.createFormData(ApplicationConstants.kVrfid, authenticateResponse.getVrfid());
        }

        if (addVisitorsEmailId.getText() != null) {
            mAddEmailId = MultipartBody.Part.createFormData(ApplicationConstants.kEmail, addVisitorsEmailId.getText().toString());
        }
        if (mAddMobileNumber.getText() != null) {
            mPhone = MultipartBody.Part.createFormData(ApplicationConstants.kPhone, mAddMobileNumber.getText().toString());
        }

        if (addVisitorName.getText() != null) {
            mVisitorName = MultipartBody.Part.createFormData(ApplicationConstants.kName, addVisitorName.getText().toString());
        }
        if (mVisitorCompanyName.getText() != null) {
            mCompanyName = MultipartBody.Part.createFormData(ApplicationConstants.kCompanyName, mVisitorCompanyName.getText().toString());
        }
        if (mVisitorArea.getText() != null) {
            mLocality = MultipartBody.Part.createFormData(ApplicationConstants.kLocality, mVisitorArea.getText().toString());
        }

        if (mVisitorCity.getText() != null) {
            mCity = MultipartBody.Part.createFormData(ApplicationConstants.kCity, mVisitorCity.getText().toString());
        }

        if (mVisiorCountry.getText() != null) {
            mCountry = MultipartBody.Part.createFormData(ApplicationConstants.kCountry, mVisiorCountry.getText().toString());
        }

        if (mVisitorState.getText() != null) {
            mAddState = MultipartBody.Part.createFormData(ApplicationConstants.kState, mVisitorState.getText().toString());
        }

        if (mVisitorPinCode.getText() != null) {
            mPinCode = MultipartBody.Part.createFormData(ApplicationConstants.kPinCode, mVisitorPinCode.getText().toString());
        }
        final NetworkService networkService = ServiceManager.getInstance().createService(NetworkService.class);
        final Observable<AdditionalVrfResponse> additionalVrfResponseObservable = networkService.additionalVisitorsDetail(licenseDetails.getToken(), mVrfid, mAddEmailId, mPhone, mVisitorName, mCompanyName, mLocality, mCity, mAddState, mCountry, mPinCode, mScanfileBody);
        additionalVrfResponseObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AdditionalVrfResponse>() {
                    @Override
                    public void onCompleted() {
                        stopBlockRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopBlockRefreshing();
                        //   AlertUtils.showInfoDialog(AdditionalVisitorActivity.this, getString(R.string.app_name), e.getMessage());
                        handleError(e);
                    }

                    @Override
                    public void onNext(AdditionalVrfResponse additionalVrfResponse) {
                        if (additionalVrfResponse.getStatusCode().equalsIgnoreCase("200")) {
                            Intent intent = PrimaryVisitorDetailActivity.createIntentWithBundle(AdditionalVisitorActivity.this, createVrfDetailsResponse, authenticateResponse);
                            startActivity(intent);
                            finish();
                        } else if (additionalVrfResponse.getStatusCode().equalsIgnoreCase("500")) {
                            AlertUtils.showInfoDialog(AdditionalVisitorActivity.this, getString(R.string.app_name), additionalVrfResponse.getMessage());
                            stopBlockRefreshing();
                        } else {
                            AlertUtils.showInfoDialog(AdditionalVisitorActivity.this, getString(R.string.app_name), additionalVrfResponse.getMessage());
                            stopBlockRefreshing();
                        }
                    }
                });
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // inImage.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "mTitle", null);
        return Uri.parse(path);
    }

}
