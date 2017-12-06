package com.telloquent.vms.servicemanager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.utils.StringUtil;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Telloquent-DM6M on 9/8/2017.
 */

public class ServiceManager {
    private final static String TAG = ServiceManager.class.getName();
    private String BASE_URL;
    private Retrofit.Builder builder;
    private Context context;
    public final static String kWebServicesVersion = "";
    private Handler mMainHandler;
    private String appVersion;
    private LicenseDetails licenseDetails;
    private SettingDetailsResponse settingDetailsResponse;
    String mPassword;
    private String licensekey;

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    private Retrofit.Builder getRetrofitBuilder() {
        if (builder == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okClient()
                            .addInterceptor(loggingInterceptor)
                            .addInterceptor(new ConnectivityInterceptor(context))
                            .build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()); // Json to Object

        }
        return builder;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public <S> S createService(final Class<S> serviceClass) {
        return getRetrofitBuilder().client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = requestBuild(chain.request()).build();
                Response response = chain.proceed(request);
                if (response.isSuccessful()) {
                    return response;
                } else {
                    Log.d(TAG, "Basic response was not successful");
                    return response;
                }
            }
        }).build()).build().create(serviceClass);
    }

    private static ServiceManager instance = null;

    protected ServiceManager() {

    }

    public void executeOnMainThread(Runnable runnable) {
        if (mMainHandler == null) {
            mMainHandler = new Handler(getContext().getMainLooper());
        }
        mMainHandler.post(runnable);
    }

    /**
     * Provides reference to the single instance so of {@link ServiceManager}.
     *
     * @return reference to service manager.
     */
    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    private Request.Builder requestBuild(Request request) {
        if (StringUtil.isEmpty(appVersion)) {
            throw new RuntimeException("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!App version needs to be set!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        Log.d(TAG, request.url().toString());
        return request.newBuilder()
                .header("Accept", "application/json")
                .method(request.method(), request.body());
    }

    private static OkHttpClient.Builder okClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.MINUTES);
    }


    public void setTokenId(LicenseDetails licenseDetails) {
        this.licenseDetails = licenseDetails;
    }

    public void setThemeValues(SettingDetailsResponse settingDetailsResponse) {
        this.settingDetailsResponse = settingDetailsResponse;
    }
    public void setPassword(String pass) {
        this.mPassword = pass;
    }

    public void saveLicenseKey(String licensekey) {
        this.licensekey = licensekey;
    }
}