package com.example.xhsapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.xhsapp.model.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<Message> messages);

    @Query("SELECT * FROM messages ORDER BY id DESC")
    List<Message> getAllMessages();

    @Query("SELECT * FROM messages WHERE type = :type ORDER BY id DESC")
    List<Message> getMessagesByType(int type);

    @Query("UPDATE messages SET isRead = 1 WHERE id = :msgId")
    void markAsRead(int msgId);

    @Query("SELECT COUNT(*) FROM messages WHERE isRead = 0")
    int getUnreadCount();

    @Query("UPDATE messages SET isRead = 1 WHERE isRead = 0")
    void markAllNoticesRead();

    @Query("DELETE FROM messages")
    void deleteAll();
}
