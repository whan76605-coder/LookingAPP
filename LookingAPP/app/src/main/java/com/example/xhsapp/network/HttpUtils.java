package com.example.xhsapp.network;

import android.os.Handler;
import android.os.Looper;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpUtils {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface Callback2 {
        void onSuccess(String response);
        void onFailure(String error);
    }

    public static void get(String url, Callback2 callback) {
        Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(() -> callback.onFailure(e.getMessage()));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body() != null ? response.body().string() : "";
                mainHandler.post(() -> {
                    if (response.isSuccessful()) callback.onSuccess(body);
                    else callback.onFailure("HTTP " + response.code());
                });
            }
        });
    }

    public static void post(String url, String json, Callback2 callback) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(() -> callback.onFailure(e.getMessage()));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respBody = response.body() != null ? response.body().string() : "";
                mainHandler.post(() -> {
                    if (response.isSuccessful()) callback.onSuccess(respBody);
                    else callback.onFailure("HTTP " + response.code());
                });
            }
        });
    }

    // 检查网络连通性（ping unsplash）
    public static void checkNetwork(Callback2 callback) {
        get("https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=10", callback);
    }
}
