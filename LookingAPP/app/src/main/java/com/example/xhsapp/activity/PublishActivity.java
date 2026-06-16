package com.example.xhsapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import android.widget.FrameLayout;

import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.xhsapp.R;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.utils.AppExecutors;
import com.example.xhsapp.utils.SpUtils;
import com.example.xhsapp.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PublishActivity extends AppCompatActivity {

    private static final int MAX_CONTENT = 500;

    private ImageView ivBack;
    private FrameLayout ivAddPhoto;
    private TextView tvPublish, tvSaveDraft, tvCounter;
    private EditText etContent, etLocation;
    private Spinner spCategory;
    private RadioGroup rgPostType;
    private LinearLayout llImageContainer;

    private List<Uri> selectedImageUris = new ArrayList<>();
    private int editingPostId = -1; // -1 = 新建, >0 = 编辑草稿

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUris.add(uri);
                    addImagePreview(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initViews();
        initEvents();

        // 检查是否是编辑草稿
        editingPostId = getIntent().getIntExtra("draft_id", -1);
        if (editingPostId > 0) {
            loadDraft(editingPostId);
        }
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        tvPublish = findViewById(R.id.tv_publish);
        tvSaveDraft = findViewById(R.id.tv_save_draft);
        tvCounter = findViewById(R.id.tv_counter);
        etContent = findViewById(R.id.et_content);
        etLocation = findViewById(R.id.et_location);
        spCategory = findViewById(R.id.sp_category);
        rgPostType = findViewById(R.id.rg_post_type);
        ivAddPhoto = findViewById(R.id.iv_add_photo);
        llImageContainer = findViewById(R.id.ll_image_container);

        String[] categories = {"旅行", "美食", "生活", "穿搭", "运动", "阅读", "科技", "摄影"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void initEvents() {
        ivBack.setOnClickListener(v -> {
            // 如果有内容则提示是否保存草稿
            if (hasContent()) {
                new AlertDialog.Builder(this)
                        .setTitle("是否保存草稿？")
                        .setMessage("退出后内容不会丢失")
                        .setPositiveButton("保存草稿", (d, w) -> saveDraft())
                        .setNegativeButton("不保存", (d, w) -> finish())
                        .setNeutralButton("取消", null)
                        .show();
            } else {
                finish();
            }
        });

        ivAddPhoto.setOnClickListener(v -> {
            if (selectedImageUris.size() >= 3) {
                ToastUtils.show(this, "最多选择3张图片");
                return;
            }
            imagePickerLauncher.launch("image/*");
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override
            public void afterTextChanged(Editable s) {
                int len = s.length();
                tvCounter.setText(len + "/" + MAX_CONTENT);
            }
        });

        tvPublish.setOnClickListener(v -> doPublish());
        tvSaveDraft.setOnClickListener(v -> {
            if (etContent.getText().toString().trim().isEmpty()) {
                ToastUtils.show(this, "内容为空，无法保存草稿");
                return;
            }
            saveDraft();
        });
    }

    private boolean hasContent() {
        return etContent.getText().toString().trim().length() > 0 ||
               !selectedImageUris.isEmpty();
    }

    // ===== 多图预览 =====

    /** 将 content:// Uri 拷贝到 App 内部图片目录，返回持久文件路径 */
    private String copyImageToStorage(Uri uri) {
        try {
            File dir = new File(getFilesDir(), "images");
            if (!dir.exists()) dir.mkdirs();
            File file = new File(dir, "img_" + System.currentTimeMillis() + ".jpg");
            try (InputStream in = getContentResolver().openInputStream(uri);
                 FileOutputStream out = new FileOutputStream(file)) {
                byte[] buf = new byte[4096];
                int len;
                while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            return uri.toString(); // fallback
        }
    }

    private void addImagePreview(Uri uri) {
        if (llImageContainer == null) return;

        FrameLayout frame = new FrameLayout(this);
        int size = (int) (90 * getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
        lp.setMarginEnd((int) (8 * getResources().getDisplayMetrics().density));
        frame.setLayoutParams(lp);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(uri).centerCrop().into(imageView);

        ImageView removeBtn = new ImageView(this);
        FrameLayout.LayoutParams rlp = new FrameLayout.LayoutParams(24, 24);
        rlp.gravity = android.view.Gravity.TOP | android.view.Gravity.END;
        removeBtn.setLayoutParams(rlp);
        removeBtn.setImageResource(R.drawable.ic_close);
        removeBtn.setBackgroundColor(0x80FF0000);
        removeBtn.setPadding(4, 4, 4, 4);

        int index = selectedImageUris.size() - 1;
        removeBtn.setOnClickListener(v -> {
            selectedImageUris.remove(index);
            llImageContainer.removeView(frame);
        });

        frame.addView(imageView);
        frame.addView(removeBtn);
        llImageContainer.addView(frame);
    }

    // ===== 草稿 =====

    private void saveDraft() {
        String content = etContent.getText().toString().trim();
        if (content.isEmpty()) { finish(); return; }

        String category = spCategory.getSelectedItem().toString();
        String nickname = SpUtils.getNickname(this);
        String avatar = SpUtils.getAvatar(this);
        String title = content.length() > 20 ? content.substring(0, 20) + "..." : content;

        String imageUrl = "";
        if (!selectedImageUris.isEmpty()) {
            imageUrl = copyImageToStorage(selectedImageUris.get(0));
        }

        Post draft;
        if (editingPostId > 0) {
            // 更新已有草稿
            draft = AppDatabase.getInstance(this).postDao().getPostById(editingPostId);
            if (draft == null) { finish(); return; }
            draft.title = title;
            draft.content = content;
            draft.imageUrl = imageUrl;
            draft.category = category;
            draft.draftTime = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(new Date());
            AppDatabase.getInstance(this).postDao().updatePost(draft);
        } else {
            // 新建草稿
            draft = new Post();
            draft.title = title;
            draft.content = content;
            draft.imageUrl = imageUrl;
            draft.category = category;
            draft.authorName = nickname.isEmpty() ? "我" : nickname;
            draft.authorAvatar = avatar.isEmpty() ? "https://i.pravatar.cc/100?img=1" : avatar;
            draft.createTime = "";
            draft.isDraft = true;
            draft.draftTime = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(new Date());
            AppDatabase.getInstance(this).postDao().insertPost(draft);
        }

        ToastUtils.show(this, "已保存草稿");
        finish();
    }

    private void loadDraft(int draftId) {
        Post draft = AppDatabase.getInstance(this).postDao().getPostById(draftId);
        if (draft == null) return;
        etContent.setText(draft.content);
        etContent.setSelection(draft.content.length());

        // 设置分类
        String[] cats = {"旅行", "美食", "生活", "穿搭", "运动", "阅读", "科技", "摄影"};
        for (int i = 0; i < cats.length; i++) {
            if (cats[i].equals(draft.category)) {
                spCategory.setSelection(i);
                break;
            }
        }
    }

    // ===== 发布 =====

    private int getPostType() {
        int id = rgPostType.getCheckedRadioButtonId();
        if (id == R.id.rb_type_text) return 1;
        if (id == R.id.rb_type_video) return 2;
        return 0; // 图文
    }

    private void doPublish() {
        String content = etContent.getText().toString().trim();
        if (content.isEmpty()) {
            ToastUtils.show(this, "请输入内容");
            return;
        }

        int postType = getPostType();

        String category = spCategory.getSelectedItem().toString();
        String location = etLocation.getText().toString().trim();
        String nickname = SpUtils.getNickname(this);
        String avatar = SpUtils.getAvatar(this);
        String now = new SimpleDateFormat("MM-dd", Locale.getDefault()).format(new Date());

        String title = content.length() > 20 ? content.substring(0, 20) + "..." : content;

        String imageUrl;
        if (!selectedImageUris.isEmpty()) {
            imageUrl = copyImageToStorage(selectedImageUris.get(0));
        } else if (postType == 0) {
            String[] images = {
                    "https://images.unsplash.com/photo-1528360983277-13d401cdc186?w=400",
                    "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400",
                    "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400",
                    "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?w=400",
                    "https://images.unsplash.com/photo-1519125323398-675f0ddb6308?w=400",
                    "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=400",
                    "https://images.unsplash.com/photo-1518770660439-4636190af475?w=400",
                    "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=400"
            };
            String[] cats = {"旅行", "美食", "生活", "穿搭", "运动", "阅读", "科技", "摄影"};
            int imgIndex = 0;
            for (int i = 0; i < cats.length; i++) {
                if (cats[i].equals(category)) { imgIndex = i; break; }
            }
            imageUrl = images[imgIndex];
        } else {
            imageUrl = "";
        }

        Post post = new Post();
        if (editingPostId > 0) {
            Post draft = AppDatabase.getInstance(this).postDao().getPostById(editingPostId);
            if (draft != null) post = draft;
        }
        post.title = title;
        post.content = content;
        post.imageUrl = imageUrl;
        post.category = category;
        post.postType = postType;
        post.authorName = nickname.isEmpty() ? "我" : nickname;
        post.authorAvatar = avatar.isEmpty() ? "https://i.pravatar.cc/100?img=1" : avatar;
        post.likes = 0;
        post.comments = 0;
        post.shares = 0;
        post.createTime = now;
        post.isLiked = false;
        post.isCollected = false;
        post.isDraft = false;

        final Post finalPost = post;
        ApiService.publishPost(finalPost, created -> {
            if (created != null) {
                AppExecutors.get().diskIO().execute(() ->
                    AppDatabase.getInstance(PublishActivity.this).postDao().insertPost(created));
            } else {
                AppDatabase.getInstance(PublishActivity.this).postDao().insertPost(finalPost);
            }
            runOnUiThread(() -> {
                ToastUtils.show(PublishActivity.this, "发布成功！");
                Intent intent = new Intent();
                intent.putExtra("published", true);
                setResult(RESULT_OK, intent);
                finish();
            });
        });
    }
}
