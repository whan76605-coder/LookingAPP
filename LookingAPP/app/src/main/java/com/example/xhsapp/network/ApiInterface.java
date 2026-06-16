package com.example.xhsapp.network;

import com.example.xhsapp.model.Category;
import com.example.xhsapp.model.Comment;
import com.example.xhsapp.model.Message;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    // ==================== 帖子 ====================

    /** 获取所有帖子 */
    @GET("posts")
    Call<ApiResponse<List<Post>>> getPosts();

    /** 分页获取帖子 */
    @GET("posts")
    Call<ApiResponse<List<Post>>> getPostsByPage(@Query("page") int page, @Query("size") int size);

    /** 获取帖子详情 */
    @GET("posts/{id}")
    Call<ApiResponse<Post>> getPostDetail(@Path("id") int postId);

    /** 搜索帖子 */
    @GET("posts/search")
    Call<ApiResponse<List<Post>>> searchPosts(@Query("q") String keyword);

    /** 发布帖子 */
    @POST("posts")
    Call<ApiResponse<Post>> createPost(@Body Post post);

    /** 点赞 */
    @PUT("posts/{id}/like")
    Call<ApiResponse<Void>> likePost(@Path("id") int postId);

    /** 收藏 */
    @PUT("posts/{id}/collect")
    Call<ApiResponse<Void>> collectPost(@Path("id") int postId);

    // ==================== 评论 ====================

    /** 获取帖子的评论 */
    @GET("posts/{id}/comments")
    Call<ApiResponse<List<Comment>>> getComments(@Path("id") int postId);

    /** 发表评论 */
    @POST("posts/{id}/comments")
    Call<ApiResponse<Comment>> createComment(@Path("id") int postId, @Body Comment comment);

    // ==================== 消息 ====================

    /** 获取所有消息 */
    @GET("messages")
    Call<ApiResponse<List<Message>>> getMessages();

    /** 标记消息已读 */
    @PUT("messages/{id}/read")
    Call<ApiResponse<Void>> markMessageRead(@Path("id") int msgId);

    // ==================== 分类 ====================

    /** 获取分类列表 */
    @GET("categories")
    Call<ApiResponse<List<Category>>> getCategories();

    // ==================== 认证 ====================

    @POST("auth/login")
    Call<ApiResponse<User>> login(@Body Map<String, String> body);

    @POST("auth/register")
    Call<ApiResponse<User>> register(@Body Map<String, String> body);
}
