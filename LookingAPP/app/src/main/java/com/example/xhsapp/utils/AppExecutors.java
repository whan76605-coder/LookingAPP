package com.example.xhsapp.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 统一线程池 —— 所有数据库/网络操作都走这里，避免到处 new Thread
 */
public class AppExecutors {

    private static AppExecutors instance;
    private final ExecutorService diskIO;
    private final ExecutorService networkIO;

    private AppExecutors() {
        this.diskIO = Executors.newFixedThreadPool(3);
        this.networkIO = Executors.newFixedThreadPool(2);
    }

    public static AppExecutors get() {
        if (instance == null) {
            synchronized (AppExecutors.class) {
                if (instance == null) {
                    instance = new AppExecutors();
                }
            }
        }
        return instance;
    }

    /** 数据库读写（短任务） */
    public ExecutorService diskIO() {
        return diskIO;
    }

    /** 网络请求（可能等待较长） */
    public ExecutorService networkIO() {
        return networkIO;
    }

    /** 方便在主线程执行回调 */
    public static void runOnUi(android.app.Activity activity, Runnable action) {
        activity.runOnUiThread(action);
    }
}
