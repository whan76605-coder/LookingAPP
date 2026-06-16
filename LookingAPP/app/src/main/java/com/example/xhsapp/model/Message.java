package com.example.xhsapp.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int type; // 0=系统通知 1=点赞 2=评论 3=粉丝 4=私信
    public String title;
    public String content;
    public String avatar;
    public String time;
    public boolean isRead;

    public Message() {}

    @Ignore
    public Message(int id, int type, String title, String content,
                   String avatar, String time) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.avatar = avatar;
        this.time = time;
        this.isRead = false;
    }
}
