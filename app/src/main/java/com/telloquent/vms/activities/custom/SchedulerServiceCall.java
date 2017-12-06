package com.telloquent.vms.activities.custom;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;
import com.telloquent.vms.R;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.servicemanager.NetworkService;
import com.telloquent.vms.servicemanager.ServiceManager;
import com.telloquent.vms.support.DepartmentsDB;
import com.telloquent.vms.support.PurposesDB;
import com.telloquent.vms.support.SettingsDB;
import com.telloquent.vms.support.ThemeValuesDB;
import com.telloquent.vms.utils.AlertUtils;
import com.telloquent.vms.utils.ApplicationConstants;
import com.telloquent.vms.utils.DeviceManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Telloquent-DM6M on 9/20/2017.
 */

public class SchedulerServiceCall extends Service {
    private SettingsDB dbSettingsHelper;
    private PurposesDB dbPurposeHelper;
    private DepartmentsDB dbDepartmentHelper;
    private ThemeValuesDB dbThemeValuesHelper;
    SimpleDateFormat df = ApplicationConstants.mDateTimeFormat;
    private String formattedDate=null;
    private TextView mDateTimeSettings;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_LONG).show();
        super.onCreate();
        initView();
    }

    private void initView() {
        dbSettingsHelper = new SettingsDB(this);
        dbPurposeHelper = new PurposesDB(this);
        dbDepartmentHelper = new DepartmentsDB(this);
        dbThemeValuesHelper = new ThemeValuesDB(this);
        Calendar c = Calendar.getInstance();
        formattedDate = df.format(c.getTime());




    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Service Destroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "Service Running ", Toast.LENGTH_LONG).show();
        if (DeviceManager.getInstance().getTokenId() != null&& formattedDate!=null) {
            updateSettingsServiceCall(DeviceManager.getInstance().getTokenId());
        }else{
            AlertUtils.showInfoDialog(SchedulerServiceCall.this, getString(R.string.app_name), getString(R.string.something_wrong));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateSettingsServiceCall(final LicenseDetails licenseDetails) {
        try {
            final NetworkService networkService = ServiceManager.getInstance().createService(NetworkService.class);
            final Observable<SettingDetailsResponse> driverTripServiceObservable = networkService.getSettingDetailsByTokenId(licenseDetails.getToken());
            driverTripServiceObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SettingDetailsResponse>() {
                        @Override
                        public void onCompleted() {
                            Toast.makeText(getApplicationContext(), "Service completed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getApplicationContext(), "Service error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(SettingDetailsResponse settingDetailsResponse) {
                            if (settingDetailsResponse.getStatusCode() == 200) {
                                settingDetailsResponse.setDateTime(formattedDate);
                                dbSettingsHelper.insertSettings(licenseDetails, settingDetailsResponse,formattedDate);
                                dbPurposeHelper.insertPurposes(settingDetailsResponse.getPurposes());
                                dbDepartmentHelper.insertDepartments(settingDetailsResponse.getDepartments());
                                dbThemeValuesHelper.insertTheme(settingDetailsResponse.getTheme());
                            } else if (settingDetailsResponse.getStatusCode() == 401) {
                                AlertUtils.showInfoDialog(SchedulerServiceCall.this, getString(R.string.app_name), settingDetailsResponse.getMessage());

                            } else {
                                AlertUtils.showInfoDialog(SchedulerServiceCall.this, getString(R.string.app_name), settingDetailsResponse.getMessage());
                            }
                        }
                    });

        } catch (Exception e) {
            AlertUtils.showInfoDialog(SchedulerServiceCall.this, getString(R.string.app_name), getString(R.string.something_wrong));
        }
    }
}
