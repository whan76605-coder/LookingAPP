package com.example.xhsapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String username;
    public String password;  // MD5 hashed
    public String nickname;
    public String avatar;
    public String bio;
    public int followCount;
    public int fansCount;
    public int likeCount;
    public String email;
    public String phone;
    public long createTime;

    public User() {}

    @Ignore
    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.bio = "热爱生活，热爱旅行";
        this.followCount = 128;
        this.fansCount = 2356;
        this.likeCount = 5687;
        this.createTime = System.currentTimeMillis();
    }
}
