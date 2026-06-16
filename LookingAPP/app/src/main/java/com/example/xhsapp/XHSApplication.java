package com.example.xhsapp;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.receiver.NetworkChangeReceiver;

public class XHSApplication extends Application {

    private static XHSApplication instance;
    private NetworkChangeReceiver networkReceiver;

    public static XHSApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 预填充数据（异步）
        AppDatabase.prepopulateIfEmpty(this);

        // 注册网络监听
        try {
            networkReceiver = new NetworkChangeReceiver();
            registerReceiver(networkReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        } catch (Exception ignored) {
            // 某些 Android 版本可能限制隐式广播注册
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (networkReceiver != null) {
            try {
                unregisterReceiver(networkReceiver);
            } catch (Exception ignored) {}
        }
    }
}
