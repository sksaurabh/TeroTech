package com.telloquent.vms.activities.custom;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.journeyapps.barcodescanner.CaptureActivity;


public class CaptureActivityLandscape extends CaptureActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onBackPressed() {
       finish();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);
    }

}
