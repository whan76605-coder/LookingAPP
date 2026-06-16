package com.example.xhsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SpUtils {

    private static final String SP_NAME = "xhs_prefs";
    private static final String KEY_IS_LOGIN = "is_login";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_BIO = "bio";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_SEARCH_HISTORY = "search_history";
    private static final String KEY_NOTIFICATION_ON = "notification_on";
    private static final String KEY_BROWSE_HISTORY = "browse_history";
    private static final String KEY_FOLLOWED_AUTHORS = "followed_authors";

    private static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static void saveLoginInfo(Context context, int userId, String username,
                                     String nickname, String avatar) {
        getSp(context).edit()
                .putBoolean(KEY_IS_LOGIN, true)
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USERNAME, username)
                .putString(KEY_NICKNAME, nickname)
                .putString(KEY_AVATAR, avatar)
                .apply();
    }

    public static void logout(Context context) {
        getSp(context).edit()
                .putBoolean(KEY_IS_LOGIN, false)
                .putInt(KEY_USER_ID, -1)
                .putString(KEY_USERNAME, "")
                .putString(KEY_NICKNAME, "")
                .putString(KEY_AVATAR, "")
                .apply();
    }

    public static boolean isLogin(Context context) {
        return getSp(context).getBoolean(KEY_IS_LOGIN, false);
    }

    public static int getUserId(Context context) {
        return getSp(context).getInt(KEY_USER_ID, -1);
    }

    public static String getUsername(Context context) {
        return getSp(context).getString(KEY_USERNAME, "");
    }

    public static String getNickname(Context context) {
        return getSp(context).getString(KEY_NICKNAME, "我");
    }

    public static String getAvatar(Context context) {
        return getSp(context).getString(KEY_AVATAR, "");
    }

    public static void updateNickname(Context context, String nickname) {
        getSp(context).edit().putString(KEY_NICKNAME, nickname).apply();
    }

    public static void updateAvatar(Context context, String avatar) {
        getSp(context).edit().putString(KEY_AVATAR, avatar).apply();
    }

    public static void updateBio(Context context, String bio) {
        getSp(context).edit().putString(KEY_BIO, bio).apply();
    }

    public static String getBio(Context context) {
        return getSp(context).getString(KEY_BIO, "热爱生活，热爱旅行");
    }

    public static boolean isDarkMode(Context context) {
        return getSp(context).getBoolean(KEY_DARK_MODE, false);
    }

    public static void setDarkMode(Context context, boolean dark) {
        getSp(context).edit().putBoolean(KEY_DARK_MODE, dark).apply();
    }

    public static String getSearchHistory(Context context) {
        return getSp(context).getString(KEY_SEARCH_HISTORY, "");
    }

    public static void saveSearchHistory(Context context, String history) {
        getSp(context).edit().putString(KEY_SEARCH_HISTORY, history).apply();
    }

    public static void clearSearchHistory(Context context) {
        getSp(context).edit().putString(KEY_SEARCH_HISTORY, "").apply();
    }

    public static boolean isNotificationOn(Context context) {
        return getSp(context).getBoolean(KEY_NOTIFICATION_ON, true);
    }

    public static void setNotification(Context context, boolean on) {
        getSp(context).edit().putBoolean(KEY_NOTIFICATION_ON, on).apply();
    }

    // ===== 浏览历史 =====
    public static String getBrowseHistory(Context context) {
        return getSp(context).getString(KEY_BROWSE_HISTORY, "");
    }

    public static void addBrowseHistory(Context context, String postIdStr) {
        if (!isLogin(context)) return;
        String old = getBrowseHistory(context);
        // 用逗号分隔，最多保留20条，去重
        StringBuilder sb = new StringBuilder();
        if (old.contains(postIdStr)) {
            // 已存在则移到最前面
            String[] items = old.split(",");
            sb.append(postIdStr);
            for (String item : items) {
                if (!item.equals(postIdStr) && !item.isEmpty()) {
                    sb.append(",").append(item);
                }
            }
        } else {
            sb.append(postIdStr).append(",").append(old);
        }
        String result = sb.toString();
        String[] parts = result.split(",");
        StringBuilder finalSb = new StringBuilder();
        for (int i = 0; i < Math.min(parts.length, 20); i++) {
            if (!parts[i].isEmpty()) {
                if (finalSb.length() > 0) finalSb.append(",");
                finalSb.append(parts[i]);
            }
        }
        getSp(context).edit().putString(KEY_BROWSE_HISTORY, finalSb.toString()).apply();
    }

    public static void clearBrowseHistory(Context context) {
        getSp(context).edit().putString(KEY_BROWSE_HISTORY, "").apply();
    }

    // ===== 按作者关注 ====
    public static boolean isPostAuthorFollowed(Context context, String authorName) {
        if (authorName == null) return false;
        Set<String> followed = getSp(context).getStringSet(KEY_FOLLOWED_AUTHORS, new HashSet<>());
        return followed.contains(authorName);
    }

    public static void setPostAuthorFollowed(Context context, String authorName, boolean followed) {
        Set<String> followedSet = new HashSet<>(getSp(context).getStringSet(KEY_FOLLOWED_AUTHORS, new HashSet<>()));
        if (followed) {
            followedSet.add(authorName);
        } else {
            followedSet.remove(authorName);
        }
        getSp(context).edit().putStringSet(KEY_FOLLOWED_AUTHORS, followedSet).apply();
    }

    /** 获取已关注作者的数量 */
    public static int getFollowCount(Context context) {
        Set<String> followed = getSp(context).getStringSet(KEY_FOLLOWED_AUTHORS, new HashSet<>());
        return followed.size();
    }

    /** 获取所有已关注的作者名列表 */
    public static Set<String> getFollowedAuthors(Context context) {
        return getSp(context).getStringSet(KEY_FOLLOWED_AUTHORS, new HashSet<>());
    }
}
