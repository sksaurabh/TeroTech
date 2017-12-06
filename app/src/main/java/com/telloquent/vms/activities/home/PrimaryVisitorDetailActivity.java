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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.telloquent.vms.R;
import com.telloquent.vms.activities.visitor.VisitorListActivity;
import com.telloquent.vms.base.BaseActivity;
import com.telloquent.vms.base.BaseDisplayActivity;
import com.telloquent.vms.model.additionalvisitors.AdditionalVrfResponse;
import com.telloquent.vms.model.authenticate.AuthenticateResponse;
import com.telloquent.vms.model.createvrf.CreateVrfDetails;
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
public class PrimaryVisitorDetailActivity extends BaseDisplayActivity implements View.OnClickListener {
    private ImageView mScanIdforviewVrfImage, mCameraPreviewImage;
    private static Bitmap bitmapFrontCam;
    private Button mScanViewVrfbutton, mVisitiorDetails;

    private CreateVrfDetails mCreateVrfDetails;
    private TextView mVisitorId, mViewCompanyName, mViewLocation, mViewDepartment, mViewName, mViewDataTime, viewToMeet, mViewPurpose;
    private CreateVrfDetailsResponse createVrfDetailsResponse;
    private AuthenticateResponse authenticateResponse;
    private LinearLayout mScanIdNotAvailableLinearLayout;
    private Button mAddVisitors, mSubmitVisitorView;
    private Dialog addAreaDialog, cameraPreviewDialog, mSecondaryDialog;
    private LinearLayout mAdditionalSecondaryVisitorsLayout;
    private Uri mImageUri;
    private SettingsDB dbSettingsHelper;
    private SettingDetailsResponse mSettingDetails;
    private int mSecondaryVisitorsDetails, mAllowCaptureImage, mAllowSecurityImageOverride;
    private EditText mAdditionalSecondaryVisitors;
    private TextView mNumberOfVisitors, mEmailId, mPhoneNumber;
    private Boolean isBoolean = true;
    private LinearLayout mToMeetLinearlayout, mVisitorImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_primary_visitor);
    }

    @Override
    protected void initializeView() {
        setupActionBar(getResources().getString(R.string.vms_screen), BaseActivity.ActionBarActivityLeftAction.ACTION_BACK, BaseActivity.ActionBarActivityRightAction.ACTION_NONE, BaseActivity.ActionBarActivityRight2Action.ACTION_NONE);
        shouldDiscardChanges = true;

        mScanIdforviewVrfImage = (ImageView) findViewById(R.id.scan_id_for_view);
        mScanViewVrfbutton = (Button) findViewById(R.id.scan_button_for_view_vrf);
        mVisitiorDetails = (Button) findViewById(R.id.view_visitior_details);
        mVisitorId = (TextView) findViewById(R.id.view_visitorId);
        mViewCompanyName = (TextView) findViewById(R.id.view_company_name);
        mViewLocation = (TextView) findViewById(R.id.view_location);
        mViewDepartment = (TextView) findViewById(R.id.view_department);
        mViewName = (TextView) findViewById(R.id.view_name);
        mViewDataTime = (TextView) findViewById(R.id.view_data_time);
        viewToMeet = (TextView) findViewById(R.id.view_to_meet);
        mAddVisitors = (Button) findViewById(R.id.add_visitior_details);
        mViewPurpose = (TextView) findViewById(R.id.view_purpose);
        mSubmitVisitorView = (Button) findViewById(R.id.submit_for_view_vrf_button);
        mAdditionalSecondaryVisitorsLayout = (LinearLayout) findViewById(R.id.additional_secondary_visitors);
        mNumberOfVisitors = (TextView) findViewById(R.id.number_of_visitors);
        mScanIdNotAvailableLinearLayout = (LinearLayout) findViewById(R.id.mScanId_not_available_linear_layout);
        mEmailId = (TextView) findViewById(R.id.view_email);
        mPhoneNumber = (TextView) findViewById(R.id.view_phone_number);
        mToMeetLinearlayout = (LinearLayout) findViewById(R.id.to_meet_linearlayout);
        mVisitorImageLayout = (LinearLayout) findViewById(R.id.visitor_image_layout);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            Gson gson = new Gson();
            createVrfDetailsResponse = gson.fromJson(bundle.getString(ApplicationConstants.kCreateVrfDetailsResponse), CreateVrfDetailsResponse.class);
            authenticateResponse = gson.fromJson(bundle.getString(ApplicationConstants.kAuthenticateResponse), AuthenticateResponse.class);
        }
        getData();
    }

    @Override
    public void getData() {
        mScanViewVrfbutton.setOnClickListener(this);
        mVisitiorDetails.setOnClickListener(this);
        mAddVisitors.setOnClickListener(this);
        mSubmitVisitorView.setOnClickListener(this);
        dbSettingsHelper = new SettingsDB(this);
        mSettingDetails = new SettingDetailsResponse();
        mSettingDetails = dbSettingsHelper.getSettingDetails();
        getSettingValuesFromDatabase();


        //------------------TODO-------------------Start create Vrf pojo-------------------------------------

        if (createVrfDetailsResponse != null) {

            if (createVrfDetailsResponse.getRequest().getCompany() != null) {
                mViewCompanyName.setText(createVrfDetailsResponse.getRequest().getCompany());
            }
            if (createVrfDetailsResponse.getRequest().getLocality() != null) {
                mViewLocation.setText(createVrfDetailsResponse.getRequest().getLocality());
            }
            if (createVrfDetailsResponse.getRequest().getName() != null) {
                mViewName.setText(createVrfDetailsResponse.getRequest().getName());
            }
            if (createVrfDetailsResponse.getRequest().getDepartment() != null) {
                mViewDepartment.setText(createVrfDetailsResponse.getRequest().getDepartment());
            }
            if (createVrfDetailsResponse.getRequest().getPurpose() != null) {
                mViewPurpose.setText(createVrfDetailsResponse.getRequest().getPurpose());
            }

            if (createVrfDetailsResponse.getRequest().getPhone() != null) {
                mPhoneNumber.setText(createVrfDetailsResponse.getRequest().getPhone());
            }
            if (createVrfDetailsResponse.getRequest().getEmail() != null) {
                mEmailId.setText(createVrfDetailsResponse.getRequest().getEmail());
            }

            if (createVrfDetailsResponse.getToMeet() != null) {
                mToMeetLinearlayout.setVisibility(View.VISIBLE);
                viewToMeet.setText(createVrfDetailsResponse.getToMeet().toString());
            } else {
                mToMeetLinearlayout.setVisibility(View.GONE);
            }
            if (createVrfDetailsResponse.getVrfid() != null) {
                mVisitorId.setText(createVrfDetailsResponse.getVrfid());
            }
            if (createVrfDetailsResponse.getEntryTime() != null) {
                mViewDataTime.setText(createVrfDetailsResponse.getEntryTime());
            }
//------TODO----------------here buttons--------------based on primary----------

            if (createVrfDetailsResponse.getIsPrimary() == 1) {
                if (mSecondaryVisitorsDetails == 1) {
                    mVisitiorDetails.setVisibility(View.VISIBLE);
                    mAddVisitors.setVisibility(View.VISIBLE);
                } else if (mSecondaryVisitorsDetails == 0) {
                    mAddVisitors.setVisibility(View.VISIBLE);
                    mVisitiorDetails.setVisibility(View.GONE);
                }
            } else {
                mVisitiorDetails.setVisibility(View.GONE);
                mAddVisitors.setVisibility(View.GONE);
            }
            if (createVrfDetailsResponse.getIdProofPath() == 0) {
                if (mAllowCaptureImage == 1) {
                    mVisitorImageLayout.setVisibility(View.VISIBLE);
                    mScanViewVrfbutton.setVisibility(View.VISIBLE);
                } else {
                    mVisitorImageLayout.setVisibility(View.GONE);
                    mScanViewVrfbutton.setVisibility(View.GONE);
                }
            } else {
                mVisitorImageLayout.setVisibility(View.GONE);
                mScanViewVrfbutton.setVisibility(View.GONE);
            }
        }

        //------------------TODO-------------------up to here create Vrf pojo- and Start Authenticate pojo------------------------------------

        if (authenticateResponse != null) {

            if (authenticateResponse.getCompanyName() != null) {
                mViewCompanyName.setText(authenticateResponse.getCompanyName());
            }
            if (authenticateResponse.getLocality() != null) {
                mViewLocation.setText(authenticateResponse.getLocality());
            }
            if (authenticateResponse.getName() != null) {
                mViewName.setText(authenticateResponse.getName());
            }
            if (authenticateResponse.getDepartment() != null) {
                mViewDepartment.setText(authenticateResponse.getDepartment().toString());
            }
            if (authenticateResponse.getPurpose() != null) {
                mViewPurpose.setText(authenticateResponse.getPurpose());
            }
            if (authenticateResponse.getVrfid() != null) {
                mVisitorId.setText(authenticateResponse.getVrfid());
            }
            if (authenticateResponse.getPhone() != null) {
                mPhoneNumber.setText(authenticateResponse.getPhone());
            }
            if (authenticateResponse.getEmail() != null) {
                mEmailId.setText(authenticateResponse.getEmail());
            }
            if (authenticateResponse.getEntryTime() != null) {
                mViewDataTime.setText(authenticateResponse.getEntryTime());
            }
            //------TODO----------------here buttons--------------based on primary----------

            if (authenticateResponse.getIsPrimary() == 1) {
                if (mSecondaryVisitorsDetails == 1) {
                    mVisitiorDetails.setVisibility(View.VISIBLE);
                    mAddVisitors.setVisibility(View.VISIBLE);
                } else if (mSecondaryVisitorsDetails == 0) {
                    mAddVisitors.setVisibility(View.VISIBLE);
                    mVisitiorDetails.setVisibility(View.GONE);
                }
            } else {
                mVisitiorDetails.setVisibility(View.GONE);
                mAddVisitors.setVisibility(View.GONE);
            }

         /*  if (authenticateResponse.getIsPrimary() == 0) {
                mVisitiorDetails.setVisibility(View.GONE);
                mAddVisitors.setVisibility(View.GONE);
            } else {
                mVisitiorDetails.setVisibility(View.VISIBLE);
                mAddVisitors.setVisibility(View.VISIBLE);
            }*/

            if (authenticateResponse.getIdProofUrl() != null) {
                if (authenticateResponse.getIdProofUrl() == 0) {
                    if (mAllowCaptureImage == 1) {
                        mVisitorImageLayout.setVisibility(View.VISIBLE);
                        mScanViewVrfbutton.setVisibility(View.VISIBLE);
                    } else {
                        mVisitorImageLayout.setVisibility(View.GONE);
                        mScanViewVrfbutton.setVisibility(View.GONE);
                    }
                } else {
                    mVisitorImageLayout.setVisibility(View.GONE);
                    mScanViewVrfbutton.setVisibility(View.GONE);
                }
            }
        }

        //-------------------TODO------------------- up to authenticate pojo--------------------


    }

    private void getSettingValuesFromDatabase() {
        if (mSettingDetails.getSettings().getSecondaryVisitorDetail() != null) {
            mSecondaryVisitorsDetails = mSettingDetails.getSettings().getSecondaryVisitorDetail();
        }
        if (mSettingDetails.getSettings().getAllowCaptureImage() != null) {
            mAllowCaptureImage = mSettingDetails.getSettings().getAllowCaptureImage();
        }
        if (mSettingDetails.getSettings().getSecurityImageOverride() != null) {
            mAllowSecurityImageOverride = mSettingDetails.getSettings().getSecurityImageOverride();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ApplicationConstants.OPEN_CAMERA) {
                if (data == null) {
                    AlertUtils.showInfoDialog(PrimaryVisitorDetailActivity.this, getString(R.string.app_name), getString(R.string.upload_pic_err));
                } else {
                    updateDocumentPic(data);
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            AlertUtils.showInfoDialog(PrimaryVisitorDetailActivity.this, getString(R.string.app_name), getString(R.string.pic));
        }
    }

    private void updateDocumentPic(Intent data) {
        Bundle extras = data.getExtras();
        bitmapFrontCam = (Bitmap) extras.get("data");
        mScanIdforviewVrfImage.setImageBitmap(bitmapFrontCam);
        mImageUri = getImageUri(getApplicationContext(), bitmapFrontCam);
      //  cameraPreviewImage();

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


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.scan_button_for_view_vrf:
                openCamera();
                break;
            case R.id.view_visitior_details:
                if (mVisitorId.getText().toString() != null) {
                    intent = VisitorListActivity.createIntentWithBundle(PrimaryVisitorDetailActivity.this, mVisitorId.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    AlertUtils.showInfoDialog(PrimaryVisitorDetailActivity.this, getString(R.string.app_name), getString(R.string.vrf_id));
                }
                break;
            case R.id.add_visitior_details:
                if (mSecondaryVisitorsDetails == 1) {
                    intent = AdditionalVisitorActivity.createIntentWithBundle(PrimaryVisitorDetailActivity.this, createVrfDetailsResponse, authenticateResponse);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                } else {
                    additionalVisitorsPopupDetails();
                }
                break;
            case R.id.submit_for_view_vrf_button:
                if (DeviceManager.getInstance().getTokenId() != null) {
                    if (createVrfDetailsResponse != null) {
                        if (createVrfDetailsResponse.getIdProofPath() == 0) {
                            if (mAllowCaptureImage == 1) {
                                if (mAllowSecurityImageOverride == 0) {
                                    if (mScanIdforviewVrfImage.getDrawable() != null) {
                                        viewAllSubmitVisitors(DeviceManager.getInstance().getTokenId());
                                    } else {
                                        AlertUtils.showInfoDialog(PrimaryVisitorDetailActivity.this, getString(R.string.app_name), getString(R.string.upload_pic));
                                    }
                                } else {
                                    viewAllSubmitVisitors(DeviceManager.getInstance().getTokenId());
                                }
                            } else {
                                viewAllSubmitVisitors(DeviceManager.getInstance().getTokenId());
                            }
                        } else {
                            viewAllSubmitVisitors(DeviceManager.getInstance().getTokenId());
                        }
                    }

                    if (authenticateResponse != null) {
                        if (authenticateResponse.getIdProofUrl() == 0) {
                            if (mAllowCaptureImage == 1) {
                                if (mAllowSecurityImageOverride == 0) {
                                    if (mScanIdforviewVrfImage.getDrawable() != null) {
                                        viewAllSubmitVisitors(DeviceManager.getInstance().getTokenId());
                                    } else {
                                        AlertUtils.showInfoDialog(PrimaryVisitorDetailActivity.this, getString(R.string.app_name), getString(R.string.upload_pic));
                                    }
                                } else {
                                    viewAllSubmitVisitors(DeviceManager.getInstance().getTokenId());
                                }
                            } else {
                                viewAllSubmitVisitors(DeviceManager.getInstance().getTokenId());
                            }
                        } else {
                            viewAllSubmitVisitors(DeviceManager.getInstance().getTokenId());

                        }
                    }
                }
                break;

        }
    }

    private void additionalVisitorsPopupDetails() {

        mSecondaryDialog = new Dialog(PrimaryVisitorDetailActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        mSecondaryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSecondaryDialog.setContentView(R.layout.secondary_visitors_popup);
        mSecondaryDialog.setCanceledOnTouchOutside(false);
        mAdditionalSecondaryVisitors = (EditText) mSecondaryDialog.findViewById(R.id.add_secondary_visitors);
        final Button submitButton = (Button) mSecondaryDialog.findViewById(R.id.submit_button_for_secondary_visitors);
        Button cancelButton = (Button) mSecondaryDialog.findViewById(R.id.cancel_button_for_secondary_visitors);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdditionalSecondaryVisitorsLayout.setVisibility(View.VISIBLE);
                if (validation()) {
                    //TODO service call is pending
                    mNumberOfVisitors.setText(mAdditionalSecondaryVisitors.getText().toString());
                    mSecondaryDialog.dismiss();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSecondaryDialog.dismiss();
            }
        });
        mSecondaryDialog.show();
    }

    public static Intent createIntentWithBundle(Context context, @NonNull CreateVrfDetailsResponse createVrfDetailsResponse) {
        Intent intent = new Intent(context, PrimaryVisitorDetailActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String myJsonResp = gson.toJson(createVrfDetailsResponse);
        bundle.putString(ApplicationConstants.kCreateVrfDetailsResponse, myJsonResp);
        intent.putExtras(bundle);
        return intent;
    }

    private void popsetThanksForView() {
        addAreaDialog = new Dialog(PrimaryVisitorDetailActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        addAreaDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addAreaDialog.setContentView(R.layout.thanks_popup);
        addAreaDialog.setCanceledOnTouchOutside(false);
        final Button okayButton = (Button) addAreaDialog.findViewById(R.id.okay_feedback);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                isBoolean = false;
                finish();
                addAreaDialog.dismiss();
            }

        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isBoolean == true) {
                    Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    addAreaDialog.dismiss();
                }
            }
        }, 10000);

        addAreaDialog.show();
    }

   /* private void cameraPreviewImage() {
        cameraPreviewDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        cameraPreviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cameraPreviewDialog.setContentView(R.layout.preview_camera_pic_popup);
        final Button otpButton = (Button) cameraPreviewDialog.findViewById(R.id.submit_button_for_camera_preview);
        Button cancelButton = (Button) cameraPreviewDialog.findViewById(R.id.cancel_button_for_camera_preview);
        mCameraPreviewImage = (ImageView) cameraPreviewDialog.findViewById(R.id.camera_preview_image);
        mCameraPreviewImage.setImageBitmap(bitmapFrontCam);
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScanIdforviewVrfImage.setImageBitmap(bitmapFrontCam);
                cameraPreviewDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreviewDialog.dismiss();
            }
        });
        cameraPreviewDialog.show();
    }
*/
    private boolean validation() {
        boolean success = true;
        if (StringUtil.isEmpty(mAdditionalSecondaryVisitors.getText().toString())) {
            mAdditionalSecondaryVisitors.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        } else
            mAdditionalSecondaryVisitors.setError(null);
        return success;
    }

    //-----------------------------submit all visitors---------------------------------------
    public static Intent createIntentWithBundleAuthenticate(Context context, @NonNull AuthenticateResponse authenticateResponse) {
        Intent intent = new Intent(context, PrimaryVisitorDetailActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String myJsonResp = gson.toJson(authenticateResponse);
        bundle.putString(ApplicationConstants.kAuthenticateResponse, myJsonResp);
        intent.putExtras(bundle);
        return intent;
    }
    //-----------------------------------submitAdditionalVisitors------------------------------------------------------

    private void viewAllSubmitVisitors(final LicenseDetails licenseDetails) {
        startBlockRefreshing();
        File file = null;
        MultipartBody.Part mAdditionalVisitors = null;
        MultipartBody.Part mVrfSid = null;
        MultipartBody.Part mScanfileBody = null;
        RequestBody mFileForAdditionalVisitors = null;
        if (mImageUri != null) {
            file = new File(getRealPathFromURI(mImageUri));
        }
        if (file != null) {
            mFileForAdditionalVisitors = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }

        if (mFileForAdditionalVisitors != null) {
            mScanfileBody = MultipartBody.Part.createFormData(ApplicationConstants.kScanfile, file.getName(), mFileForAdditionalVisitors);
        }

        if (authenticateResponse != null) {
            mVrfSid = MultipartBody.Part.createFormData(ApplicationConstants.kVrfsid, authenticateResponse.getVrfsid());
        }
        if (createVrfDetailsResponse != null) {
            mVrfSid = MultipartBody.Part.createFormData(ApplicationConstants.kVrfsid, createVrfDetailsResponse.getVrfsid());
        }

        if (mNumberOfVisitors.getText() != null) {
            mAdditionalVisitors = MultipartBody.Part.createFormData(ApplicationConstants.mAdditionalVisitorsConstant, mNumberOfVisitors.getText().toString());
        }

        final NetworkService networkService = ServiceManager.getInstance().createService(NetworkService.class);
        final Observable<AdditionalVrfResponse> additionalVrfResponseObservable = networkService.additionalNoOfVrf(licenseDetails.getToken(), mScanfileBody, mVrfSid, mAdditionalVisitors);
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
                        handleError(e);
                    }

                    @Override
                    public void onNext(AdditionalVrfResponse additionalVrfResponse) {
                        if (additionalVrfResponse.getStatusCode().equalsIgnoreCase("200")) {
                            popsetThanksForView();
                        } else if (additionalVrfResponse.getStatusCode().equalsIgnoreCase("500")) {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(PrimaryVisitorDetailActivity.this, getString(R.string.app_name), additionalVrfResponse.getMessage());
                        } else {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(PrimaryVisitorDetailActivity.this, getString(R.string.app_name), additionalVrfResponse.getMessage());
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

    public static Intent createIntentWithBundle(Context context, @NonNull CreateVrfDetailsResponse createVrfDetailsResponse, @NonNull AuthenticateResponse authenticateResponse) {
        Intent intent = new Intent(context, PrimaryVisitorDetailActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String myJsonResp = gson.toJson(createVrfDetailsResponse);
        String myJsonAuthen = gson.toJson(authenticateResponse);
        bundle.putString(ApplicationConstants.kCreateVrfDetailsResponse, myJsonResp);
        bundle.putString(ApplicationConstants.kAuthenticateResponse, myJsonAuthen);
        intent.putExtras(bundle);
        return intent;
    }


}
