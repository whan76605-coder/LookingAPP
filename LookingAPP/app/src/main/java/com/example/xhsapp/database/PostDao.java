package com.example.xhsapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xhsapp.model.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(Post post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPosts(List<Post> posts);

    @Update
    void updatePost(Post post);

    @Delete
    void deletePost(Post post);

    @Query("SELECT * FROM posts WHERE isDraft = 0 ORDER BY id DESC")
    List<Post> getAllPosts();

    @Query("SELECT * FROM posts WHERE isCollected = 1 AND isDraft = 0 ORDER BY id DESC")
    List<Post> getCollectedPosts();

    @Query("SELECT * FROM posts WHERE id = :postId")
    Post getPostById(int postId);

    @Query("SELECT * FROM posts WHERE category = :category ORDER BY id DESC")
    List<Post> getPostsByCategory(String category);

    @Query("UPDATE posts SET isLiked = :liked, likes = :likeCount WHERE id = :postId")
    void updateLike(int postId, boolean liked, int likeCount);

    @Query("UPDATE posts SET isCollected = :collected WHERE id = :postId")
    void updateCollect(int postId, boolean collected);

    @Query("DELETE FROM posts")
    void deleteAll();

    @Query("DELETE FROM posts WHERE isDraft = 0")
    void deleteAllRemotePosts();

    // ===== 草稿 =====
    @Query("SELECT * FROM posts WHERE isDraft = 1 ORDER BY id DESC")
    List<Post> getDrafts();

    // ===== 搜索 =====
    @Query("SELECT * FROM posts WHERE (title LIKE '%' || :keyword || '%' OR content LIKE '%' || :keyword || '%' OR category LIKE '%' || :keyword || '%') AND isDraft = 0 ORDER BY id DESC")
    List<Post> searchPosts(String keyword);
}
