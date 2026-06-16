package com.xhsapp.controller;

import com.xhsapp.common.Result;
import com.xhsapp.entity.Comment;
import com.xhsapp.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /** GET /api/v1/posts/{id}/comments */
    @GetMapping("/posts/{id}/comments")
    public Result<List<Comment>> getComments(@PathVariable Integer id) {
        return Result.ok(commentService.getCommentsByPostId(id));
    }

    /** POST /api/v1/posts/{id}/comments */
    @PostMapping("/posts/{id}/comments")
    public Result<Comment> createComment(@PathVariable Integer id,
                                         @RequestBody Comment comment) {
        return Result.ok(commentService.createComment(id, comment));
    }

    /** DELETE /api/v1/comments/{id} */
    @DeleteMapping("/comments/{id}")
    public Result<Void> deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return Result.ok();
    }
}
