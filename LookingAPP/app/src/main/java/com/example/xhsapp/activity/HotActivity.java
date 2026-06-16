package com.example.xhsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xhsapp.R;
import com.example.xhsapp.adapter.PostAdapter;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.utils.DataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 热门——按点赞数排序展示所有作品 */
public class HotActivity extends AppCompatActivity {

    private RecyclerView rvList;
    private PostAdapter adapter;
    private ImageView ivBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);

        ivBack = findViewById(R.id.iv_back);
        rvList = findViewById(R.id.rv_hot_list);
        tvTitle = findViewById(R.id.tv_hot_title);

        ivBack.setOnClickListener(v -> finish());

        String mode = getIntent().getStringExtra("mode");
        tvTitle.setText("video".equals(mode) ? "🎬 视频专区" : "🔥 热门推荐");

        rvList.setLayoutManager(new LinearLayoutManager(this));

        ApiService.getAllPosts(this, posts -> {
            if ("video".equals(mode)) {
                List<Post> videos = new ArrayList<>();
                for (Post p : posts) {
                    if (p.postType == 2) videos.add(p);
                }
                if (!videos.isEmpty()) posts = videos;
            } else {
                // 热门——按点赞数排序所有作品
                Collections.sort(posts, (a, b) -> Integer.compare(b.likes, a.likes));
            }

            List<Post> fin = new ArrayList<>(posts);
            runOnUiThread(() -> {
                adapter = new PostAdapter(this, fin);
                adapter.setOnItemClickListener(post -> {
                    Intent intent = new Intent(this, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_ID, post.id);
                    startActivity(intent);
                });
                rvList.setAdapter(adapter);
            });
        });
    }
}
