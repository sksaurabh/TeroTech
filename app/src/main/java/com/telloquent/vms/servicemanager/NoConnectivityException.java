package com.telloquent.vms.servicemanager;

import java.io.IOException;

/**
 * Created by Telloquent-DM6M on 9/8/2017.
 */

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No network available, please check your WiFi or Data connection";
    }
}
