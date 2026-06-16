package com.example.xhsapp.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.xhsapp.R;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.User;
import com.example.xhsapp.utils.AppExecutors;
import com.example.xhsapp.utils.SpUtils;
import com.example.xhsapp.utils.ToastUtils;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView ivBack, ivAvatar;
    private EditText etNickname, etBio;
    private TextView tvSave;

    private Uri selectedAvatarUri;

    private final ActivityResultLauncher<String> avatarPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedAvatarUri = uri;
                    Glide.with(this).load(uri).circleCrop().into(ivAvatar);
                    // 保存头像uri到SharedPreferences
                    SpUtils.updateAvatar(this, uri.toString());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initViews();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivAvatar = findViewById(R.id.iv_avatar);
        etNickname = findViewById(R.id.et_nickname);
        etBio = findViewById(R.id.et_bio);
        tvSave = findViewById(R.id.tv_save);

        // 加载当前头像
        String currentAvatar = SpUtils.getAvatar(this);
        if (!currentAvatar.isEmpty()) {
            Glide.with(this).load(currentAvatar)
                    .placeholder(R.drawable.ic_default_avatar)
                    .circleCrop().into(ivAvatar);
        }

        etNickname.setText(SpUtils.getNickname(this));
        etBio.setText(SpUtils.getBio(this));

        ivBack.setOnClickListener(v -> finish());
        ivAvatar.setOnClickListener(v -> avatarPickerLauncher.launch("image/*"));
        tvSave.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String nickname = etNickname.getText().toString().trim();
        String bio = etBio.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            etNickname.setError("昵称不能为空");
            return;
        }
        SpUtils.updateNickname(this, nickname);
        SpUtils.updateBio(this, bio);

        int userId = SpUtils.getUserId(this);
        if (userId > 0) {
            AppDatabase db = AppDatabase.getInstance(this);
            AppExecutors.get().diskIO().execute(() -> {
                User user = db.userDao().getUserById(userId);
                if (user != null) {
                    user.nickname = nickname;
                    user.bio = bio;
                    db.userDao().updateUser(user);
                }
                runOnUiThread(() -> {
                    ToastUtils.show(this, "保存成功");
                    finish();
                });
            });
        } else {
            ToastUtils.show(this, "保存成功");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 刷新头像显示
        String avatar = SpUtils.getAvatar(this);
        if (!avatar.isEmpty() && selectedAvatarUri == null) {
            try {
                Glide.with(this).load(avatar)
                        .placeholder(R.drawable.ic_default_avatar)
                        .circleCrop().into(ivAvatar);
            } catch (Exception ignored) {}
        }
    }
}
