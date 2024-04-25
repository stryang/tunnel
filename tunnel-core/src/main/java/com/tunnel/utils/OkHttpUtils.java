package com.tunnel.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author yq
 * @since 2024/4/16
 */
public class OkHttpUtils {

    private final OkHttpClient okHttpClient;
    private static OkHttpUtils instance;
    private MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

    public OkHttpUtils() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpUtils getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (OkHttpUtils.class) {
            if (instance != null) {
                return instance;
            }
            instance = new OkHttpUtils();
            return instance;
        }
    }

    /**
     * GET 请求
     */
    public Response get(String url, Map<String, String> params) throws IOException {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        Request request = new Request.Builder()
                .get()
                .url(sb.toString())
                .build();
        return okHttpClient.newCall(request).execute();
    }

    /**
     * POST 请求
     */
    public <T> Response post(String url, T params) throws IOException {
        return execute(params, requestBody -> {
            Request request = new Request.Builder()
                    .post(requestBody)
                    .url(url)
                    .build();
            return okHttpClient.newCall(request);
        }).execute();
    }

    /**
     * POST 请求
     */
    public <T> Response put(String url, T params) throws IOException {
        return execute(params, requestBody -> {
            Request request = new Request.Builder()
                    .put(requestBody)
                    .url(url)
                    .build();
            return okHttpClient.newCall(request);
        }).execute();
    }

    /**
     * POST 请求
     */
    public <T> Response delete(String url, T params) throws IOException {
        return execute(params, requestBody -> {
            Request request = new Request.Builder()
                    .delete(requestBody)
                    .url(url)
                    .build();
            return okHttpClient.newCall(request);
        }).execute();
    }

    <T> Call execute(T body, Function<RequestBody, Call> executor) {
        RequestBody requestBody = RequestBody.create(JSON_TYPE, JSON.toJSONString(body));
        return executor.apply(requestBody);
    }
}
