package com.telloquent.vms;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.telloquent.vms.activities.home.DashBoardActivity;
import com.telloquent.vms.activities.home.LicenseKeyActivity;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.model.settingsmodel.Theme;
import com.telloquent.vms.support.SettingsDB;
import com.telloquent.vms.support.ThemeValuesDB;
import com.telloquent.vms.utils.ApplicationStorage;
import com.telloquent.vms.utils.DeviceManager;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {
    private final static String TAG = ApplicationStorage.class.getCanonicalName();
    //splashscreen timeout
    private static int SPLASH_TIME_OUT = 2000;
    Context context;
    //handler for splashscreen
    Handler mHandler;
    Runnable mLaunchRunnable;
    private ImageView mLogoUrl;
    SettingDetailsResponse settingDetails = new SettingDetailsResponse();
    private SettingsDB dbHelper;
    private LinearLayout mSplashBackground;
    private List<Theme> themes;
    private ThemeValuesDB dbThemeValuesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //startService(new Intent(getBaseContext(), ReopenAppService.class));
        initializeView();

    }

    public void initializeView() {
        dbThemeValuesHelper=new ThemeValuesDB(this);
        setThemeValues();
        dbHelper = new SettingsDB(this);
        mLogoUrl = (ImageView) findViewById(R.id.vms_Logo);
        settingDetails = dbHelper.getSettingDetails();
        if (settingDetails != null) {
            Picasso.with(SplashScreenActivity.this)
                    .load(settingDetails.getLogoUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.trava_logo_white)
                    .error(R.drawable.trava_logo_white)
                    .into(mLogoUrl, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(SplashScreenActivity.this)
                                    .load(settingDetails.getLogoUrl())
                                    .error(R.drawable.trava_logo_white)
                                    .into(mLogoUrl, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError() {
                                            Log.v(TAG, "Could not fetch image");
                                        }
                                    });
                        }
                    });
        }
        mHandler = new Handler();
        mLaunchRunnable = new Runnable() {
            @Override
            public void run() {
                if (DeviceManager.getInstance().getTokenId() != null) {
                    Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LicenseKeyActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    private void setThemeValues() {
        try {
            themes = dbThemeValuesHelper.getThemeList();
            if (themes.size() != 0) {
                for (int i = 0; i < themes.size(); i++) {
                    String identifire = themes.get(i).getIdentifier();
                    if (identifire.contains("background")) {
                        mSplashBackground.setBackgroundColor(Color.parseColor(themes.get(i).getValue().getBackgroundColor()));
                    }
                }
            }
        } catch (Exception e) {
           // AlertUtils.showInfoDialog(SplashScreenActivity.this, getString(R.string.app_name), getString(R.string.invalid_theme));
        }
    }


    @Override
    protected void onPause() {
        mHandler.removeCallbacks(mLaunchRunnable);
        super.onPause();
    }

    @Override
    protected void onResume() {
        mHandler.postDelayed(mLaunchRunnable, SPLASH_TIME_OUT);
        super.onResume();
    }
}