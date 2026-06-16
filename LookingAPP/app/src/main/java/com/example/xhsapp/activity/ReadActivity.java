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
import java.util.List;

/** 阅读——显示纯文案作品（postType=1） */
public class ReadActivity extends AppCompatActivity {

    private RecyclerView rvList;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        ivBack = findViewById(R.id.iv_back);
        rvList = findViewById(R.id.rv_read_list);

        ivBack.setOnClickListener(v -> finish());
        rvList.setLayoutManager(new LinearLayoutManager(this));

        ApiService.getAllPosts(this, posts -> {
            List<Post> readingPosts = new ArrayList<>();
            for (Post p : posts) {
                if (p.postType == 1) {
                    readingPosts.add(p);
                }
            }
            if (readingPosts.isEmpty()) readingPosts = posts;

            List<Post> fin = new ArrayList<>(readingPosts);
            runOnUiThread(() -> {
                PostAdapter adapter = new PostAdapter(this, fin, false);
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
