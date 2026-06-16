package com.example.xhsapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xhsapp.R;
import com.example.xhsapp.adapter.MessageAdapter;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Message;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.utils.AppExecutors;
import com.example.xhsapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知中心 — 只显示 type 0-3 的系统通知（点赞/评论/粉丝/系统），不包含私信。
 * 复用 MessageAdapter 和 item_message 布局。
 */
public class NoticeActivity extends AppCompatActivity {

    private RecyclerView rvNotices;
    private TextView tvEmpty, tvClearAll;
    private ImageView ivBack;

    private MessageAdapter adapter;
    private List<Message> notices = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        ivBack = findViewById(R.id.iv_back);
        rvNotices = findViewById(R.id.rv_notices);
        tvEmpty = findViewById(R.id.tv_empty);
        tvClearAll = findViewById(R.id.tv_clear_all);

        ivBack.setOnClickListener(v -> finish());

        rvNotices.setLayoutManager(new LinearLayoutManager(this));

        tvClearAll.setOnClickListener(v -> {
            AppExecutors.get().diskIO().execute(() -> {
                AppDatabase.getInstance(this).messageDao().markAllNoticesRead();
                runOnUiThread(() -> {
                    for (Message m : notices) m.isRead = true;
                    adapter.notifyDataSetChanged();
                    ToastUtils.show(this, "全部已读");
                    setResult(RESULT_OK);
                });
            });
        });

        loadNotices();
    }

    private void loadNotices() {
        ApiService.getAllMessages(this, messages -> {
            runOnUiThread(() -> {
                notices.clear();
                if (messages != null) {
                    for (Message m : messages) {
                        if (m.type >= 0 && m.type <= 3) notices.add(m); // 只显示通知类
                    }
                }
                if (notices.isEmpty()) {
                    rvNotices.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    rvNotices.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                    adapter = new MessageAdapter(this, notices);
                    adapter.setOnItemClickListener(msg -> handleClick(msg));
                    rvNotices.setAdapter(adapter);
                }
            });
        });
    }

    private void handleClick(Message msg) {
        // 标记已读
        AppExecutors.get().diskIO().execute(() -> {
            AppDatabase.getInstance(this).messageDao().markAsRead(msg.id);
        });

        switch (msg.type) {
            case 0:
                ToastUtils.show(this, "系统通知：已读");
                break;
            case 1:
                ToastUtils.show(this, "有人赞了你");
                break;
            case 2:
                ToastUtils.show(this, "有人评论了你");
                break;
            case 3:
                ToastUtils.show(this, "你有了新粉丝");
                break;
        }
    }
}
