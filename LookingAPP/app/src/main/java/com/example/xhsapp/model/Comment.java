package com.example.xhsapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int postId;
    public String authorName;
    public String authorAvatar;
    public String content;
    public int likes;
    public String createTime;

    public Comment() {}

    @Ignore
    public Comment(int id, int postId, String authorName, String authorAvatar,
                   String content, int likes, String createTime) {
        this.id = id;
        this.postId = postId;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.content = content;
        this.likes = likes;
        this.createTime = createTime;
    }
}
