package com.example.xhsapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.xhsapp.model.Comment;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(List<Comment> comments);

    @Delete
    void deleteComment(Comment comment);

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY id DESC")
    List<Comment> getCommentsByPostId(int postId);

    @Query("SELECT * FROM comments ORDER BY id DESC")
    List<Comment> getAllComments();

    @Query("UPDATE comments SET likes = :likes WHERE id = :commentId")
    void updateCommentLikes(int commentId, int likes);

    @Query("DELETE FROM comments")
    void deleteAll();

    @Query("DELETE FROM comments WHERE postId = :postId")
    void deleteByPostId(int postId);
}
