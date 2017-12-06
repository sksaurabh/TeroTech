package com.telloquent.vms.utils;
import android.Manifest;
import java.text.SimpleDateFormat;
public interface ApplicationConstants
{
    String PHONE_PATTERN = "^[789]\\d{9}$";
    String key_pattern= "^[a-zA-Z0-9]*$";
    String Db_Name="Vms";

    int MULTIPLE_PERMISSIONS = 211;
    String[] mPermissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    SimpleDateFormat mDateTimeFormat=new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
    int CAMERA_PIC_REQUEST = 0;
    int mOneday=86400000;
    String mBitmapImage="BitmapImage";
    String kVisitorsfromDb = "kVisitorDetails";
    String kCreateVrfDetailsResponse="kCreateVrfDetailsResponse";
    String[] duration_in_hours = { "Duration (in hours)", "1", "2", "3", "4", "5","6","7","8","9"};
    String otpRandomNum="0123456789";
    int length=6;
    String mPurposeConstant="purpose";
    String mDepartmentConstant="department";
    String mEmployeeEmailConstant="employee_email";
    String mVrfTypeConstant="vrf_type";
    String mPinCodeConstant="pin_code";
    String mCountryConstant="country";
    String mStateConstant="state";
    String mCityConstant="city";
    String mLocalityConstant="locality";
    String mCompanyNameConstant="company";
    String mNameConstant="name";
    String mPhoneConstant="phone";
    String mEmailConstant="email";
    String mID_ProofConstant="scan_file";
    String kvrfID="kvrfID";
    String kAuthenticateResponse="kAuthenticateResponse";
    String mAdditionalVisitorsConstant="num_of_additional_visitor";
    String kScanfile="scan_file";
    String kVrfid="vrfid";
    String kEmail="email";
    String kPhone="phone";
    String kName="name";
    String kCompanyName="company";
    String kLocality="locality";
    String kCity="city";
    String kState="state";
    String kCountry="country";
    String kPinCode="pin_code";
    String kVrfsid="vrfsid";
    String kDuration="duration";
    String KEmployeePhoneConstant="employee_phone";
    int OPEN_CAMERA = 508;
}
