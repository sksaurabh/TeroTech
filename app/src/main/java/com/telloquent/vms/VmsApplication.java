package com.telloquent.vms;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.servicemanager.ServiceManager;
import com.telloquent.vms.support.SettingsDB;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.BaseStorage;
import com.telloquent.vms.utils.DeviceManager;
import java.net.UnknownHostException;
import java.util.Map;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

/**
 * Created by Telloquent-DM6M on 9/8/2017.
 */

public class VmsApplication extends MultiDexApplication {
    private final static String TAG = VmsApplication.class.getName();
    private static VmsApplication mInstance;
    private Map<String, BaseStorage> mApplicationStorageMap;
    LicenseDetails licenseDetails;
    private SettingsDB dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpPortraitMode();
        mInstance = this;
        dbHelper = new SettingsDB(mInstance);
        setupServiceManager();
    }

    private void setupServiceManager() {
        final ServiceManager serviceManager = ServiceManager.getInstance();
        serviceManager.setContext(this);
        serviceManager.setBaseUrl(getString(R.string.serverURL));
        serviceManager.setAppVersion("1.0.0");
        licenseDetails = dbHelper.getTokenKeyDetail();

            if (DeviceManager.getInstance().getTokenId() == null) {
            if (licenseDetails != null &&licenseDetails.getToken().length()!=0) {
                ApplicationStorage.getInstance().saveToken(licenseDetails);
                serviceManager.setTokenId(licenseDetails);
            }
        }
        final RxJavaErrorHandler rxJavaErrorHandler = new RxJavaErrorHandler() {
            @Override
            public void handleError(final Throwable x) {
                if (x instanceof UnknownHostException) {
                    Log.d(TAG, "No internet rxJavaErrorHandler.handleError: " + x.getClass().getSimpleName());

                } else if (x instanceof IllegalStateException) {
                    Log.d(TAG, "Unauthorized: " + x.getClass().getSimpleName());

                } else {
                    Log.d(TAG, "Unknown Exception: " + x.getClass().getSimpleName());
                }
            }
        };
        RxJavaPlugins.getInstance().registerErrorHandler(rxJavaErrorHandler);
    }

    public static synchronized VmsApplication getApplication() {
        return mInstance;
    }

    private void setUpPortraitMode() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }

        });
    }


    public synchronized void setProperty(String tag, BaseStorage instance) {
        mApplicationStorageMap.put(tag, instance);
    }
}
