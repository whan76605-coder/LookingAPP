package com.example.xhsapp.utils;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class NightModeHelper {

    /** 应用夜间模式设置（在 Activity 的 onCreate 之前调用） */
    public static void apply(Context context) {
        boolean dark = SpUtils.isDarkMode(context);
        AppCompatDelegate.setDefaultNightMode(
                dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

    /** 切换夜间模式并即时生效 */
    public static void toggle(Activity activity) {
        boolean dark = !SpUtils.isDarkMode(activity);
        SpUtils.setDarkMode(activity, dark);
        AppCompatDelegate.setDefaultNightMode(
                dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        activity.recreate();
    }

    /** 获取当前是否为夜间模式 */
    public static boolean isDark(Context context) {
        return SpUtils.isDarkMode(context);
    }
}
