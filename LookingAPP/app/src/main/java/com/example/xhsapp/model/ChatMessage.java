package com.example.xhsapp.model;

public class ChatMessage {
    public String content;
    public boolean isUser;
    public String time;

    public ChatMessage(String content, boolean isUser, String time) {
        this.content = content;
        this.isUser = isUser;
        this.time = time;
    }
}
