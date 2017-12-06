package com.telloquent.vms.base;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.WindowManager;
import com.telloquent.vms.R;
import com.telloquent.vms.activities.custom.ATProgressDialog;
import com.telloquent.vms.activities.custom.OperationStatus;
import com.telloquent.vms.utils.HTTPUtility;
import com.telloquent.vms.utils.ListUtil;
import java.util.List;

/**
 * Created by developer on 5/31/17.
 */

public abstract class BaseDisplayActivity extends BaseActivity {
    private static final String TAG = BaseDisplayActivity.class.getCanonicalName();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ATProgressDialog mCircularProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void setContentView(int id) {
        super.setContentView(id);
      /*  mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(swipeRefreshId());
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.blue_color, R.color.blue_color, R.color.blue_color, R.color.blue_color);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshActivity();
                }
            });
        }*/
        initializeView();

    }


    protected abstract void initializeView();

   /* private int swipeRefreshId() {
        return R.id.swipeRefreshLayout;
    }*/

    public void refreshActivity(){

    }
/*
    public SwipeRefreshLayout getBusyIndicator(){
        if (mSwipeRefreshLayout == null) throw new RuntimeException("You need to make sure you have the swipe refresh implemented in the layout file");
        return mSwipeRefreshLayout;
    }*/

    public void startBlockRefreshing(){
        if (mCircularProgressView == null && !isFinishing()) {
            mCircularProgressView = new ATProgressDialog(this, true);
            mCircularProgressView.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mCircularProgressView.setTitle("please Wait....");
        }
        mCircularProgressView.show();
    }

    public void stopBlockRefreshing(){
        if (mCircularProgressView == null) throw new RuntimeException("You need to make sure you call startBlockRefreshing first");
        if (mCircularProgressView.isShowing() && !isFinishing()) {
            mCircularProgressView.dismiss();
        }
    }

    /*public void startRefreshing() {
        getBusyIndicator().setEnabled(true);
        getBusyIndicator().setRefreshing(true);
    }*/

   /* public void stopRefreshing() {
        getBusyIndicator().setRefreshing(false);
    }*/

    protected abstract void getData();


    public boolean isListResponseOk(OperationStatus operationStatus, List details) {
        if (HTTPUtility.isSuccess(operationStatus)) {
            if (!ListUtil.isEmpty(details)) {
                return true;
            } else {
                showSnackMessage(getString(R.string.no_rows));
            }
        } else {
            showSnackMessage(operationStatus.getMessages().get(0));
        }
        return false;
    }

}
