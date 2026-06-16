package com.example.xhsapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xhsapp.R;
import com.example.xhsapp.utils.SpUtils;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private View ivLogo, vGlow;
    private TextView tvBrand, tvSlogan, tvSubSlogan;
    private View loadingBar, vDivider;
    private View dot1, dot2, dot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏沉浸
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        setContentView(R.layout.activity_splash_looking);

        initViews();
        startAnimations();
    }

    private void initViews() {
        ivLogo     = findViewById(R.id.iv_logo);
        vGlow      = findViewById(R.id.v_glow);
        tvBrand    = findViewById(R.id.tv_brand);
        tvSlogan   = findViewById(R.id.tv_slogan);
        tvSubSlogan = findViewById(R.id.tv_sub_slogan);
        loadingBar = findViewById(R.id.loading_bar);
        vDivider   = findViewById(R.id.v_divider);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        // 初始状态：全部透明/缩小
        ivLogo.setAlpha(0f);
        ivLogo.setScaleX(0.5f);
        ivLogo.setScaleY(0.5f);
        vGlow.setAlpha(0f);
        tvBrand.setAlpha(0f);
        tvBrand.setTranslationY(30f);
        tvSlogan.setAlpha(0f);
        tvSlogan.setTranslationY(20f);
        tvSubSlogan.setAlpha(0f);
        tvSubSlogan.setTranslationY(20f);
        vDivider.setAlpha(0f);
        vDivider.setScaleX(0f);
        loadingBar.setAlpha(0f);
        dot1.setAlpha(0f); dot2.setAlpha(0f); dot3.setAlpha(0f);
    }

    private void startAnimations() {
        // ① Logo 弹入
        AnimatorSet logoSet = new AnimatorSet();
        logoSet.playTogether(
            ObjectAnimator.ofFloat(ivLogo, "alpha", 0f, 1f).setDuration(600),
            ObjectAnimator.ofFloat(ivLogo, "scaleX", 0.5f, 1f).setDuration(700),
            ObjectAnimator.ofFloat(ivLogo, "scaleY", 0.5f, 1f).setDuration(700),
            ObjectAnimator.ofFloat(vGlow, "alpha", 0f, 1f).setDuration(800)
        );
        logoSet.setInterpolator(new OvershootInterpolator(1.2f));
        logoSet.setStartDelay(100);

        // ② 品牌名浮入
        AnimatorSet brandSet = new AnimatorSet();
        brandSet.playTogether(
            ObjectAnimator.ofFloat(tvBrand, "alpha", 0f, 1f).setDuration(600),
            ObjectAnimator.ofFloat(tvBrand, "translationY", 30f, 0f).setDuration(600)
        );
        brandSet.setInterpolator(new DecelerateInterpolator(2f));
        brandSet.setStartDelay(500);

        // ③ Slogan
        AnimatorSet sloganSet = new AnimatorSet();
        sloganSet.playTogether(
            ObjectAnimator.ofFloat(tvSlogan, "alpha", 0f, 1f).setDuration(500),
            ObjectAnimator.ofFloat(tvSlogan, "translationY", 20f, 0f).setDuration(500),
            ObjectAnimator.ofFloat(tvSubSlogan, "alpha", 0f, 0.6f).setDuration(500),
            ObjectAnimator.ofFloat(tvSubSlogan, "translationY", 20f, 0f).setDuration(500)
        );
        sloganSet.setInterpolator(new DecelerateInterpolator());
        sloganSet.setStartDelay(700);

        // ④ 分割线展开
        AnimatorSet divSet = new AnimatorSet();
        divSet.playTogether(
            ObjectAnimator.ofFloat(vDivider, "scaleX", 0f, 1f).setDuration(400),
            ObjectAnimator.ofFloat(vDivider, "alpha", 0f, 1f).setDuration(400)
        );
        divSet.setInterpolator(new AccelerateDecelerateInterpolator());
        divSet.setStartDelay(900);

        // ⑤ 三点指示 依次出现
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            animateDot(dot1, 0);
            animateDot(dot2, 120);
            animateDot(dot3, 240);
        }, 1000);

        // ⑥ Loading 条出现
        ObjectAnimator loadAnim = ObjectAnimator.ofFloat(loadingBar, "alpha", 0f, 1f);
        loadAnim.setDuration(400);
        loadAnim.setStartDelay(1200);

        // 启动所有动画
        logoSet.start();
        brandSet.start();
        sloganSet.start();
        divSet.start();
        loadAnim.start();

        // Glow 持续脉动
        new Handler(Looper.getMainLooper()).postDelayed(() -> startGlowPulse(), 900);

        // 2.8秒后跳转
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateNext, 2800);
    }

    private void animateDot(View dot, long delay) {
        ObjectAnimator a = ObjectAnimator.ofFloat(dot, "alpha", 0f, 1f);
        a.setDuration(300);
        a.setStartDelay(delay);
        a.setInterpolator(new DecelerateInterpolator());
        a.start();
    }

    private void startGlowPulse() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(vGlow, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(vGlow, "scaleY", 1f, 1.2f, 1f);
        ObjectAnimator alpha  = ObjectAnimator.ofFloat(vGlow, "alpha", 0.7f, 1f, 0.7f);
        AnimatorSet pulse = new AnimatorSet();
        pulse.playTogether(scaleX, scaleY, alpha);
        pulse.setDuration(2000);
        // 用 AnimatorListener 实现无限循环
        pulse.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator a) { pulse.start(); }
        });
        pulse.start();
    }

    private void navigateNext() {
        // Logo 缩小淡出
        AnimatorSet exitSet = new AnimatorSet();
        exitSet.playTogether(
            ObjectAnimator.ofFloat(ivLogo, "scaleX", 1f, 1.1f).setDuration(300),
            ObjectAnimator.ofFloat(ivLogo, "scaleY", 1f, 1.1f).setDuration(300),
            ObjectAnimator.ofFloat(ivLogo, "alpha", 1f, 0f).setDuration(400),
            ObjectAnimator.ofFloat(tvBrand, "alpha", 1f, 0f).setDuration(400),
            ObjectAnimator.ofFloat(tvSlogan, "alpha", 1f, 0f).setDuration(400)
        );
        exitSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator a) {
                goNext();
            }
        });
        exitSet.start();
    }

    private void goNext() {
        Intent intent = SpUtils.isLogin(this)
                ? new Intent(this, MainActivity.class)
                : new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    // AnimatorSet 不支持 setRepeatCount，通过此方法绑定
    // （此处仅为占位注释，实际已在 listener 里实现循环）
}
