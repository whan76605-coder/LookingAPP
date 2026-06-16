package com.example.xhsapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "posts")
public class Post {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String content;
    public String imageUrl;
    public String category;
    public String authorName;
    public String authorAvatar;
    public int likes;
    public int comments;
    public int shares;
    public boolean isLiked;
    public boolean isCollected;
    public String createTime;
    public boolean isDraft;       // 是否为草稿
    public String draftTime;      // 草稿保存时间
    public int postType;          // 0=图文, 1=纯文案, 2=视频
    public String videoResource;  // 本地视频 raw 资源名，如 "video_lvxing"

    public Post() {}

    @Ignore
    public Post(int id, String title, String content, String imageUrl,
                String category, String authorName, String authorAvatar,
                int likes, int comments, int shares, String createTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
        this.createTime = createTime;
        this.isLiked = false;
        this.isCollected = false;
        this.isDraft = false;
        this.draftTime = null;
    }
}
