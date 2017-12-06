package com.telloquent.vms.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.telloquent.vms.R;
import com.telloquent.vms.activities.custom.SnackBar;
import com.telloquent.vms.model.custom.ResponseError;
import com.telloquent.vms.model.settingsmodel.Theme;
import com.telloquent.vms.support.ThemeValuesDB;
import com.telloquent.vms.utils.AlertUtils;
import com.telloquent.vms.utils.BuildConfiguration;
import com.telloquent.vms.utils.ConnectionUtil;
import com.telloquent.vms.utils.StringUtil;

import org.w3c.dom.Text;

import java.net.UnknownHostException;
import java.util.List;

import retrofit2.HttpException;


public class BaseActivity extends AppCompatActivity {
    private Boolean activityFinishFlag;
    public boolean shouldDiscardChanges = false;
    private int busyCounter = 0;
    private final static String TAG = BaseActivity.class.getName();
    private View mNewRequestView;
    protected String mTripKey;
    TextView mTimerTextView;
    Toolbar toolbar;
    private List<Theme> themes;
    private ThemeValuesDB dbThemeValuesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void setupActionBar(String title, final ActionBarActivityLeftAction leftAction, final ActionBarActivityRightAction rightAction, final ActionBarActivityRight2Action right2Action) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.framework_action_bar_view, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        setActivityTitle(title);
        setToolbarRightIcon2(right2Action);


        final ImageView leftIcon = ((ImageView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_leftIcon));
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarLeftButtonClicked();
                if (activityFinishFlag && !shouldDiscardChanges) {
                    finish();
                } else if (shouldDiscardChanges) {
                    if (shouldDiscardChanges) {
                        showDiscardAlert();
                    }
                } else if (leftAction == ActionBarActivityLeftAction.WELCOME) {
                    //actionBarLeftButtonClicked();
                }
            }
        });
        leftIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    TypedValue outValue = new TypedValue();
                    getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
                    ((ImageView) v).setBackgroundResource(outValue.resourceId);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ((ImageView) v).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                return false;
            }
        });

        final ImageView rightIcon = ((ImageView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_rightIcon));
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightAction == ActionBarActivityRightAction.ACTION_DELETE) {
                    actionBarRightButtonClicked();
                } else if (rightAction == ActionBarActivityRightAction.ACTION_UPDATE_BUTTON) {
                    actionBarRightButtonClicked();
                } else if (rightAction == ActionBarActivityRightAction.ACTION_QUESTION) {
                    actionBarRightButtonClicked();
                } else if (rightAction == ActionBarActivityRightAction.ACTION_CLOSE) {
                    actionBarRightButtonClicked();
                    if (activityFinishFlag && !shouldDiscardChanges) {
                        finish();
                    } else if (shouldDiscardChanges) {
                        if (shouldDiscardChanges) {
                            showDiscardAlert();
                        }
                    }
                }
            }
        });
        TextView leftText=((TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_left_title));

        TextView rightText = ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_rightText));
        /*Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Lato-Regular.ttf");
        rightText.setTypeface(custom_font);*/

        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (right2Action == ActionBarActivityRight2Action.ACTION_DONE) {
                    actionBarRight2TextClicked();
                }

            }
        });

        rightIcon.setVisibility(View.GONE);
        leftIcon.setVisibility(View.GONE);
        leftText.setVisibility(View.GONE);
        switch (leftAction) {
            case ACTION_BACK:
                leftIcon.setImageResource(R.drawable.keyboard_back_icon);
                leftIcon.setVisibility(View.VISIBLE);
                activityFinishFlag = true;
                break;
            case ACTION_CLOSE:
                leftIcon.setVisibility(View.VISIBLE);
                leftIcon.setImageResource(R.drawable.close_icon);
                activityFinishFlag = true;
                break;
            case ACTION_NONE:
                activityFinishFlag = false;
                break;
            case WELCOME:
                leftText.setVisibility(View.VISIBLE);
                leftText.setText("Welcome!");
                leftText.setTextColor(getResources().getColor(R.color.yellow_color));
                break;
        }
        switch (rightAction) {
            case ACTION_NONE:
                break;
            case ACTION_DELETE:
                rightIcon.setVisibility(View.VISIBLE);
                rightIcon.setImageResource(R.drawable.delete_icon);
                break;
            case ACTION_UPDATE_BUTTON:
                rightIcon.setVisibility(View.VISIBLE);
                rightIcon.setImageResource(R.drawable.update_arrow);
                break;
            case ACTION_QUESTION:
                rightIcon.setVisibility(View.VISIBLE);
                rightIcon.setImageResource(R.drawable.ic_help_icon);
                break;
            case ACTION_CLOSE:
                rightIcon.setVisibility(View.VISIBLE);
                rightIcon.setImageResource(R.drawable.close_icon);
                activityFinishFlag = true;
        }


      /*  if (DeviceManager.getInstance().getThemeValues() != null) {
            settingDetailsResponse = new SettingDetailsResponse();
            settingDetailsResponse = DeviceManager.getInstance().getThemeValues();
            if (settingDetailsResponse != null && settingDetailsResponse.getTheme().size() != 0) {
                for (int i = 0; i < settingDetailsResponse.getTheme().size(); i++) {
                    String identifire = settingDetailsResponse.getTheme().get(i).getIdentifier();
                    if (identifire.contains("toolbar")) {
                        toolbar.setBackgroundColor(Color.parseColor(settingDetailsResponse.getTheme().get(i).getValue().getBackgroundColor()));
                    }
                }
            }
        }*/
        dbThemeValuesHelper = new ThemeValuesDB(this);
        //setThemeValues();
    }


    private void setThemeValues() {
        try {
            themes = dbThemeValuesHelper.getThemeList();
            if (themes.size() != 0) {
                for (int i = 0; i < themes.size(); i++) {
                    String identifire = themes.get(i).getIdentifier();
                    if (identifire.contains("toolbar")) {
                        toolbar.setBackgroundColor(Color.parseColor(themes.get(i).getValue().getBackgroundColor()));
                    }
                }
            }
        } catch (Exception e) {
            // AlertUtils.showInfoDialog(BaseActivity.this, getString(R.string.app_name), getString(R.string.invalid_theme));
        }

    }


    protected void setActivityTitle(String title) {
        final TextView activityTitle = ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title));
        /*Typeface custom_title_font = Typeface.createFromAsset(getAssets(),  "fonts/Lato-Regular.ttf");
        activityTitle.setTypeface(custom_title_font);*/
        activityTitle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        activityTitle.setText(title);
    }

    protected void setToolbarRightIcon2(final ActionBarActivityRight2Action right2Action) {
        final ImageView rightIcon2 = ((ImageView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_rightIcon2));
        rightIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (right2Action == ActionBarActivityRight2Action.COMPANY_LOGO) {

                } else if (right2Action == ActionBarActivityRight2Action.ACTION_RESET) {
                    actionBarRight2ButtonClicked();
                } else if (right2Action == ActionBarActivityRight2Action.ACTION_EDIT) {
                    actionBarRight2ButtonClicked();

                }
            }
        });
        rightIcon2.setVisibility(View.INVISIBLE);
        switch (right2Action) {
            case ACTION_NONE:
                break;
            case ACTION_EDIT:
                rightIcon2.setVisibility(View.VISIBLE);
                rightIcon2.setImageResource(R.drawable.edit_icon);
                break;
            case ACTION_DONE:
                rightIcon2.setVisibility(View.VISIBLE);
                rightIcon2.setImageResource(R.drawable.company_logo);
                break;
            case ACTION_RESET:
                rightIcon2.setVisibility(View.VISIBLE);
                rightIcon2.setImageResource(R.drawable.ic_reset_pass);
                break;
            case COMPANY_LOGO:
                rightIcon2.setVisibility(View.VISIBLE);
                rightIcon2.setImageResource(R.drawable.company_logo);
                break;

        }

    }

    public void showToolbar(boolean show) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        if (show) {
            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        } else {
            toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        }
    }

    public void actionBarLeftButtonClicked() {
        //Toast.makeText(this, "LeftIcon Clicked", Toast.LENGTH_SHORT).show();
    }

    public void actionBarRightButtonClicked() {
        //Toast.makeText(this, "RightIcon Clicked", Toast.LENGTH_SHORT).show();
    }

    public void actionBarRight2ButtonClicked() {
        //Toast.makeText(this, "Clicked Right2Icon", Toast.LENGTH_SHORT).show();
    }

    public void actionBarRight2TextClicked() {
        //Toast.makeText(this, "Clicked Right2Text", Toast.LENGTH_SHORT).show();
    }

    public void doLogoutWithAlert(Boolean withAlert) {

    }

    public enum ActionBarActivityLeftAction {
        ACTION_NONE,
        ACTION_CLOSE,
        ACTION_BACK,
        WELCOME
    }

    public enum ActionBarActivityRightAction {
        ACTION_NONE,
        ACTION_DELETE,
        ACTION_UPDATE_BUTTON,
        ACTION_QUESTION,
        ACTION_CLOSE
    }

    public enum ActionBarActivityRight2Action {
        ACTION_NONE,
        ACTION_DONE,
        ACTION_EDIT,
        ACTION_RESET,
        COMPANY_LOGO,
    }

    @Override
    public void onBackPressed() {

    }

    void showDiscardAlert() {
        AlertUtils.showYesNoDialog(this, "Alert!", getString(R.string.discard_title),
                getString(R.string.yes), getString(R.string.no), new IAlertDialogCallback() {
                    @Override
                    public void positiveButtonClicked(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();

                    }

                    @Override
                    public void cancelButtonClicked(DialogInterface dialog, int which) {
                    }

                    @Override
                    public void negativeButtonClicked(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public void showTextHideRecycler(RecyclerView recyclerView, TextView textView) {
        recyclerView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    public void showDevelopmentToastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void handleError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            okhttp3.ResponseBody responseBody = httpException.response().errorBody();
            try {
                String responseString = responseBody.string();
                ResponseError responseError = new Gson().fromJson(responseString, ResponseError.class);
                if (!StringUtil.isEmpty(responseError.getError_description()) && responseError.getError_description().equalsIgnoreCase("bad credentials")) {
                    AlertUtils.showInfoDialog(this, getString(R.string.app_name), getString(R.string.invalid_token));
                } else {
                    showDevMessage(new Exception(responseError.getMessage()));
                }
            } catch (Exception e1) {
                showDevMessage(e1);
                e1.printStackTrace();
            }
        } else if (e instanceof UnknownHostException) {
            if (!ConnectionUtil.isConnected(this)) {
                AlertUtils.showInfoDialog(this, getString(R.string.app_name), getString(R.string.please_check_internet));
            } else {
                showDevMessage(e);
            }
        } else {
            showDevMessage(e);
        }
    }

    private void showDevMessage(Throwable e) {
        if (!BuildConfiguration.IS_PROD_BUILD) {
            AlertUtils.showInfoDialog(this, getString(R.string.app_name), e.getMessage());
        } else {
            AlertUtils.showInfoDialog(this, getString(R.string.app_name), getString(R.string.something_wrong));
        }
    }


//------------------------hiding the keypad ontouch of outside----------------------------------------

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void showSnackMessage(@NonNull String message) {
        SnackBar.makeText(this, message, SnackBar.LENGTH_LARGE).show();
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


