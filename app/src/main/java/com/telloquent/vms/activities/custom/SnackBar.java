package com.telloquent.vms.activities.custom;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.telloquent.vms.R;

/**
 * Created by Telloquent-DM6M on 9/5/2017.
 */

public class SnackBar {
    public static final int LENGTH_SHORT = Snackbar.LENGTH_SHORT;
    public static final int LENGTH_LARGE = Snackbar.LENGTH_LONG;
    public static Snackbar makeText(Context context, int resId, int duration) {
        Activity activity = (Activity) context;
        View layout;
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), context.getResources().getText(resId), duration);
        layout = snackbar.getView();
        layout.setBackgroundColor(context.getResources().getColor(R.color.button_active));
        android.widget.TextView text = (android.widget.TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(context.getResources().getColor(R.color.white));
        text.setAllCaps(true);

        /* ------------------ if user want to use custom font----------------------------------*/
        /* Typeface font = null;
        if (readLocale(context).equals("pa")) {
            font = Typeface.createFromAsset(context.getAssets(), "Gotham-Book.otf");
        } else {
            font = Typeface.createFromAsset(context.getAssets(), "Gotham-Book.otf");
        }
        text.setTypeface(font);*/
        return snackbar;

    }

    public static Snackbar makeText(Context context, String message, int duration) {
        Activity activity = (Activity) context;
        View layout;
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), message, duration);
        layout = snackbar.getView();
        layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        android.widget.TextView text = (android.widget.TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(context.getResources().getColor(R.color.white));


        /* ------------------ if user want to use custom font----------------------------------*/
        /*Typeface font = null;
        if (readLocale(context).equals("pa")) {
            font = Typeface.createFromAsset(context.getAssets(), "Gotham-Book.otf");
        } else {
            font = Typeface.createFromAsset(context.getAssets(), "Gotham-Book.otf");
        }
        text.setTypeface(font);*/

        return snackbar;

    }

    public static Snackbar makeText(Context context, View view, String message, int duration) {
        View layout;
        Snackbar snackbar = Snackbar
                .make(view, message, duration);
        layout = snackbar.getView();
        layout.setBackgroundColor(context.getResources().getColor(R.color.button_active));
        android.widget.TextView text = (android.widget.TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(context.getResources().getColor(R.color.white));

      /* ------------------ if user want to use custom font----------------------------------*/


       /* Typeface font = null;
        if (readLocale(context).equals("pa")) {
            font = Typeface.createFromAsset(context.getAssets(), "Gotham-Book.otf");
        } else {
            font = Typeface.createFromAsset(context.getAssets(), "Gotham-Book.otf");
        }
        text.setTypeface(font);*/
        return snackbar;

    }

    public static Snackbar makeText(Context context, View view, int resId, int duration) {
        View layout;
        Snackbar snackbar = Snackbar
                .make(view, context.getResources().getText(resId), duration);
        layout = snackbar.getView();
        layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        android.widget.TextView text = (android.widget.TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(context.getResources().getColor(R.color.white));

        /* ------------------ if user want to use custom font----------------------------------*/
       /* Typeface font = null;
        if (readLocale(context).equals("pa")) {
            font = Typeface.createFromAsset(context.getAssets(), "Gotham-Book.otf");
        } else {
            font = Typeface.createFromAsset(context.getAssets(), "Gotham-Book.otf");
        }
        text.setTypeface(font);*/
        return snackbar;

    }

 /*   private static String readLocale(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences("Locale", mContext.MODE_PRIVATE);
        return sp.getString("Language", "en");
    }*/
}

