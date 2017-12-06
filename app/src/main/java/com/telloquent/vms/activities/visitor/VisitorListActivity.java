package com.telloquent.vms.activities.visitor;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.telloquent.vms.R;
import com.telloquent.vms.adapter.VisitorListAdapter;
import com.telloquent.vms.base.BaseActivity;
import com.telloquent.vms.base.BaseDisplayActivity;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.visitorlist.VisitiorListResponse;
import com.telloquent.vms.servicemanager.NetworkService;
import com.telloquent.vms.servicemanager.ServiceManager;
import com.telloquent.vms.utils.AlertUtils;
import com.telloquent.vms.utils.ApplicationConstants;
import com.telloquent.vms.utils.DeviceManager;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VisitorListActivity extends BaseDisplayActivity {
    private VisitorListAdapter mVisitorListAdapter;
    private RecyclerView mVisitorListRecyclerview;
    private LicenseDetails mLicenseDetails;
    private static String mVrfId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_list);
    }

    @Override
    protected void initializeView() {
        setupActionBar(getResources().getString(R.string.all_visitors), BaseActivity.ActionBarActivityLeftAction.ACTION_BACK, BaseActivity.ActionBarActivityRightAction.ACTION_NONE, BaseActivity.ActionBarActivityRight2Action.ACTION_NONE);
        mVisitorListRecyclerview = (RecyclerView) findViewById(R.id.visitor_list_recyclerView);
        RecyclerView.LayoutManager purposesLayoutManager = new LinearLayoutManager(this);
        mVisitorListRecyclerview.setLayoutManager(purposesLayoutManager);
        mVisitorListRecyclerview.setItemAnimator(new DefaultItemAnimator());
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mVrfId = bundle.getString(ApplicationConstants.kvrfID);
        }
        getData();
    }
    @Override
    public void getData() {
        if (DeviceManager.getInstance().getTokenId() != null && mVrfId != null) {
            mLicenseDetails = DeviceManager.getInstance().getTokenId();
            getVisitorList(mLicenseDetails, mVrfId);
        } else {
            AlertUtils.showInfoDialog(VisitorListActivity.this, getString(R.string.app_name), getString(R.string.token_id_or_vrf));
        }
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();
        finish();*/
    }

    private void getVisitorList(final LicenseDetails licenseDetails, String vrfID) {
        startBlockRefreshing();
        final NetworkService networkService = ServiceManager.getInstance().createService(NetworkService.class);
        final Observable<VisitiorListResponse> visitorListObservable = networkService.viewVisitorsList(licenseDetails.getToken(), vrfID);
        visitorListObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VisitiorListResponse>() {
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
                    public void onNext(VisitiorListResponse visitiorListResponse) {
                        if (visitiorListResponse.getStatusCode() == 200) {
                            mVisitorListAdapter = new VisitorListAdapter(VisitorListActivity.this, visitiorListResponse.getVisitors());
                            mVisitorListRecyclerview.setAdapter(mVisitorListAdapter);
                            mVisitorListAdapter.notifyDataSetChanged();
                        } else if (visitiorListResponse.getStatusCode() == 401) {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(VisitorListActivity.this, getString(R.string.app_name), visitiorListResponse.getMessage());
                        } else {
                            stopBlockRefreshing();
                            AlertUtils.showInfoDialog(VisitorListActivity.this, getString(R.string.app_name), visitiorListResponse.getMessage());
                        }
                    }
                });
          }

    public static Intent createIntentWithBundle(Context context, @NonNull String vrfID) {
        Intent intent = new Intent(context, VisitorListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ApplicationConstants.kvrfID, vrfID);
        intent.putExtras(bundle);
        return intent;
    }
  /*  public static Intent createIntentWithBundleauthenticate(Context context, @NonNull String vrfID) {
        Intent intent = new Intent(context, VisitorListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ApplicationConstants.kvrfID, vrfID);
        intent.putExtras(bundle);
        return intent;
    }*/
}
