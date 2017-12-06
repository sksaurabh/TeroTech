package com.telloquent.vms.servicemanager;

import android.content.Context;
import com.telloquent.vms.utils.ConnectionUtil;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Telloquent-DM6M on 9/8/2017.
 */

public class ConnectivityInterceptor implements Interceptor {
    final String TAG = ConnectivityInterceptor.class.getName();
    private Context context;
    public ConnectivityInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        boolean isConnected = ConnectionUtil.isConnected(context);

        if (!isConnected) {
            throw new NoConnectivityException();
        }
        else {
            Response response = chain.proceed(chain.request());
            return response;
        }
    }
}