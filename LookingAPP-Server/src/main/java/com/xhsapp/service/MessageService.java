package com.xhsapp.service;

import com.xhsapp.entity.Message;

import java.util.List;

public interface MessageService {

    List<Message> getAllMessages();

    void markAsRead(Integer id);

    int getUnreadCount();
}
