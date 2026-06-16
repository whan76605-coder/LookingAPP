package com.example.xhsapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.xhsapp.R;
import com.example.xhsapp.utils.SpUtils;

public class SettingsActivity extends AppCompatActivity {

    private ImageView ivBack;
    private Switch swNotification, swDarkMode;
    private TextView tvLogout, tvVersion, tvAbout, tvPrivacy, tvClearCache;
    private View tvAccountSecurity, tvGeneralSettings, tvPrivacySettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        initEvents();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        swNotification = findViewById(R.id.sw_notification);
        swDarkMode = findViewById(R.id.sw_dark_mode);
        tvLogout = findViewById(R.id.tv_logout);
        tvVersion = findViewById(R.id.tv_version);
        tvAbout = findViewById(R.id.tv_about);
        tvPrivacy = findViewById(R.id.tv_privacy);
        tvClearCache = findViewById(R.id.tv_clear_cache);

        tvAccountSecurity = findViewById(R.id.ll_account_security);
        tvGeneralSettings = findViewById(R.id.ll_general_settings);
        tvPrivacySettings = findViewById(R.id.ll_privacy_settings);

        swNotification.setChecked(SpUtils.isNotificationOn(this));
        swDarkMode.setChecked(SpUtils.isDarkMode(this));
        tvVersion.setText("V1.0.0");
        tvClearCache.setText("12.6MB");
    }

    private void initEvents() {
        ivBack.setOnClickListener(v -> finish());

        swNotification.setOnCheckedChangeListener((btn, checked) -> {
            SpUtils.setNotification(this, checked);
            Toast.makeText(this, checked ? "消息通知已开启" : "消息通知已关闭", Toast.LENGTH_SHORT).show();
        });

        swDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            SpUtils.setDarkMode(this, checked);
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, checked ? "夜间模式已开启" : "夜间模式已关闭", Toast.LENGTH_SHORT).show();
        });

        tvClearCache.setOnClickListener(v -> {
            tvClearCache.setText("0MB");
            Toast.makeText(this, "缓存清理完成", Toast.LENGTH_SHORT).show();
        });

        tvAbout.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("关于我们")
                        .setMessage("Looking - 发现更多美好\n\n" +
                                "版本：V1.0.0\n" +
                                "一个仿小红书风格的内容分享社区应用。\n\n" +
                                "技术栈：\n" +
                                "· Java 17 + Android Studio\n" +
                                "· Android SDK 26-34\n" +
                                "· ViewBinding 视图绑定\n" +
                                "· Room 本地数据库\n" +
                                "· Retrofit2 + OkHttp 网络请求\n" +
                                "· Gson JSON 解析\n" +
                                "· Glide 图片加载\n" +
                                "· Material Design 组件\n" +
                                "· ViewPager2 + RecyclerView\n\n" +
                                "后端：\n" +
                                "· Spring Boot 3.2.6\n" +
                                "· MyBatis-Plus 3.5.7\n" +
                                "· MySQL 数据库\n\n" +
                                "功能：\n" +
                                "· 图文/视频/纯文案多类型发布\n" +
                                "· 点赞/收藏/评论/分享\n" +
                                "· 用户登录注册\n" +
                                "· 分类浏览与搜索\n" +
                                "· 夜间模式\n" +
                                "· 草稿箱\n" +
                                "· 私信聊天")
                        .setPositiveButton("确定", null)
                        .show());

        tvPrivacy.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("隐私政策")
                        .setMessage("我们重视你的隐私保护。\n\n" +
                                "1. 我们不会收集你的个人信息\n" +
                                "2. 所有数据存储在本地数据库中\n" +
                                "3. 我们不会向第三方分享你的数据\n" +
                                "4. 你可以随时清除所有本地数据\n\n" +
                                "更新日期：2026年6月")
                        .setPositiveButton("我知道了", null)
                        .show());

        if (tvAccountSecurity != null) {
            tvAccountSecurity.setOnClickListener(v ->
                    new AlertDialog.Builder(this)
                            .setTitle("账号与安全")
                            .setMessage("账号安全设置\n\n" +
                                    "· 当前账号：已绑定\n" +
                                    "· 密码安全：MD5加密存储\n" +
                                    "· 更多安全功能即将上线")
                            .setPositiveButton("确定", null)
                            .show());
        }

        if (tvGeneralSettings != null) {
            tvGeneralSettings.setOnClickListener(v ->
                    new AlertDialog.Builder(this)
                            .setTitle("通用设置")
                            .setMessage("通用设置\n\n" +
                                    "· 字体大小：标准\n" +
                                    "· 图片质量：高清\n" +
                                    "· 自动播放：仅WiFi\n" +
                                    "· 多语言：中文\n\n" +
                                    "更多设置即将上线")
                            .setPositiveButton("确定", null)
                            .show());
        }

        if (tvPrivacySettings != null) {
            tvPrivacySettings.setOnClickListener(v ->
                    new AlertDialog.Builder(this)
                            .setTitle("隐私设置")
                            .setMessage("隐私设置\n\n" +
                                    "· 允许他人查看我的收藏：开\n" +
                                    "· 允许通过用户名搜索到我：开\n" +
                                    "· 个性化推荐：开\n\n" +
                                    "更多隐私设置即将上线")
                            .setPositiveButton("确定", null)
                            .show());
        }

        tvLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("退出登录")
                    .setMessage("确定要退出登录吗？")
                    .setPositiveButton("确定", (d, w) -> {
                        SpUtils.logout(this);
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
    }
}
