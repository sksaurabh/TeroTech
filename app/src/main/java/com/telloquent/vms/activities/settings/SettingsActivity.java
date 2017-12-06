package com.telloquent.vms.activities.settings;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.telloquent.vms.R;
import com.telloquent.vms.activities.home.DashBoardActivity;
import com.telloquent.vms.adapter.DepartmentGridAdapter;
import com.telloquent.vms.adapter.PurposeGridAdapter;
import com.telloquent.vms.base.BaseActivity;
import com.telloquent.vms.base.BaseDisplayActivity;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.settingsmodel.Department;
import com.telloquent.vms.model.settingsmodel.Purpose;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.servicemanager.NetworkService;
import com.telloquent.vms.servicemanager.ServiceManager;
import com.telloquent.vms.support.DepartmentsDB;
import com.telloquent.vms.support.PurposesDB;
import com.telloquent.vms.support.SettingsDB;
import com.telloquent.vms.support.ThemeValuesDB;
import com.telloquent.vms.utils.AlertUtils;
import com.telloquent.vms.utils.ApplicationConstants;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.DeviceManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingsActivity extends BaseDisplayActivity implements View.OnClickListener {
    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    private GridView mPurposeDetails, mDepartmentDetails;
    private PurposeGridAdapter mPurposeGridAdapter;
    private List<Purpose> purposesList;
    private SettingsDB dbSettingsHelper;
    private PurposesDB dbPurposeHelper;
    private DepartmentsDB dbDepartmentHelper;
    private SettingDetailsResponse mSettingDetails;
    private DepartmentGridAdapter mDepartmentGridAdapter;
    private List<Department> departmentsList;
    SimpleDateFormat df = ApplicationConstants.mDateTimeFormat;
    private String formattedDate=null;
    private TextView mDateTimeSettings;
    private ThemeValuesDB dbThemeValuesHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void initializeView() {
        setupActionBar(getResources().getString(R.string.settings_screen), BaseActivity.ActionBarActivityLeftAction.ACTION_BACK, BaseActivity.ActionBarActivityRightAction.ACTION_UPDATE_BUTTON, ActionBarActivityRight2Action.ACTION_RESET);
        mPurposeDetails = (GridView) findViewById(R.id.purpose_grid_details);
        mDepartmentDetails = (GridView) findViewById(R.id.department_grid_details);
        mDateTimeSettings= (TextView) findViewById(R.id.date_time_settings);
        getValues();

    }

    @Override
    protected void getData() {

    }

    private void getValues() {
        dbSettingsHelper = new SettingsDB(this);
        dbPurposeHelper = new PurposesDB(this);
        dbDepartmentHelper = new DepartmentsDB(this);
        dbThemeValuesHelper = new ThemeValuesDB(this);
        mSettingDetails = new SettingDetailsResponse();
        purposesList = new ArrayList<>();
        departmentsList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        formattedDate = df.format(c.getTime());

        setUIdata();
    }

    @Override
    public void onClick(View v) {

    }

    private void setUIdata() {
        mSettingDetails = dbSettingsHelper.getSettingDetails();
        if(mSettingDetails.getDateTime()!=null){
            mDateTimeSettings.setText(mSettingDetails.getDateTime());
        }
        purposesList = dbPurposeHelper.getPurposeList();
        mPurposeGridAdapter = new PurposeGridAdapter(SettingsActivity.this, purposesList);
        departmentsList = dbDepartmentHelper.getDepartmentList();
        mDepartmentGridAdapter = new DepartmentGridAdapter(SettingsActivity.this, departmentsList);
        mPurposeDetails.setAdapter(mPurposeGridAdapter);
        mPurposeGridAdapter.notifyDataSetChanged();
        mDepartmentDetails.setAdapter(mDepartmentGridAdapter);
        mDepartmentGridAdapter.notifyDataSetChanged();
         //setListViewHeightBasedOnChildren(mPurposeDetails);
        //setGridViewHeightBasedOnChildrenForDepartment(mDepartmentDetails);
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_update_details: {
                if (DeviceManager.getInstance().getTokenId() != null && formattedDate!=null) {
                    updateSettingsServiceCall(DeviceManager.getInstance().getTokenId());
                } else {
                    AlertUtils.showInfoDialog(SettingsActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
                }
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    //------------------Service calling ---------------------------------

    private void updateSettingsServiceCall(final LicenseDetails licenseDetails) {
        startBlockRefreshing();
        final NetworkService networkService = ServiceManager.getInstance().createService(NetworkService.class);
        final Observable<SettingDetailsResponse> driverTripServiceObservable = networkService.getSettingDetailsByTokenId(licenseDetails.getToken());
        driverTripServiceObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SettingDetailsResponse>() {
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
                    public void onNext(SettingDetailsResponse settingDetailsResponse) {
                        if (settingDetailsResponse.getStatusCode() == 200) {
                            dbSettingsHelper.insertSettings(licenseDetails, settingDetailsResponse,formattedDate);
                            dbPurposeHelper.insertPurposes(settingDetailsResponse.getPurposes());
                            dbDepartmentHelper.insertDepartments(settingDetailsResponse.getDepartments());
                            dbThemeValuesHelper.insertTheme(settingDetailsResponse.getTheme());
                            //DeviceManager.getInstance().saveThemeValues(settingDetailsResponse);
                            setUIdata();
                        } else if (settingDetailsResponse.getStatusCode() == 401) {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(SettingsActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
                        } else {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(SettingsActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
                        }
                    }
                });
    }

    public static void setListViewHeightBasedOnChildren(GridView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight + listView.getColumnWidth() * (listAdapter.getCount() + listAdapter.getViewTypeCount()));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public static void setGridViewHeightBasedOnChildrenForPurpose(GridView gridview) {
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        if(listAdapter.getCount()%2==0){
            for (int i = 0; i < listAdapter.getCount()/2; i++) {
                View listItem = listAdapter.getView(i, null, gridview);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }else{
            for (int i = 0; i < (listAdapter.getCount()+1)/2; i++) {
                View listItem = listAdapter.getView(i, null, gridview);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = (totalHeight + gridview.getColumnWidth() * (listAdapter.getCount() + listAdapter.getViewTypeCount()));
        gridview.setLayoutParams(params);
        gridview.requestLayout();
    }

    public void actionBarRight2ButtonClicked() {
        popsetResetSettingPassword();

    }

    public void actionBarRightButtonClicked() {
        if (DeviceManager.getInstance().getTokenId() != null && formattedDate!=null) {
            updateSettingsServiceCall(DeviceManager.getInstance().getTokenId());
        } else {
            AlertUtils.showInfoDialog(SettingsActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
        }
    }

    public void popsetResetSettingPassword() {
        final Dialog securitySetDialog = new Dialog(SettingsActivity.this, R.style.NewDialog);
        securitySetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        securitySetDialog.setContentView(R.layout.security_popup);
        securitySetDialog.setCanceledOnTouchOutside(false);
        TextView mTitleTextview = (TextView) securitySetDialog.findViewById(R.id.popup_title_textview);
        final Button setPasswordButton = (Button) securitySetDialog.findViewById(R.id.submit_button_for_security);
        mTitleTextview.setText(getResources().getString(R.string.reset_pass));
        final EditText mPassword = (EditText) securitySetDialog.findViewById(R.id.mPassword);
        ImageView closepopup = (ImageView) securitySetDialog.findViewById(R.id.close_popup_security);
        final EditText mReEnterPassword = (EditText) securitySetDialog.findViewById(R.id.re_enter_password);
        setPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPassword.getText() != null && mPassword.getText().length() >= 4 && mReEnterPassword.getText() != null && mReEnterPassword.getText().length() >= 4) {
                    if (mReEnterPassword.getText().toString().equals(mPassword.getText().toString())) {
                        DeviceManager.getInstance().saveSettingPassword(mPassword.getText().toString());
                        securitySetDialog.dismiss();
                    } else {
                        AlertUtils.showInfoDialog(SettingsActivity.this, getString(R.string.app_name), getString(R.string.pasword_mismatch));
                    }
                } else {
                    AlertUtils.showInfoDialog(SettingsActivity.this, getString(R.string.app_name), getString(R.string.set_min_password));
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

}

