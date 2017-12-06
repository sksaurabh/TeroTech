package com.telloquent.vms.activities.custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Telloquent-DM6M on 10/31/2017.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, ReopenAppService.class);
        context.startService(service);
    }
}
