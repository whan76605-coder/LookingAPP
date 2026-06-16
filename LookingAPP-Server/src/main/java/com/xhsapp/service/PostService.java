package com.xhsapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhsapp.entity.Post;

import java.util.List;

public interface PostService {

    List<Post> getAllPosts();

    Page<Post> getPostsByPage(int page, int size);

    Post getPostById(Integer id);

    List<Post> searchPosts(String keyword);

    Post createPost(Post post);

    void likePost(Integer id);

    void collectPost(Integer id);

    /** 点赞数+1 */
    void incrementLikes(Integer id);

    /** 点赞数-1 */
    void decrementLikes(Integer id);
}
