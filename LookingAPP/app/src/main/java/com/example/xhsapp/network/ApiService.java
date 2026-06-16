package com.example.xhsapp.network;

import android.content.Context;

import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Category;
import com.example.xhsapp.model.Comment;
import com.example.xhsapp.model.Message;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.model.User;
import com.example.xhsapp.utils.AppExecutors;
import com.example.xhsapp.utils.DataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Unified data layer.
 * Uses Retrofit as HTTP client. Falls back to local DB → static mock data.
 *
 * To connect a real backend, change BASE_URL and ensure your API returns
 * the same JSON shape as the model classes (Post, Comment, Message, Category).
 */
public class ApiService {

    // 模拟器用 10.0.2.2，真机用电脑局域网IP
    public static final String BASE_URL = "http://192.168.113.26:8080/api/v1/";

    // ================================================================
    //  Posts
    // ================================================================

    public static void fetchPosts(Context ctx, DataCallback<List<Post>> cb) {
        RetrofitClient.getApi().getPosts().enqueue(new RetroCallback<List<Post>>() {
            @Override void onOk(List<Post> posts) {
                AppExecutors.get().diskIO().execute(() ->
                    AppDatabase.getInstance(ctx).postDao().insertPosts(posts));
                cb.onSuccess(posts);
            }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    public static void fetchPostDetail(Context ctx, int postId, DataCallback<Post> cb) {
        RetrofitClient.getApi().getPostDetail(postId).enqueue(new RetroCallback<Post>() {
            @Override void onOk(Post p) {
                AppExecutors.get().diskIO().execute(() ->
                    AppDatabase.getInstance(ctx).postDao().insertPost(p));
                cb.onSuccess(p);
            }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    public static void searchPosts(Context ctx, String keyword, DataCallback<List<Post>> cb) {
        RetrofitClient.getApi().searchPosts(keyword).enqueue(new RetroCallback<List<Post>>() {
            @Override void onOk(List<Post> posts) { cb.onSuccess(posts); }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    // ================================================================
    //  Posts — with local fallback
    // ================================================================

    public static void getAllPosts(Context ctx, DataCallback<List<Post>> cb) {
        fetchPosts(ctx, posts -> {
            AppExecutors.get().diskIO().execute(() -> {
                List<Post> local = AppDatabase.getInstance(ctx).postDao().getAllPosts();
                if (local != null && !local.isEmpty()) {
                    cb.onSuccess(local);
                } else {
                    cb.onSuccess(DataProvider.getMockPosts());
                }
            });
        });
    }

    public static void getPostById(Context ctx, int postId, DataCallback<Post> cb) {
        // 先从本地加载，秒开；网络请求作为后台刷新
        AppExecutors.get().diskIO().execute(() -> {
            Post local = AppDatabase.getInstance(ctx).postDao().getPostById(postId);
            if (local != null) {
                cb.onSuccess(local);
            } else {
                for (Post p : DataProvider.getMockPosts()) {
                    if (p.id == postId) {
                        cb.onSuccess(p);
                        break;
                    }
                }
            }
        });
    }

    public static void searchPostsWithFallback(Context ctx, String keyword, DataCallback<List<Post>> cb) {
        searchPosts(ctx, keyword, posts -> {
            if (posts != null && !posts.isEmpty()) {
                cb.onSuccess(posts);
            } else {
                AppExecutors.get().diskIO().execute(() -> {
                    List<Post> local = AppDatabase.getInstance(ctx).postDao().searchPosts(keyword);
                    if (local != null && !local.isEmpty()) {
                        cb.onSuccess(local);
                    } else {
                        List<Post> all = DataProvider.getMockPosts();
                        List<Post> result = new ArrayList<>();
                        for (Post p : all) {
                            if (p.title.contains(keyword) || p.content.contains(keyword)
                                    || p.category.contains(keyword) || p.authorName.contains(keyword)) {
                                result.add(p);
                            }
                        }
                        cb.onSuccess(result);
                    }
                });
            }
        });
    }

    public static void publishPost(Post post, DataCallback<Post> cb) {
        RetrofitClient.getApi().createPost(post).enqueue(new RetroCallback<Post>() {
            @Override void onOk(Post created) { cb.onSuccess(created); }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    public static void likePost(int postId, DataCallback<Void> cb) {
        RetrofitClient.getApi().likePost(postId).enqueue(new RetroCallback<Void>() {
            @Override void onOk(Void v) { cb.onSuccess(v); }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    public static void collectPost(int postId, DataCallback<Void> cb) {
        RetrofitClient.getApi().collectPost(postId).enqueue(new RetroCallback<Void>() {
            @Override void onOk(Void v) { cb.onSuccess(v); }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    // ================================================================
    //  Comments
    // ================================================================

    public static void fetchComments(Context ctx, int postId, DataCallback<List<Comment>> cb) {
        RetrofitClient.getApi().getComments(postId).enqueue(new RetroCallback<List<Comment>>() {
            @Override void onOk(List<Comment> comments) {
                AppExecutors.get().diskIO().execute(() -> {
                    AppDatabase.getInstance(ctx).commentDao().deleteByPostId(postId);
                    for (Comment c : comments)
                        AppDatabase.getInstance(ctx).commentDao().insertComment(c);
                });
                cb.onSuccess(comments);
            }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    public static void getCommentsForPost(Context ctx, int postId, DataCallback<List<Comment>> cb) {
        fetchComments(ctx, postId, comments -> {
            if (comments != null && !comments.isEmpty()) {
                cb.onSuccess(comments);
            } else {
                AppExecutors.get().diskIO().execute(() -> {
                    List<Comment> local = AppDatabase.getInstance(ctx).commentDao().getCommentsByPostId(postId);
                    if (local != null && !local.isEmpty()) {
                        cb.onSuccess(local);
                    } else {
                        cb.onSuccess(DataProvider.getComments(postId));
                    }
                });
            }
        });
    }

    public static void sendComment(int postId, Comment comment, DataCallback<Comment> cb) {
        RetrofitClient.getApi().createComment(postId, comment).enqueue(new RetroCallback<Comment>() {
            @Override void onOk(Comment created) { cb.onSuccess(created); }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    // ================================================================
    //  Messages
    // ================================================================

    public static void fetchMessages(Context ctx, DataCallback<List<Message>> cb) {
        RetrofitClient.getApi().getMessages().enqueue(new RetroCallback<List<Message>>() {
            @Override void onOk(List<Message> messages) {
                AppExecutors.get().diskIO().execute(() -> {
                    AppDatabase.getInstance(ctx).messageDao().deleteAll();
                    AppDatabase.getInstance(ctx).messageDao().insertMessages(messages);
                });
                cb.onSuccess(messages);
            }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    public static void getAllMessages(Context ctx, DataCallback<List<Message>> cb) {
        fetchMessages(ctx, messages -> {
            if (messages != null && !messages.isEmpty()) {
                cb.onSuccess(messages);
            } else {
                AppExecutors.get().diskIO().execute(() -> {
                    List<Message> local = AppDatabase.getInstance(ctx).messageDao().getAllMessages();
                    if (local != null && !local.isEmpty()) {
                        cb.onSuccess(local);
                    } else {
                        cb.onSuccess(DataProvider.getMessages());
                    }
                });
            }
        });
    }

    // ================================================================
    //  Categories
    // ================================================================

    public static void fetchCategories(DataCallback<List<Category>> cb) {
        RetrofitClient.getApi().getCategories().enqueue(new RetroCallback<List<Category>>() {
            @Override void onOk(List<Category> cats) { cb.onSuccess(cats); }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    public static void getCategories(DataCallback<List<Category>> cb) {
        fetchCategories(cats -> {
            if (cats != null && !cats.isEmpty()) {
                cb.onSuccess(cats);
            } else {
                cb.onSuccess(DataProvider.getCategories());
            }
        });
    }

    // ================================================================
    //  Auth
    // ================================================================

    public static void login(String username, String password, DataCallback<User> cb) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        RetrofitClient.getApi().login(body).enqueue(new RetroCallback<User>() {
            @Override void onOk(User user) { cb.onSuccess(user); }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    public static void register(String username, String password, String nickname, DataCallback<User> cb) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        body.put("nickname", nickname);
        RetrofitClient.getApi().register(body).enqueue(new RetroCallback<User>() {
            @Override void onOk(User user) { cb.onSuccess(user); }
            @Override void onFail() { cb.onSuccess(null); }
        });
    }

    // ================================================================
    //  DataCallback
    // ================================================================

    public interface DataCallback<T> {
        void onSuccess(T data);
    }

    // ================================================================
    //  Retrofit callback adapter (internal)
    // ================================================================

    private abstract static class RetroCallback<T> implements Callback<ApiResponse<T>> {
        abstract void onOk(T data);
        abstract void onFail();

        @Override
        public void onResponse(Call<ApiResponse<T>> call, Response<ApiResponse<T>> response) {
            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                T data = response.body().data;
                if (data != null) {
                    onOk(data);
                } else {
                    onFail();
                }
            } else {
                onFail();
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<T>> call, Throwable t) {
            onFail();
        }
    }
}
