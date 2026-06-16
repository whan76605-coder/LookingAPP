package com.example.xhsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xhsapp.R;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.User;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.utils.AppExecutors;
import com.example.xhsapp.utils.MD5Utils;
import com.example.xhsapp.utils.SpUtils;
import com.example.xhsapp.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private TextView btnLogin, tvRegister, tvForget;
    private ImageView ivTogglePwd;
    private boolean pwdVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initEvents();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvForget = findViewById(R.id.tv_forget);
        ivTogglePwd = findViewById(R.id.iv_toggle_pwd);
    }

    private void initEvents() {
        btnLogin.setOnClickListener(v -> doLogin());

        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        tvForget.setOnClickListener(v ->
                ToastUtils.show(this, "请联系客服重置密码"));

        ivTogglePwd.setOnClickListener(v -> {
            pwdVisible = !pwdVisible;
            if (pwdVisible) {
                etPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePwd.setImageResource(R.drawable.ic_eye_open);
            } else {
                etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePwd.setImageResource(R.drawable.ic_eye_close);
            }
            etPassword.setSelection(etPassword.getText().length());
        });
    }

    private void doLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) { etUsername.setError("请输入用户名"); return; }
        if (TextUtils.isEmpty(password)) { etPassword.setError("请输入密码"); return; }
        if (password.length() < 6) { etPassword.setError("密码不能少于6位"); return; }

        btnLogin.setEnabled(false);
        btnLogin.setText("登录中...");

        ApiService.login(username, password, user -> {
            if (user != null) {
                SpUtils.saveLoginInfo(this, user.id, user.username,
                        user.nickname != null ? user.nickname : username,
                        user.avatar != null ? user.avatar : "");
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("登 录");
                    ToastUtils.show(this, "欢迎回来，" + user.nickname + "！");
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });
            } else {
                AppExecutors.get().diskIO().execute(() -> {
                    String md5Pwd = MD5Utils.md5(password);
                    User local = AppDatabase.getInstance(LoginActivity.this).userDao().login(username, md5Pwd);
                    runOnUiThread(() -> {
                        btnLogin.setEnabled(true);
                        btnLogin.setText("登 录");
                        if (local != null) {
                            SpUtils.saveLoginInfo(LoginActivity.this, local.id, local.username,
                                    local.nickname, local.avatar != null ? local.avatar : "");
                            ToastUtils.show(LoginActivity.this, "欢迎回来，" + local.nickname + "！");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            ToastUtils.show(LoginActivity.this, "用户名或密码错误");
                        }
                    });
                });
            }
        });
    }
}
