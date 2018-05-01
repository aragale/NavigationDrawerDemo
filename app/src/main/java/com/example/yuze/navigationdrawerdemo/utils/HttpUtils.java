package com.example.yuze.navigationdrawerdemo.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * post and return response
     *
     * @param url  URL
     * @param json request body
     * @return return response string if succeed, or null
     */
    public static String post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("HttpUtils", "POST Exception", e);
            return null;
        }
    }

    public static String get(String url,String session){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("session",session)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e("HttpUtil","GET Exception",e);
            return null;
        }
    }
}
