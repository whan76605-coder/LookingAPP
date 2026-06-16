package com.xhsapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhsapp.entity.Post;
import com.xhsapp.mapper.PostMapper;
import com.xhsapp.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public List<Post> getAllPosts() {
        return postMapper.selectList(
                new LambdaQueryWrapper<Post>().orderByDesc(Post::getCreateTime));
    }

    @Override
    public Page<Post> getPostsByPage(int page, int size) {
        Page<Post> p = new Page<>(page, size);
        return postMapper.selectPage(p,
                new LambdaQueryWrapper<Post>().orderByDesc(Post::getCreateTime));
    }

    @Override
    public Post getPostById(Integer id) {
        return postMapper.selectById(id);
    }

    @Override
    public List<Post> searchPosts(String keyword) {
        return postMapper.selectList(
                new LambdaQueryWrapper<Post>()
                        .like(Post::getTitle, keyword)
                        .or().like(Post::getContent, keyword)
                        .or().like(Post::getCategory, keyword)
                        .or().like(Post::getAuthorName, keyword)
                        .orderByDesc(Post::getCreateTime));
    }

    @Override
    public Post createPost(Post post) {
        postMapper.insert(post);
        return post;
    }

    @Override
    public void likePost(Integer id) {
        Post post = postMapper.selectById(id);
        if (post != null) {
            post.setIsLiked(!Boolean.TRUE.equals(post.getIsLiked()));
            if (post.getIsLiked()) {
                post.setLikes(post.getLikes() + 1);
            } else {
                post.setLikes(Math.max(0, post.getLikes() - 1));
            }
            postMapper.updateById(post);
        }
    }

    @Override
    public void collectPost(Integer id) {
        Post post = postMapper.selectById(id);
        if (post != null) {
            post.setIsCollected(!Boolean.TRUE.equals(post.getIsCollected()));
            postMapper.updateById(post);
        }
    }

    @Override
    public void incrementLikes(Integer id) {
        Post post = postMapper.selectById(id);
        if (post != null) {
            post.setLikes(post.getLikes() + 1);
            postMapper.updateById(post);
        }
    }

    @Override
    public void decrementLikes(Integer id) {
        Post post = postMapper.selectById(id);
        if (post != null && post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
            postMapper.updateById(post);
        }
    }
}
