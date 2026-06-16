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

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirm, etNickname;
    private TextView btnRegister;
    private ImageView ivBack;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initEvents();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirm = findViewById(R.id.et_confirm);
        etNickname = findViewById(R.id.et_nickname);
        btnRegister = findViewById(R.id.btn_register);
        ivBack = findViewById(R.id.iv_back);
        tvLogin = findViewById(R.id.tv_login);
    }

    private void initEvents() {
        ivBack.setOnClickListener(v -> finish());
        tvLogin.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();

        if (TextUtils.isEmpty(username)) { etUsername.setError("请输入用户名"); return; }
        if (username.length() < 3) { etUsername.setError("用户名至少3位"); return; }
        if (TextUtils.isEmpty(nickname)) { etNickname.setError("请输入昵称"); return; }
        if (TextUtils.isEmpty(password)) { etPassword.setError("请输入密码"); return; }
        if (password.length() < 6) { etPassword.setError("密码至少6位"); return; }
        if (!password.equals(confirm)) { etConfirm.setError("两次密码不一致"); return; }

        btnRegister.setEnabled(false);
        btnRegister.setText("注册中...");

        ApiService.register(username, password, nickname, user -> {
            if (user != null) {
                runOnUiThread(() -> {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("注 册");
                    SpUtils.saveLoginInfo(RegisterActivity.this, user.id, username,
                            user.nickname != null ? user.nickname : nickname,
                            user.avatar != null ? user.avatar : "");
                    ToastUtils.show(RegisterActivity.this, "注册成功，欢迎 " + nickname + "！");
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finishAffinity();
                });
            } else {
                AppDatabase db = AppDatabase.getInstance(RegisterActivity.this);
                AppExecutors.get().diskIO().execute(() -> {
                    int exists = db.userDao().isUsernameExist(username);
                    if (exists > 0) {
                        runOnUiThread(() -> {
                            btnRegister.setEnabled(true);
                            btnRegister.setText("注册");
                            etUsername.setError("用户名已存在");
                        });
                        return;
                    }
                    User localUser = new User(username, MD5Utils.md5(password), nickname);
                    long id = db.userDao().insertUser(localUser);
                    runOnUiThread(() -> {
                        btnRegister.setEnabled(true);
                        btnRegister.setText("注 册");
                        if (id > 0) {
                            SpUtils.saveLoginInfo(RegisterActivity.this, (int) id, username, nickname, "");
                            ToastUtils.show(RegisterActivity.this, "注册成功，欢迎 " + nickname + "！");
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finishAffinity();
                        } else {
                            ToastUtils.show(RegisterActivity.this, "注册失败，请重试");
                        }
                    });
                });
            }
        });
    }
}
