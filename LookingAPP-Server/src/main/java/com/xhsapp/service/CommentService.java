package com.xhsapp.service;

import com.xhsapp.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getCommentsByPostId(Integer postId);

    Comment createComment(Integer postId, Comment comment);

    void deleteComment(Integer id);
}
