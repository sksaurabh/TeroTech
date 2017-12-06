package com.telloquent.vms.activities.custom;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.telloquent.vms.R;

/**
 * Created by Telloquent-DM6M on 9/4/2017.
 */

public class ATProgressDialog extends Dialog {


    protected static final float DIM_AMOUNT = 0.40f;

    protected boolean m_modal = false;

    public ATProgressDialog(Context context, boolean modal) {
        super(context);
        m_modal = modal;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.progress_dialog);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(getContext().getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Dialog may or may not be modal, but never cancelable
        this.setTitle("Please wait ...");
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }


    protected void onStart() {
        super.onStart();

        Window window = this.getWindow();

        if (m_modal) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.dimAmount = DIM_AMOUNT;
            window.setAttributes(layoutParams);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        } else {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }




}
