package com.telloquent.vms.utils;

import com.telloquent.vms.activities.custom.OperationStatus;

/**
 * Created by Telloquent-DM6M on 9/21/2017.
 */

public class HTTPUtility {
    public HTTPUtility() {
    }

    public static Boolean isSuccess(OperationStatus operationStatus) {
        return operationStatus.getStatusCode().equalsIgnoreCase("200")?Boolean.valueOf(true):Boolean.valueOf(false);
    }
}
