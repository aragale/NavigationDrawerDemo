package com.example.yuze.navigationdrawerdemo;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JsonHelper {

//    public static final String Host = "http://47.93.221.223:8002";
    public static final String REGISTE_URL = "http://47.93.221.223:8002/api/users";
    public static final String LOGIN_URL = "http://47.93.221.223:8002/api/sessions";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    /**
     * downloads a URL and print its contents as a string
     * @param url
     * @return
     * @throws IOException
     */
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     *posts data to a service
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    String  Register(String url,String json) throws IOException{
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(REGISTE_URL)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
