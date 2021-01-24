package com.example.myapplication.HttpConnector;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

/**
 * Lớp có nhiệm vụ thực hiện HTTP connect, các method POST, GET, ...
 */
@Slf4j
public class HttpConnector {
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000,TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build() ;
    /**
     * Send patch method HTTP.
     *
     * @param url  the url
     * @param body the body
     * @return the response
     */
    public String sendPatch(String url , String body) {
        try {

            RequestBody requestBody = RequestBody.create(body, MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));
            Request request = new Request.Builder().url(url)
//                    .header("Connection", "close")
                    .patch(requestBody).build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Send post method.
     *
     * @param url  the url
     * @param body the body
     * @return the response
     */
    public String sendPost(String url , String body) {
        try {

            RequestBody requestBody = RequestBody.create(body, MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE));
            Request request = new Request.Builder().url(url)
//                    .header("Connection", "close")
                    .post(requestBody).build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
