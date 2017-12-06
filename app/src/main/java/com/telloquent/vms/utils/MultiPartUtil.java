package com.telloquent.vms.utils;

import android.content.Context;
import com.google.gson.Gson;
import com.telloquent.vms.model.createvrf.CreateVrfDetails;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Telloquent-DM6M on 10/9/2017.
 */

public class MultiPartUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public MultiPartUtil() {
    }

    public static MultipartBody.Part multiPartFile(Context context, String name, InputStream inputStream) throws IOException {
        File outputDir = context.getCacheDir();
        File file = File.createTempFile(name, ".png", outputDir);
        FileUtils.copyInputStreamToFile(inputStream, file);
        return multiPartFile(name, file);
    }

    public static MultipartBody.Part multiPartFile(String name, File file) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        return body;
    }

    public static RequestBody jsonRequestBody(CreateVrfDetails documentDataDetail) {
        Gson gson = new Gson();
        String json = gson.toJson(documentDataDetail);
        RequestBody detail = RequestBody.create(JSON, json);
        return detail;
    }

}
