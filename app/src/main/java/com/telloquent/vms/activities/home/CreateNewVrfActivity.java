package com.telloquent.vms.activities.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.telloquent.vms.R;
import com.telloquent.vms.base.BaseDisplayActivity;
import com.telloquent.vms.model.checkvisitors.VisitorDetailsResponse;
import com.telloquent.vms.model.createvrf.CreateVrfDetails;
import com.telloquent.vms.model.createvrf.CreateVrfDetailsResponse;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.settingsmodel.Department;
import com.telloquent.vms.model.settingsmodel.Purpose;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.servicemanager.NetworkService;
import com.telloquent.vms.servicemanager.ServiceManager;
import com.telloquent.vms.support.DepartmentsDB;
import com.telloquent.vms.support.PurposesDB;
import com.telloquent.vms.support.SettingsDB;
import com.telloquent.vms.utils.AlertUtils;
import com.telloquent.vms.utils.ApplicationConstants;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.DeviceManager;
import com.telloquent.vms.utils.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SuppressWarnings("deprecation")
public class CreateNewVrfActivity extends BaseDisplayActivity implements View.OnClickListener {
    private ImageView mScanIdImage, mFrameIcons;
    private Bitmap bitmapFrontCam;
    private Button mScanIdButton;
    private EditText mCompanyName, mAreaDetails, mPrimaryVisitorName, mMobileNumber, mEmployeeEmailId;
    private EditText mEmailId;
    private Button mResetVrfForm, mSubmitVrf;
    private EditText mCity, mState, mCountry, mPinCode;
    private Spinner mPurposeSpinner, mDepartmentSpinner, mDurationSpinner;
    private RadioGroup mOfficePersonalRadioGroup, mDepartmentEmployeeRadioGroup;
    private RadioButton mOfficialRadioButton, mPersonalRadioButton, mDepartmentRadioButton, mEmployeeRadioButton;
    private PurposesDB dbPurposeHelper;
    private DepartmentsDB dbDepartmentHelper;
    private List<Purpose> mPurposeList;
    private List<Department> mDepartmentsList;
    private LinearLayout mPersonalLinearLayout;
    private LicenseDetails mLicenseDetails;
    private CreateVrfDetails createVrfDetails;
    private VisitorDetailsResponse visitorDetailsResponse;
    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    private Uri mImageUri;
    private String mOfficePersonalvalue, mEmployeeDepartmentvalue;
    private RelativeLayout mDepEmpRadioButtonRelativeLayout;
    private String mSelectedPurpose, mSelectedDepatment, mSelectedDuration;
    private TextInputLayout mEmployeeEmailIdInputText;
    private SettingsDB dbSettingsHelper;
    private SettingDetailsResponse mSettingDetails;
    private int mAllowCaptureImage, mSecurityOverrideImage, mRequiredPrimaryCompany, mRequiredPrimaryCompanyAddress, mDisablePersonalVisits;
    private RelativeLayout mScanImageRelativeLayout;
    private LinearLayout mDisablePersonVisitLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_vrf);
    }

    @Override
    protected void initializeView() {
        setupActionBar(getResources().getString(R.string.fill_vrf_information), ActionBarActivityLeftAction.ACTION_BACK, ActionBarActivityRightAction.ACTION_NONE, ActionBarActivityRight2Action.COMPANY_LOGO);
        shouldDiscardChanges = true;
        mScanIdImage = (ImageView) findViewById(R.id.scan_id);
        mCompanyName = (EditText) findViewById(R.id.company_name);
        mAreaDetails = (EditText) findViewById(R.id.area);
        mPrimaryVisitorName = (EditText) findViewById(R.id.visitor_name);
        mMobileNumber = (EditText) findViewById(R.id.create_vrf_mobile_number);
        mCity = (EditText) findViewById(R.id.city);
        mState = (EditText) findViewById(R.id.state);
        mCountry = (EditText) findViewById(R.id.country);
        mPinCode = (EditText) findViewById(R.id.pin_code);
        mEmailId = (EditText) findViewById(R.id.email_id);
        mEmployeeEmailId = (EditText) findViewById(R.id.employee_email_id);
        mEmployeeEmailIdInputText = (TextInputLayout) findViewById(R.id.employee_email_id_inputText_layout);
        mResetVrfForm = (Button) findViewById(R.id.reset_vrf_form);
        mSubmitVrf = (Button) findViewById(R.id.submit_vrf_button);
        mScanIdButton = (Button) findViewById(R.id.scan_button_for_create_vrf);
        mOfficePersonalRadioGroup = (RadioGroup) findViewById(R.id.official_personel_radio_group);
        mOfficialRadioButton = (RadioButton) findViewById(R.id.official_radio_button);
        mPersonalRadioButton = (RadioButton) findViewById(R.id.personal_radio_button);
        mScanImageRelativeLayout = (RelativeLayout) findViewById(R.id.scan_image_relative_layout);
        mDisablePersonVisitLayout = (LinearLayout) findViewById(R.id.disable_personVisit);
        mDepartmentEmployeeRadioGroup = (RadioGroup) findViewById(R.id.vrf_department_employee_radio_group);
        mDepartmentRadioButton = (RadioButton) findViewById(R.id.department_radio_button);
        mEmployeeRadioButton = (RadioButton) findViewById(R.id.employee_radio_button);
        mPurposeSpinner = (Spinner) findViewById(R.id.purpose_spinner);
        mDepartmentSpinner = (Spinner) findViewById(R.id.department_spinner);
        mPersonalLinearLayout = (LinearLayout) findViewById(R.id.personal_linear_layout);
        mDepEmpRadioButtonRelativeLayout = (RelativeLayout) findViewById(R.id.employee_department_relative_layout);
        mDurationSpinner = (Spinner) findViewById(R.id.duration_spinner);
        mFrameIcons = (ImageView) findViewById(R.id.frame_icons);
        //click events
        mScanIdButton.setOnClickListener(this);
        mSubmitVrf.setOnClickListener(this);
        mResetVrfForm.setOnClickListener(this);
        dbPurposeHelper = new PurposesDB(this);
        dbDepartmentHelper = new DepartmentsDB(this);
        mPurposeList = new ArrayList<>();
        mDepartmentsList = new ArrayList<>();
        mSettingDetails = new SettingDetailsResponse();
        dbSettingsHelper = new SettingsDB(this);
        mSettingDetails = dbSettingsHelper.getSettingDetails();
        loadSpinnerData();
        mPurposeSpinner.setOnItemSelectedListener(new myOnItemSelectedPurposeListener());
        mDepartmentSpinner.setOnItemSelectedListener(new myOnItemSelectedforDepListener());
        mDurationSpinner.setOnItemSelectedListener(new myDurationForVrdListener());
        getDataFromSettingsDatabase();
        setRadioButtonValues();
        createVrfDetails = new CreateVrfDetails();
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            Gson gson = new Gson();
            visitorDetailsResponse = gson.fromJson(bundle.getString(ApplicationConstants.kVisitorsfromDb), VisitorDetailsResponse.class);
        }
        getData();
    }

    private void getDataFromSettingsDatabase() {
        if (mSettingDetails.getSettings().getAllowCaptureImage() != null) {
            mAllowCaptureImage = mSettingDetails.getSettings().getAllowCaptureImage();
        }
        if (mSettingDetails.getSettings().getSecurityImageOverride() != null) {
            mSecurityOverrideImage = mSettingDetails.getSettings().getSecurityImageOverride();
        }
        if (mSettingDetails.getSettings().getRequirePrimaryCompany() != null) {
            mRequiredPrimaryCompany = mSettingDetails.getSettings().getRequirePrimaryCompany();
        }
        if (mSettingDetails.getSettings().getRequirePrimaryCompanyAddress() != null) {
            mRequiredPrimaryCompanyAddress = mSettingDetails.getSettings().getRequirePrimaryCompanyAddress();
        }
        if (mSettingDetails.getSettings().getDisablePersonalVisits() != null) {
            mDisablePersonalVisits = mSettingDetails.getSettings().getDisablePersonalVisits();
        }
    }

    private void setRadioButtonValues() {
        if (mDisablePersonalVisits == 1) {
            mDisablePersonVisitLayout.setVisibility(View.INVISIBLE);
            mOfficePersonalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    if (mOfficialRadioButton.isChecked()) {
                        mPersonalLinearLayout.setVisibility(View.VISIBLE);
                        mOfficePersonalvalue = ((RadioButton) findViewById(mOfficePersonalRadioGroup.getCheckedRadioButtonId())).getText().toString();
                    } else {
                        mPersonalLinearLayout.setVisibility(View.GONE);
                        mOfficePersonalvalue = ((RadioButton) findViewById(mOfficePersonalRadioGroup.getCheckedRadioButtonId())).getText().toString();
                    }
                }
            });

            mOfficePersonalvalue = ((RadioButton) findViewById(mOfficePersonalRadioGroup.getCheckedRadioButtonId())).getText().toString();
        } else {
            mDisablePersonVisitLayout.setVisibility(View.VISIBLE);
            mOfficePersonalRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    if (mPersonalRadioButton.isChecked()) {
                        mPersonalLinearLayout.setVisibility(View.GONE);
                        mOfficePersonalvalue = ((RadioButton) findViewById(mOfficePersonalRadioGroup.getCheckedRadioButtonId())).getText().toString();
                    } else {
                        mPersonalLinearLayout.setVisibility(View.VISIBLE);
                        mOfficePersonalvalue = ((RadioButton) findViewById(mOfficePersonalRadioGroup.getCheckedRadioButtonId())).getText().toString();
                    }
                }
            });
            mOfficePersonalvalue = ((RadioButton) findViewById(mOfficePersonalRadioGroup.getCheckedRadioButtonId())).getText().toString();
        }
        mDepartmentEmployeeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (mEmployeeRadioButton.isChecked()) {
                    mEmployeeEmailIdInputText.setVisibility(View.VISIBLE);
                    mDepEmpRadioButtonRelativeLayout.setVisibility(View.GONE);
                    mEmployeeDepartmentvalue = ((RadioButton) findViewById(mDepartmentEmployeeRadioGroup.getCheckedRadioButtonId())).getText().toString();
                } else {
                    mEmployeeEmailIdInputText.setVisibility(View.GONE);
                    mDepEmpRadioButtonRelativeLayout.setVisibility(View.VISIBLE);
                    mEmployeeDepartmentvalue = ((RadioButton) findViewById(mDepartmentEmployeeRadioGroup.getCheckedRadioButtonId())).getText().toString();
                }
            }
        });
        mEmployeeDepartmentvalue = ((RadioButton) findViewById(mDepartmentEmployeeRadioGroup.getCheckedRadioButtonId())).getText().toString();
        if (mEmployeeDepartmentvalue != null) {
            if (mEmployeeDepartmentvalue.equalsIgnoreCase("Employee")) {
                mDepEmpRadioButtonRelativeLayout.setVisibility(View.GONE);
            } else {
                mDepEmpRadioButtonRelativeLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void getData() {

        if (visitorDetailsResponse != null) {
            if (visitorDetailsResponse.getEmail() != null && visitorDetailsResponse.getEmail().length() != 0) {
                mEmailId.setText(visitorDetailsResponse.getEmail());
            }
            if (visitorDetailsResponse.getPhone() != null && visitorDetailsResponse.getPhone().length() != 0) {
                mMobileNumber.setText(visitorDetailsResponse.getPhone());
            }
            if (visitorDetailsResponse.getName() != null && visitorDetailsResponse.getName().length() != 0) {
                mPrimaryVisitorName.setText(visitorDetailsResponse.getName());
            }
            if (visitorDetailsResponse.getCompanyName() != null && visitorDetailsResponse.getCompanyName().length() != 0) {
                mCompanyName.setText(visitorDetailsResponse.getCompanyName());
            }
            if (visitorDetailsResponse.getLocality() != null && visitorDetailsResponse.getLocality().length() != 0) {
                mAreaDetails.setText(visitorDetailsResponse.getLocality());
            }
            if (visitorDetailsResponse.getCity() != null && visitorDetailsResponse.getCity().length() != 0) {
                mCity.setText(visitorDetailsResponse.getCity());
            }
            if (visitorDetailsResponse.getState() != null && visitorDetailsResponse.getState().length() != 0) {
                mState.setText(visitorDetailsResponse.getState());
            }
            if (visitorDetailsResponse.getCountry() != null && visitorDetailsResponse.getCountry().length() != 0) {
                mCountry.setText(visitorDetailsResponse.getCountry());
            }
            if (visitorDetailsResponse.getPinCode() != null && visitorDetailsResponse.getPinCode().length() != 0) {
                mPinCode.setText(visitorDetailsResponse.getPinCode());
            }
            if (visitorDetailsResponse.getIdProof() != null) {
                if (visitorDetailsResponse.getIdProof() == 0) {
                    if (mAllowCaptureImage == 1) {
                        mScanImageRelativeLayout.setVisibility(View.VISIBLE);
                        mScanIdButton.setVisibility(View.VISIBLE);
                    } else {
                        mScanImageRelativeLayout.setVisibility(View.GONE);
                        mScanIdButton.setVisibility(View.GONE);
                    }
                } else {
                    mScanIdButton.setVisibility(View.GONE);
                    mScanImageRelativeLayout.setVisibility(View.GONE);
                }
            } else {
                if (mAllowCaptureImage == 0) {
                    mScanIdButton.setVisibility(View.GONE);
                    mScanImageRelativeLayout.setVisibility(View.GONE);
                } else {
                    mScanIdButton.setVisibility(View.VISIBLE);
                    mScanImageRelativeLayout.setVisibility(View.VISIBLE);
                }
            }

        } else {
            if (mAllowCaptureImage == 0) {
                mScanIdButton.setVisibility(View.GONE);
                mScanImageRelativeLayout.setVisibility(View.GONE);
            } else {
                mScanImageRelativeLayout.setVisibility(View.VISIBLE);
                mScanIdButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ApplicationConstants.OPEN_CAMERA) {
                if (data == null) {
                    AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), getString(R.string.upload_pic_err));
                } else {
                    updateDocumentPic(data);
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), getString(R.string.pic));
        }
    }

    private void updateDocumentPic(Intent data) {
        Bundle extras = data.getExtras();
        bitmapFrontCam = (Bitmap) extras.get("data");
        mScanIdImage.setImageBitmap(bitmapFrontCam);

        mImageUri = getImageUri(getApplicationContext(), bitmapFrontCam);


        //  mScanIdImage.setImageURI(mImageUri);
       /* Picasso.with(CreateNewVrfActivity.this)
                .load(mImageUri)
                .placeholder(R.drawable.blank_profile_male)
                .error(R.drawable.blank_profile_male).fit()
                .into(mScanIdImage);*/
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
        switch (v.getId()) {
            case R.id.scan_button_for_create_vrf:
                openCamera();
                break;
            case R.id.reset_vrf_form:
                resetVrfForm();
                break;
            case R.id.submit_vrf_button:
                mPurposeSpinner.setOnItemSelectedListener(new myOnItemSelectedPurposeListener());
                mDepartmentSpinner.setOnItemSelectedListener(new myOnItemSelectedforDepListener());
                mDurationSpinner.setOnItemSelectedListener(new myDurationForVrdListener());

                if (validation() && !mSelectedPurpose.equalsIgnoreCase("Select Purpose") && !mSelectedDuration.equalsIgnoreCase("Duration (in hours)")) {
                    checkVrfService();
                } else {
                    AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), getString(R.string.select_duration));
                }

                break;
        }
    }

    private void resetVrfForm() {
        mCompanyName.getText().clear();
        mAreaDetails.getText().clear();
        mPrimaryVisitorName.getText().clear();
        mPinCode.getText().clear();
        mCity.getText().clear();
        mState.getText().clear();
        mCountry.getText().clear();
        mEmployeeEmailId.getText().clear();
    }

    private boolean validation() {
        boolean success = true;
        if (visitorDetailsResponse.getIdProof() != null) {
            if (visitorDetailsResponse.getIdProof() == 0) {
                if (mAllowCaptureImage == 1) {
                    if (mSecurityOverrideImage == 0) {
                        if (mScanIdImage.getDrawable() == null) {
                            AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), getString(R.string.please_upload_image));
                            success = false;
                        }
                    }
                }
            }
        }

       /* if (!isImageSelected && success) {
            AlertUtils.showInfoDialog(ProfileDetailActivity.this, "", getString(R.string.upload_picture_msg));
            success = false;
        }*/
        if (mRequiredPrimaryCompany == 1) {
            if (mOfficialRadioButton.isChecked()) {
                if (StringUtil.isEmpty(mCompanyName.getText().toString())) {
                    mCompanyName.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                    success = false;
                } else
                    mCompanyName.setError(null);
            }
        }
        if (mRequiredPrimaryCompanyAddress == 1) {
            if (mOfficialRadioButton.isChecked()) {
                if (StringUtil.isEmpty(mAreaDetails.getText().toString())) {
                    mAreaDetails.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                    success = false;
                } else
                    mAreaDetails.setError(null);

                if (StringUtil.isEmpty(mCity.getText().toString())) {
                    mCity.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                    success = false;
                } else
                    mCity.setError(null);
                if (StringUtil.isEmpty(mState.getText().toString())) {
                    mState.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                    success = false;
                } else
                    mState.setError(null);
                if (StringUtil.isEmpty(mCountry.getText().toString())) {
                    mCountry.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                    success = false;
                } else
                    mCountry.setError(null);
                if (StringUtil.isEmpty(mPinCode.getText().toString())) {
                    mPinCode.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                    success = false;
                } else
                    mPinCode.setError(null);
            }
        }

        if (StringUtil.isEmpty(mPrimaryVisitorName.getText().toString())) {
            mPrimaryVisitorName.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
            success = false;
        } else
            mPrimaryVisitorName.setError(null);

        if (mEmployeeDepartmentvalue.equalsIgnoreCase("Employee")) {
            if (StringUtil.isEmpty(mEmployeeEmailId.getText().toString())) {
                mEmployeeEmailId.setError(getResources().getString(R.string.activity_mandatory_fields_missing));
                success = false;
            } else
                mEmployeeEmailId.setError(null);
        } else {
            if (mSelectedDepatment.equalsIgnoreCase("Select Department")) {
                AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), getString(R.string.select_department));
                success = false;
            }
        }

        return success;
    }


    //---------------------spinner data from db code------------------------
    private void loadSpinnerData() {
        //-----------------purpose spinner--------------
        mPurposeList = dbPurposeHelper.getPurposeList();
        if (mPurposeList != null) {
            Purpose purpose = new Purpose();
            purpose.setName("Select Purpose");
            mPurposeList.add(0, purpose);
            String[] purposeNameList = new String[mPurposeList.size()];
            for (int i = 0; i < mPurposeList.size(); i++) {
                purposeNameList[i] = mPurposeList.get(i).getName();
            }
            ArrayAdapter<String> purposeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, purposeNameList);
            purposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mPurposeSpinner.setAdapter(purposeAdapter);
        }
        //------------------------------department db----------------------------
        mDepartmentsList = dbDepartmentHelper.getDepartmentList();
        if (mDepartmentsList != null) {
            Department department = new Department();
            department.setName("Select Department");
            mDepartmentsList.add(0, department);
            String[] depatmentNameList = new String[mDepartmentsList.size()];
            for (int i = 0; i < mDepartmentsList.size(); i++) {
                depatmentNameList[i] = mDepartmentsList.get(i).getName();
            }
            ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, depatmentNameList);
            departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDepartmentSpinner.setAdapter(departmentAdapter);
        }

        ArrayAdapter mDurationSpinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ApplicationConstants.duration_in_hours);
        mDurationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDurationSpinner.setAdapter(mDurationSpinnerAdapter);
    }

    public class myOnItemSelectedPurposeListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
            mSelectedPurpose = parent.getItemAtPosition(pos).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    public class myOnItemSelectedforDepListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
            mSelectedDepatment = parent.getItemAtPosition(pos).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    public class myDurationForVrdListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {
            mSelectedDuration = parent.getItemAtPosition(pos).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private void checkVrfService() {
        if (DeviceManager.getInstance().getTokenId() != null) {
            if (mMobileNumber.getText() != null && mMobileNumber.getText().length() != 0) {
                createVrfDetails.setPhone(mMobileNumber.getText().toString());
            }
            if (mCity.getText() != null && mCity.getText().length() != 0) {
                createVrfDetails.setCity(mCity.getText().toString());
            }
            if (mCompanyName.getText() != null && mCompanyName.getText().length() != 0) {
                createVrfDetails.setCompany(mCompanyName.getText().toString());
            }

            if (mEmailId.getText() != null && mEmailId.getText().length() != 0) {
                createVrfDetails.setEmail(mEmailId.getText().toString());
            }
            if (mOfficePersonalvalue != null) {
                createVrfDetails.setVrfType(mOfficePersonalvalue);
            }
            if (mAreaDetails.getText() != null && mAreaDetails.getText().length() != 0) {
                createVrfDetails.setLocality(mAreaDetails.getText().toString());
            }
            if (mCountry.getText() != null && mCountry.getText().length() != 0) {
                createVrfDetails.setCountry(mCountry.getText().toString());
            }
            if (mEmployeeDepartmentvalue != null) {
                if (mEmployeeDepartmentvalue.equalsIgnoreCase("Employee")) {

                    if (mEmployeeEmailId.getText() != null && mEmployeeEmailId.getText().length() != 0) {
                        if (AlertUtils.isValidPhoneNumber(mEmployeeEmailId.getText().toString())) {
                            createVrfDetails.setEmployeePhone(mEmployeeEmailId.getText().toString());
                        } else {
                            createVrfDetails.setEmployeeEmail(mEmployeeEmailId.getText().toString());
                        }
                    }
                } else {
                    if (mSelectedDepatment != null) {
                        createVrfDetails.setDepartment(mSelectedDepatment);
                    }
                }
                if (mSelectedPurpose != null) {
                    createVrfDetails.setPurpose(mSelectedPurpose);
                }
                if (mPinCode.getText() != null && mPinCode.getText().length() != 0) {
                    createVrfDetails.setPinCode(mPinCode.getText().toString());
                }
                if (mState.getText() != null && mState.getText().length() != 0) {
                    createVrfDetails.setState(mState.getText().toString());
                }

                if (mPrimaryVisitorName.getText() != null && mPrimaryVisitorName.getText().length() != 0) {
                    createVrfDetails.setName(mPrimaryVisitorName.getText().toString());
                }
            }

            if (mSelectedDuration != null) {
                createVrfDetails.setDuration(mSelectedDuration);
            }
            mLicenseDetails = DeviceManager.getInstance().getTokenId();
            if (mLicenseDetails != null) {
                CreateVtfService(mLicenseDetails, createVrfDetails);
            } else {
                AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), getString(R.string.activity_mandatory_fields_missing));
            }
        } else {
            AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
        }
    }


    //-------------------------------------------------create Vrf details service call-----------------------


    public void CreateVtfService(LicenseDetails mLicenseDetails, CreateVrfDetails createVrfDetails) {
        File file = null;
        startBlockRefreshing();
        if (mImageUri != null) {
            file = new File(getRealPathFromURI(mImageUri));
        }
        MultipartBody.Part mDepartmentForCreate = null;
        MultipartBody.Part mEmployeeEmail = null;
        MultipartBody.Part mLocality = null;
        MultipartBody.Part mCitydetails = null;
        MultipartBody.Part mCompany_name = null;
        MultipartBody.Part mNameForCreate = null;
        MultipartBody.Part mStateForCreate = null;
        MultipartBody.Part mCountryForCreate = null;
        MultipartBody.Part mPincodeForCreate = null;
        MultipartBody.Part mVrftypeForCreate = null;
        MultipartBody.Part mPurposeForCreate = null;
        MultipartBody.Part mPhoneNumForCreate = null;
        MultipartBody.Part mEmailForCreate = null;
        MultipartBody.Part mScanImageBody = null;
        RequestBody mRequestFileForCreate = null;
        MultipartBody.Part mSelectDuration = null;
        MultipartBody.Part mEmployeePhoneNumber = null;
        try {
            if (file != null) {
                mRequestFileForCreate = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            }
            if (mRequestFileForCreate != null) {
                mScanImageBody = MultipartBody.Part.createFormData("scan_file", file.getName(), mRequestFileForCreate);
            }

            mEmailForCreate = MultipartBody.Part.createFormData(ApplicationConstants.mEmailConstant, (createVrfDetails.getEmail() != null) ? createVrfDetails.getEmail() : "");

            if (createVrfDetails.getPhone() != null) {
                mPhoneNumForCreate = MultipartBody.Part.createFormData(ApplicationConstants.mPhoneConstant, createVrfDetails.getPhone());
            }

            mNameForCreate = MultipartBody.Part.createFormData(ApplicationConstants.mNameConstant, (createVrfDetails.getName() != null) ? createVrfDetails.getName() : "");

            mCompany_name = MultipartBody.Part.createFormData(ApplicationConstants.mCompanyNameConstant, (createVrfDetails.getCompany() != null) ? createVrfDetails.getCompany() : "");

            mLocality = MultipartBody.Part.createFormData(ApplicationConstants.kLocality, (createVrfDetails.getLocality() != null) ? createVrfDetails.getLocality() : "");

            mCitydetails = MultipartBody.Part.createFormData(ApplicationConstants.mCityConstant, (createVrfDetails.getCity() != null) ? createVrfDetails.getCity() : "");

            mStateForCreate = MultipartBody.Part.createFormData(ApplicationConstants.mStateConstant, (createVrfDetails.getState() != null) ? createVrfDetails.getState() : "");

            mCountryForCreate = MultipartBody.Part.createFormData(ApplicationConstants.mCountryConstant, (createVrfDetails.getCountry() != null) ? createVrfDetails.getCountry() : "");

            mPincodeForCreate = MultipartBody.Part.createFormData(ApplicationConstants.mPinCodeConstant, (createVrfDetails.getPinCode() != null) ? createVrfDetails.getPinCode() : "");

            mVrftypeForCreate = MultipartBody.Part.createFormData(ApplicationConstants.mVrfTypeConstant, (createVrfDetails.getVrfType() != null) ? createVrfDetails.getVrfType() : "");

            mEmployeeEmail = MultipartBody.Part.createFormData(ApplicationConstants.mEmployeeEmailConstant, (createVrfDetails.getEmployeeEmail() != null) ? createVrfDetails.getEmployeeEmail().toString() : "");

            mEmployeePhoneNumber = MultipartBody.Part.createFormData(ApplicationConstants.KEmployeePhoneConstant, (createVrfDetails.getEmployeePhone() != null) ? createVrfDetails.getEmployeePhone().toString() : "");

            mDepartmentForCreate = MultipartBody.Part.createFormData(ApplicationConstants.mDepartmentConstant, (createVrfDetails.getDepartment() != null) ? createVrfDetails.getDepartment() : "");

            if (createVrfDetails.getPurpose() != null) {
                mPurposeForCreate = MultipartBody.Part.createFormData(ApplicationConstants.mPurposeConstant, createVrfDetails.getPurpose());
            }
            if (createVrfDetails.getDuration() != null) {
                mSelectDuration = MultipartBody.Part.createFormData(ApplicationConstants.kDuration, createVrfDetails.getDuration());
            }

            final NetworkService createVrfDetailsService = ServiceManager.getInstance().createService(NetworkService.class);
            Observable<CreateVrfDetailsResponse> checkVisitorsObservable = createVrfDetailsService.createVrfServiceResponse(mLicenseDetails.getToken(), mEmailForCreate, mPhoneNumForCreate, mNameForCreate, mLocality, mCitydetails, mStateForCreate, mCountryForCreate, mPincodeForCreate, mVrftypeForCreate, mEmployeeEmail, mEmployeePhoneNumber, mPurposeForCreate, mSelectDuration, mDepartmentForCreate, mCompany_name, mScanImageBody);
            checkVisitorsObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CreateVrfDetailsResponse>() {
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
                        public void onNext(CreateVrfDetailsResponse createVrfDetailsResponse) {
                            if (createVrfDetailsResponse.getStatusCode() == 200) {
                                Intent intent = PrimaryVisitorDetailActivity.createIntentWithBundle(getApplicationContext(), createVrfDetailsResponse);
                                startActivity(intent);
                                finish();
                            } else if (createVrfDetailsResponse.getStatusCode() == 401) {
                                stopBlockRefreshing();
                                AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), createVrfDetailsResponse.getMessage());
                            } else {
                                stopBlockRefreshing();
                                AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), createVrfDetailsResponse.getMessage());
                            }
                        }
                    });
        } catch (Exception e) {
            stopBlockRefreshing();
            AlertUtils.showInfoDialog(CreateNewVrfActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
        }
    }

    public static Intent createIntentWithBundle(Context context, @NonNull VisitorDetailsResponse visitorDetailsResponse) {
        Intent intent = new Intent(context, CreateNewVrfActivity.class);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String myJson = gson.toJson(visitorDetailsResponse);
        bundle.putString(ApplicationConstants.kVisitorsfromDb, myJson);
        intent.putExtras(bundle);
        return intent;
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

// herhe image  writing the code to delete the image and reduce t


}
