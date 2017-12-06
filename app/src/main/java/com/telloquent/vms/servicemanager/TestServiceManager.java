package com.telloquent.vms.servicemanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import java.security.cert.CertificateException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Telloquent-DM6M on 9/14/2017.
 */

public class TestServiceManager {
        public static final String DOMAIN = "qwerty.xyz"; //not the real url obviously
        public static final String BASE_URL = "https://www.revbay.com/";

        private static Retrofit retrofit = null;

        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        private static Gson gson = new GsonBuilder()
                .create();

        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson));

        public static <S> S createService(Class<S> serviceClass) {
            return createService(serviceClass, null, null);
        }

        public static <S> S createService(Class<S> serviceClass, String username, String password) {
            Retrofit retrofit = builder.client(getUnsafeOkHttpClient())
                    .build();
            return retrofit.create(serviceClass);
        }

        public static OkHttpClient getUnsafeOkHttpClient() {

            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[0];
                    }
                } };




                return httpClient.sslSocketFactory(new TLSSocketFactory(), (X509TrustManager)trustAllCerts[0])
                        .build();



            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    private static TestServiceManager instance = null;
    public static TestServiceManager getInstance() {
        if (instance == null) {
            instance = new TestServiceManager();
        }
        return instance;
    }

}
