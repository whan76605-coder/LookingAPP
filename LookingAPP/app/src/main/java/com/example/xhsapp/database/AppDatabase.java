package com.example.xhsapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.xhsapp.model.Comment;
import com.example.xhsapp.model.Message;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.model.User;
import com.example.xhsapp.utils.DataProvider;

import java.util.List;

@Database(entities = {Post.class, User.class, Comment.class, Message.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract PostDao postDao();
    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
    public abstract MessageDao messageDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "xhs_database"
                    ).allowMainThreadQueries()
                     .fallbackToDestructiveMigration()
                     .build();
                }
            }
        }
        return INSTANCE;
    }

    /** 预填充数据（首次启动时调用） */
    public static void prepopulateIfEmpty(Context context) {
        AppDatabase db = getInstance(context);
        new Thread(() -> {
            // 预填充帖子（不再从 DataProvider，走 ApiService fallback 链）
            if (db.postDao().getAllPosts().isEmpty()) {
                List<Post> mockPosts = DataProvider.getMockPosts();
                db.postDao().insertPosts(mockPosts);
            }
            // 预填充评论
            if (db.commentDao().getAllComments().isEmpty()) {
                for (int i = 1; i <= 42; i++) {
                    db.commentDao().insertComments(DataProvider.getComments(i));
                }
            }
            // 预填充消息
            if (db.messageDao().getAllMessages().isEmpty()) {
                db.messageDao().insertMessages(DataProvider.getMessages());
            }
        }).start();
    }

    /** 更新缓存数据（从网络拉取成功后调用） */
    public static void updateCache(Context context, List<Post> posts) {
        AppDatabase db = getInstance(context);
        new Thread(() -> {
            db.postDao().deleteAll();
            db.postDao().insertPosts(posts);
        }).start();
    }
}
