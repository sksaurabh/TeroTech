package com.telloquent.vms.base;

import android.content.DialogInterface;

/**
 * Created by Telloquent-DM6M on 9/6/2017.
 */

public interface IAlertDialogCallback {
    void positiveButtonClicked(DialogInterface dialog, int which);
    void cancelButtonClicked(DialogInterface dialog, int which);
    void negativeButtonClicked(DialogInterface dialog, int which);
}

