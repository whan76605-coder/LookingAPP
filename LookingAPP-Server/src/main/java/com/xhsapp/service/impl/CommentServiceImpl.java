package com.xhsapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xhsapp.entity.Comment;
import com.xhsapp.entity.Post;
import com.xhsapp.mapper.CommentMapper;
import com.xhsapp.mapper.PostMapper;
import com.xhsapp.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;

    public CommentServiceImpl(CommentMapper commentMapper, PostMapper postMapper) {
        this.commentMapper = commentMapper;
        this.postMapper = postMapper;
    }

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getPostId, postId)
                        .orderByDesc(Comment::getCreateTime));
    }

    @Override
    @Transactional
    public Comment createComment(Integer postId, Comment comment) {
        comment.setPostId(postId);
        commentMapper.insert(comment);

        // 更新帖子评论数
        Post post = postMapper.selectById(postId);
        if (post != null) {
            post.setComments(post.getComments() + 1);
            postMapper.updateById(post);
        }

        return comment;
    }

    @Override
    public void deleteComment(Integer id) {
        Comment comment = commentMapper.selectById(id);
        if (comment != null) {
            // 更新帖子评论数
            Post post = postMapper.selectById(comment.getPostId());
            if (post != null && post.getComments() > 0) {
                post.setComments(post.getComments() - 1);
                postMapper.updateById(post);
            }
            commentMapper.deleteById(id);
        }
    }
}
