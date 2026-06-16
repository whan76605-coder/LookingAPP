package com.xhsapp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xhsapp.common.Result;
import com.xhsapp.entity.Post;
import com.xhsapp.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /** GET /api/v1/posts  OR  GET /api/v1/posts?page=1&size=10 */
    @GetMapping
    public Result<List<Post>> getPosts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "0") int size) {
        if (page > 0 && size > 0) {
            // 分页
            Page<Post> result = postService.getPostsByPage(page, size);
            return Result.ok(result.getRecords());
        }
        return Result.ok(postService.getAllPosts());
    }

    /** GET /api/v1/posts/{id} */
    @GetMapping("/{id}")
    public Result<Post> getPostById(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        if (post == null) return Result.fail("帖子不存在");
        return Result.ok(post);
    }

    /** GET /api/v1/posts/search?q= */
    @GetMapping("/search")
    public Result<List<Post>> searchPosts(@RequestParam String q) {
        return Result.ok(postService.searchPosts(q));
    }

    /** POST /api/v1/posts */
    @PostMapping
    public Result<Post> createPost(@RequestBody Post post) {
        return Result.ok(postService.createPost(post));
    }

    /** PUT /api/v1/posts/{id}/like */
    @PutMapping("/{id}/like")
    public Result<Void> likePost(@PathVariable Integer id) {
        postService.likePost(id);
        return Result.ok();
    }

    /** PUT /api/v1/posts/{id}/collect */
    @PutMapping("/{id}/collect")
    public Result<Void> collectPost(@PathVariable Integer id) {
        postService.collectPost(id);
        return Result.ok();
    }
}
