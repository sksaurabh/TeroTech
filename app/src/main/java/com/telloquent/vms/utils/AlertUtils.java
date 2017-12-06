package com.telloquent.vms.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.telloquent.vms.R;
import com.telloquent.vms.base.IAlertDialogCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Telloquent-DM6M on 9/6/2017.
 */
@SuppressLint({"NewApi"})
public class AlertUtils {

    public static void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showDebugToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showInfoDialog(final Context context, String title, String msg) {
        // get prompts.xml view
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomAlertDialogStyle));
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.text_ok, null);
        builder.show();
    }

    public static void showCancellableDialog(Context context, String title, String msg, String positiveButtonText,
                                             String negativeButtonText, final IAlertDialogCallback alertDialogCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomAlertDialogStyle));
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        if (!StringUtil.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialogCallback.positiveButtonClicked(dialog, which);
                }
            });
        }
        if (!StringUtil.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialogCallback.cancelButtonClicked(dialog, which);
                }
            });
        }
        builder.show();
    }

    public static void showYesNoDialog(Context context, String title, String msg, String positiveButtonText,
                                       String negativeButtonText, final IAlertDialogCallback alertDialogCallback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomAlertDialogStyle));
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        if (!StringUtil.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialogCallback.positiveButtonClicked(dialog, which);
                }
            });
        }
        if (!StringUtil.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialogCallback.negativeButtonClicked(dialog, which);
                }
            });
        }
        builder.show();
    }

    public static void showYesNoDialogWithCustomView(Context context, View view, String title, String positiveButtonText,
                                                     String negativeButtonText, final IAlertDialogCallback alertDialogCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomAlertDialogStyle));
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setView(view);
        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialogCallback.positiveButtonClicked(dialog, which);
            }
        });
        builder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogCallback.negativeButtonClicked(dialog, which);
            }
        });
        builder.show();
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (phone.length() != 10)
            return false;
        else {
            Pattern pattern = Pattern.compile(ApplicationConstants.PHONE_PATTERN);
            Matcher matcher = pattern.matcher(phone);
            return matcher.matches();
            //return  true;
        }
    }

    public static boolean isAlphaNumeric(String key) {
        if (key.length() <= 6)
            return false;
        else {
            Pattern keyval = Pattern.compile(ApplicationConstants.key_pattern);
            Matcher matcher = keyval.matcher(key);
            return matcher.matches();
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


}
