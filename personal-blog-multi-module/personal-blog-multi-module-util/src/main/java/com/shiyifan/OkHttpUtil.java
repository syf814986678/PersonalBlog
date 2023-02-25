package com.shiyifan;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * @author ZouCha
 * @name OkHttpUtil
 * @date 2021-07-23 19:01
 **/
public class OkHttpUtil {
    /**
     * get请求
     */
    public static String get(String url) throws Exception {
        try {
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                    .addHeader("Accept", "*/*")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            Request request = builder.get().build();
            Response response = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true).build().newCall(request).execute();
            ResponseBody responseBody = null;
            if (response.isSuccessful()) {
                responseBody = response.body();
                if (responseBody != null) {
                    return responseBody.string();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return "error";
    }
}
