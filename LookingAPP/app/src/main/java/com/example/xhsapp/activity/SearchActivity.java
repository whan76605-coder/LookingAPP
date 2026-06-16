package com.example.xhsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.xhsapp.R;
import com.example.xhsapp.adapter.PostAdapter;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.utils.DataProvider;
import com.example.xhsapp.utils.SpUtils;
import com.example.xhsapp.utils.ToastUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ImageView ivBack, ivClear;
    private EditText etSearch;
    private ChipGroup cgHistory, cgHot;
    private RecyclerView rvResult;
    private TextView tvClearHistory;
    private PostAdapter postAdapter;

// allPosts no longer needed — ApiService handles it

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        loadAllPosts();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivClear = findViewById(R.id.iv_clear);
        etSearch = findViewById(R.id.et_search);
        cgHistory = findViewById(R.id.cg_history);
        cgHot = findViewById(R.id.cg_hot);
        rvResult = findViewById(R.id.rv_result);
        tvClearHistory = findViewById(R.id.tv_clear_history);

        ivBack.setOnClickListener(v -> finish());
        ivClear.setOnClickListener(v -> {
            etSearch.setText("");
            etSearch.requestFocus();
        });
        tvClearHistory.setOnClickListener(v -> {
            SpUtils.clearSearchHistory(this);
            cgHistory.removeAllViews();
            ToastUtils.show(this, "已清除搜索历史");
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                doSearch(etSearch.getText().toString().trim());
                return true;
            }
            return false;
        });

        rvResult.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
    }

    private void loadAllPosts() {
        ApiService.getAllPosts(this, posts -> {
            runOnUiThread(() -> {
                loadHistory();
                loadHot();
            });
        });
    }

    private void loadHistory() {
        String history = SpUtils.getSearchHistory(this);
        if (TextUtils.isEmpty(history)) return;
        for (String item : history.split(",")) {
            if (!TextUtils.isEmpty(item)) addChip(cgHistory, item, true);
        }
    }

    private void loadHot() {
        for (String h : DataProvider.getHotSearch()) {
            addChip(cgHot, h, false);
        }
    }

    private void addChip(ChipGroup group, String text, boolean closable) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCloseIconVisible(closable);
        chip.setOnClickListener(v -> doSearch(text));
        if (closable) {
            chip.setOnCloseIconClickListener(v -> group.removeView(chip));
        }
        group.addView(chip);
    }

    private void doSearch(String keyword) {
        if (TextUtils.isEmpty(keyword)) return;
        saveHistory(keyword);

        ApiService.searchPostsWithFallback(this, keyword, result -> {
            runOnUiThread(() -> {
                if (postAdapter == null) {
                    postAdapter = new PostAdapter(this, result);
                    postAdapter.setOnItemClickListener(post -> {
                        Intent intent = new Intent(this, PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, post.id);
                        startActivity(intent);
                    });
                    rvResult.setAdapter(postAdapter);
                } else {
                    postAdapter.updateData(result);
                }

                ToastUtils.show(this, result.isEmpty() ? "未找到相关内容" :
                        "找到 " + result.size() + " 条结果");
            });
        });
    }

    private void saveHistory(String keyword) {
        String old = SpUtils.getSearchHistory(this);
        List<String> list = new ArrayList<>(Arrays.asList(old.split(",")));
        list.remove(keyword);
        list.add(0, keyword);
        if (list.size() > 10) list = list.subList(0, 10);
        SpUtils.saveSearchHistory(this, String.join(",", list));
    }
}
