package com.example.xhsapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xhsapp.R;
import com.example.xhsapp.adapter.CommentAdapter;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Comment;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.utils.AppExecutors;
import com.example.xhsapp.utils.SpUtils;
import com.example.xhsapp.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST_ID = "post_id";

    private ImageView ivBack, ivCover, ivAuthorAvatar, ivLike, ivCollect, ivShare, ivMore, ivPlayPause;
    private FrameLayout flVideoContainer;
    private VideoView vvPlayer;
    private LinearLayout llVideoControls;
    private SeekBar sbProgress, sbVolume;
    private TextView tvCurrentTime, tvTotalTime, tvSpeed;
    private TextView tvAuthorName, tvCategory, tvTitle, tvContent, tvCreateTime;
    private TextView tvLikeCount, tvCommentCount, tvShareCount, tvFollowBtn;
    private RecyclerView rvComments;
    private EditText etComment;
    private TextView tvSend;
    private CommentAdapter commentAdapter;

    private Post post;
    private int postId;
    private boolean isPlaying;
    private float speed = 1.0f;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable progressRunnable, hideControlsRunnable;
    private AudioManager audioManager;
    private int maxVolume;

    private GestureDetector gestureDetector;
    private boolean isAdjusting;
    private float startY, startX;
    private int startVolume;
    private int gestureSide; // 0=none, -1=left(brightness), 1=right(volume)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postId = getIntent().getIntExtra(EXTRA_POST_ID, -1);
        if (postId == -1) { finish(); return; }

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        initViews();
        loadPost();
    }

    private void initViews() {
        ivBack = findViewById(R.id.iv_back);
        ivCover = findViewById(R.id.iv_cover);
        flVideoContainer = findViewById(R.id.fl_video_container);
        vvPlayer = findViewById(R.id.vv_player);
        ivPlayPause = findViewById(R.id.iv_play_pause);
        llVideoControls = findViewById(R.id.ll_video_controls);
        sbProgress = findViewById(R.id.sb_progress);
        sbVolume = findViewById(R.id.sb_volume);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvTotalTime = findViewById(R.id.tv_total_time);
        tvSpeed = findViewById(R.id.tv_speed);
        ivAuthorAvatar = findViewById(R.id.iv_author_avatar);
        ivLike = findViewById(R.id.iv_like);
        ivCollect = findViewById(R.id.iv_collect);
        ivShare = findViewById(R.id.iv_share);
        ivMore = findViewById(R.id.iv_more);
        tvAuthorName = findViewById(R.id.tv_author_name);
        tvCategory = findViewById(R.id.tv_category);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvCreateTime = findViewById(R.id.tv_create_time);
        tvLikeCount = findViewById(R.id.tv_like_count);
        tvCommentCount = findViewById(R.id.tv_comment_count);
        tvShareCount = findViewById(R.id.tv_share_count);
        tvFollowBtn = findViewById(R.id.tv_follow_btn);
        rvComments = findViewById(R.id.rv_comments);
        etComment = findViewById(R.id.et_comment);
        tvSend = findViewById(R.id.tv_send);

        ivBack.setOnClickListener(v -> { setResult(RESULT_OK); finish(); });
        ivCover.setOnClickListener(v -> {
            if (post != null && post.postType != 2) showFullScreenImage();
        });

        setupVideoControls();
    }

    // ==================== 视频控制 ====================

    private void setupVideoControls() {
        // Volume seekbar
        int curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sbVolume.setMax(maxVolume);
        sbVolume.setProgress(curVol);
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar sb, int p, boolean fromUser) {
                if (fromUser) audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, p, 0);
            }
            @Override public void onStartTrackingTouch(SeekBar sb) {}
            @Override public void onStopTrackingTouch(SeekBar sb) {}
        });

        // Progress seekbar
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar sb, int p, boolean fromUser) {
                if (fromUser) vvPlayer.seekTo(p);
            }
            @Override public void onStartTrackingTouch(SeekBar sb) { handler.removeCallbacks(hideControlsRunnable); }
            @Override public void onStopTrackingTouch(SeekBar sb) { resetHideControls(); }
        });

        // Play/pause
        ivPlayPause.setOnClickListener(v -> togglePlayPause());

        // Tap anywhere to toggle controls
        flVideoContainer.setOnClickListener(v -> {
            if (llVideoControls.getVisibility() == View.VISIBLE) {
                hideControls();
            } else {
                showControls();
            }
        });

        // Speed
        tvSpeed.setOnClickListener(v -> cycleSpeed());

        // Gesture — volume/brightness on vertical swipe
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
                if (e1 == null || e2 == null) return false;
                float w = flVideoContainer.getWidth();
                float x = e1.getX();
                if (x < w * 0.4f) return false; // left side: reserved for brightness (not implemented)
                if (!isAdjusting) {
                    isAdjusting = true;
                    startY = e1.getY();
                    startVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                }
                float delta = (startY - e2.getY()) / flVideoContainer.getHeight();
                int newVol = Math.min(maxVolume, Math.max(0, startVolume + (int)(delta * maxVolume)));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVol, 0);
                sbVolume.setProgress(newVol);
                return true;
            }
        });
        gestureDetector.setIsLongpressEnabled(false);

        flVideoContainer.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!isAdjusting) {
                    // 普通单击 → 切换控制栏
                    if (llVideoControls.getVisibility() == View.VISIBLE) {
                        hideControls();
                    } else {
                        showControls();
                    }
                }
                isAdjusting = false;
            }
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }

    private void togglePlayPause() {
        if (vvPlayer == null) return;
        if (isPlaying) {
            vvPlayer.pause();
            isPlaying = false;
            ivPlayPause.setImageResource(R.drawable.ic_play);
            stopProgressUpdater();
        } else {
            vvPlayer.start();
            isPlaying = true;
            ivPlayPause.setImageResource(R.drawable.ic_pause);
            startProgressUpdater();
        }
        resetHideControls();
    }

    private void cycleSpeed() {
        if (speed < 1f) speed = 1.0f;
        else if (speed < 1.5f) speed = 1.5f;
        else if (speed < 2f) speed = 2.0f;
        else speed = 0.5f;
        tvSpeed.setText(speed == 1f ? "1x" : speed == 0.5f ? "0.5x" : speed == 1.5f ? "1.5x" : "2x");
        try {
            java.lang.reflect.Method m = vvPlayer.getClass()
                    .getMethod("setPlaybackParams", android.media.PlaybackParams.class);
            m.invoke(vvPlayer, new android.media.PlaybackParams().setSpeed(speed));
        } catch (Exception ignored) {}
        resetHideControls();
    }

    private void showControls() {
        llVideoControls.setVisibility(View.VISIBLE);
        ivPlayPause.setVisibility(View.VISIBLE);
        resetHideControls();
    }

    private void hideControls() {
        llVideoControls.setVisibility(View.GONE);
        ivPlayPause.setVisibility(View.GONE);
        if (hideControlsRunnable != null) handler.removeCallbacks(hideControlsRunnable);
    }

    private void resetHideControls() {
        handler.removeCallbacks(hideControlsRunnable);
        hideControlsRunnable = () -> hideControls();
        handler.postDelayed(hideControlsRunnable, 3000);
    }

    private void startProgressUpdater() {
        stopProgressUpdater();
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (vvPlayer != null && isPlaying) {
                    int pos = vvPlayer.getCurrentPosition();
                    int dur = vvPlayer.getDuration();
                    if (dur > 0) {
                        sbProgress.setMax(dur);
                        sbProgress.setProgress(pos);
                        tvCurrentTime.setText(formatTime(pos));
                        tvTotalTime.setText(formatTime(dur));
                    }
                }
                handler.postDelayed(this, 300);
            }
        };
        handler.post(progressRunnable);
    }

    private void stopProgressUpdater() {
        if (progressRunnable != null) handler.removeCallbacks(progressRunnable);
    }

    private static String formatTime(int ms) {
        int s = ms / 1000;
        return String.format("%02d:%02d", s / 60, s % 60);
    }

    // ==================== 数据 ====================

    private void loadPost() {
        ApiService.getPostById(this, postId, p -> {
            runOnUiThread(() -> {
                if (p == null || isFinishing() || isDestroyed()) { finish(); return; }
                post = p;
                bindData();
                loadComments();
            });
        });
    }

    private void bindData() {
        boolean isVideo = post.postType == 2 && post.videoResource != null && !post.videoResource.isEmpty();

        if (isVideo) {
            ivCover.setVisibility(View.GONE);
            flVideoContainer.setVisibility(View.VISIBLE);

            int resId = getResources().getIdentifier(post.videoResource, "raw", getPackageName());
            if (resId != 0) {
                String path = "android.resource://" + getPackageName() + "/" + resId;
                vvPlayer.setVideoURI(Uri.parse(path));
                vvPlayer.setOnPreparedListener(mp -> {
                    mp.setLooping(true);
                    sbProgress.setMax(mp.getDuration());
                    tvTotalTime.setText(formatTime(mp.getDuration()));
                    isPlaying = true;
                    ivPlayPause.setImageResource(R.drawable.ic_pause);
                    startProgressUpdater();
                    vvPlayer.start();
                    showControls();
                });
                vvPlayer.setOnCompletionListener(mp -> {
                    isPlaying = false;
                    ivPlayPause.setImageResource(R.drawable.ic_play);
                    stopProgressUpdater();
                });
                vvPlayer.setOnErrorListener((mp, what, extra) -> {
                    flVideoContainer.setVisibility(View.GONE);
                    ivCover.setVisibility(View.VISIBLE);
                    loadCoverImage();
                    return true;
                });
            }
        } else if (post.imageUrl != null && !post.imageUrl.isEmpty()) {
            ivCover.setVisibility(View.VISIBLE);
            flVideoContainer.setVisibility(View.GONE);
            loadCoverImage();
        } else {
            ivCover.setVisibility(View.GONE);
            flVideoContainer.setVisibility(View.GONE);
        }

        Glide.with(this).load(post.authorAvatar)
                .placeholder(R.drawable.ic_default_avatar).circleCrop().into(ivAuthorAvatar);

        tvAuthorName.setText(post.authorName);
        tvCategory.setText(post.category);
        tvTitle.setText(post.title);
        tvContent.setText(post.content);
        tvCreateTime.setText("发布于 " + post.createTime);
        tvLikeCount.setText(formatCount(post.likes));
        tvCommentCount.setText(formatCount(post.comments));
        tvShareCount.setText(String.valueOf(post.shares));

        updateFollowUI(); updateLikeUI(); updateCollectUI();

        View.OnClickListener author = v -> showAuthorInfo();
        ivAuthorAvatar.setOnClickListener(author);
        tvAuthorName.setOnClickListener(author);
        tvFollowBtn.setOnClickListener(v -> toggleFollow());
        ivLike.setOnClickListener(v -> toggleLike());
        ivCollect.setOnClickListener(v -> toggleCollect());
        ivShare.setOnClickListener(v -> doShare());
        tvSend.setOnClickListener(v -> sendComment());
        ivMore.setOnClickListener(v -> showPostMenu());
        checkOwnPost();
    }

    // ==================== 原有业务（不变） ====================

    private void checkOwnPost() {
        if (post == null || ivMore == null) return;
        String n = SpUtils.getNickname(this);
        ivMore.setVisibility(n != null && n.equals(post.authorName) ? View.VISIBLE : View.GONE);
    }
    private void showPostMenu() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{"删除"}, (d, w) -> { if (w == 0) confirmDeletePost(); })
                .setNegativeButton("取消", null).show();
    }
    private void confirmDeletePost() {
        new AlertDialog.Builder(this).setTitle("确认删除").setMessage("删除后无法恢复，确定要删除这篇作品吗？")
                .setPositiveButton("删除", (d, w) -> {
                    AppExecutors.get().diskIO().execute(() -> {
                        AppDatabase.getInstance(this).postDao().deletePost(post);
                        runOnUiThread(() -> { ToastUtils.show(this, "已删除"); setResult(RESULT_OK); finish(); });
                    });
                }).setNegativeButton("取消", null).show();
    }
    private void showFullScreenImage() {
        Dialog d = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_fullscreen_image);
        d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageView f = d.findViewById(R.id.iv_fullscreen), c = d.findViewById(R.id.iv_fullscreen_close);
        Glide.with(this).load(post.imageUrl).placeholder(R.drawable.bg_placeholder).into(f);
        c.setOnClickListener(v -> d.dismiss()); f.setOnClickListener(v -> d.dismiss());
        d.show();
    }
    private void loadCoverImage() {
        Glide.with(this).load(post.imageUrl).placeholder(R.drawable.bg_placeholder).into(ivCover);
    }
    private void showAuthorInfo() {
        boolean fd = SpUtils.isPostAuthorFollowed(this, post.authorName);
        new AlertDialog.Builder(this).setTitle(post.authorName)
                .setMessage("粉丝: " + formatCount(2356) + "  获赞: " + formatCount(5687) + "\n\n他的作品: " + post.title + " 等多篇笔记")
                .setPositiveButton(fd ? "已关注" : "关注", (d, w) -> { if (!fd) toggleFollow(); })
                .setNeutralButton("查看主页", (d, w) -> ToastUtils.show(this, "作者主页开发中"))
                .setNegativeButton("关闭", null).show();
    }
    private boolean isFollowing;
    private void toggleFollow() {
        isFollowing = !isFollowing; SpUtils.setPostAuthorFollowed(this, post.authorName, isFollowing);
        updateFollowUI(); ToastUtils.show(this, isFollowing ? "已关注" : "已取消关注");
    }
    private void updateFollowUI() {
        if (tvFollowBtn == null) return;
        isFollowing = SpUtils.isPostAuthorFollowed(this, post.authorName);
        tvFollowBtn.setText(isFollowing ? "已关注" : "关注");
        tvFollowBtn.setBackgroundResource(isFollowing ? R.drawable.bg_btn_outline_white : R.drawable.bg_btn_primary);
        tvFollowBtn.setTextColor(isFollowing ? getColor(R.color.text_secondary) : getColor(R.color.white));
    }
    private void updateLikeUI() { ivLike.setImageResource(post.isLiked ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline); tvLikeCount.setText(formatCount(post.likes)); }
    private void toggleLike() {
        if (!SpUtils.isLogin(this)) { ToastUtils.show(this, "请先登录"); return; }
        post.isLiked = !post.isLiked;
        post.likes = post.isLiked ? post.likes + 1 : post.likes - 1;
        AppDatabase.getInstance(this).postDao().updateLike(post.id, post.isLiked, post.likes);
        ApiService.likePost(post.id, v -> {});
        updateLikeUI(); setResult(RESULT_OK);
    }
    private void updateCollectUI() { ivCollect.setImageResource(post.isCollected ? R.drawable.ic_star_filled : R.drawable.ic_star_outline); }
    private void toggleCollect() {
        if (!SpUtils.isLogin(this)) { ToastUtils.show(this, "请先登录"); return; }
        post.isCollected = !post.isCollected;
        AppDatabase.getInstance(this).postDao().updateCollect(post.id, post.isCollected);
        ApiService.collectPost(post.id, v -> {});
        updateCollectUI(); ToastUtils.show(this, post.isCollected ? "已收藏" : "已取消收藏"); setResult(RESULT_OK);
    }
    private void doShare() {
        post.shares++; tvShareCount.setText(String.valueOf(post.shares));
        Intent s = new Intent(Intent.ACTION_SEND); s.setType("text/plain");
        s.putExtra(Intent.EXTRA_SUBJECT, post.title);
        s.putExtra(Intent.EXTRA_TEXT, post.title + " - " + post.content + "\n\n来自「发现」App");
        startActivity(Intent.createChooser(s, "分享到"));
    }
    private void loadComments() {
        ApiService.getCommentsForPost(this, postId, cs -> runOnUiThread(() -> {
            commentAdapter = new CommentAdapter(this, cs);
            rvComments.setLayoutManager(new LinearLayoutManager(this));
            rvComments.setAdapter(commentAdapter);
        }));
    }
    private void sendComment() {
        String t = etComment.getText().toString().trim();
        if (TextUtils.isEmpty(t)) { ToastUtils.show(this, "请输入评论内容"); return; }
        if (!SpUtils.isLogin(this)) { ToastUtils.show(this, "请先登录"); return; }
        String now = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(new Date());
        Comment c = new Comment(0, postId, SpUtils.getNickname(this), SpUtils.getAvatar(this), t, 0, now);
        ApiService.sendComment(postId, c, created -> {
            if (created != null) {
                AppExecutors.get().diskIO().execute(() ->
                    AppDatabase.getInstance(PostDetailActivity.this).commentDao().insertComment(created));
            } else {
                AppExecutors.get().diskIO().execute(() ->
                    AppDatabase.getInstance(PostDetailActivity.this).commentDao().insertComment(c));
            }
            Comment finalC = created != null ? created : c;
            post.comments++;
            runOnUiThread(() -> {
                tvCommentCount.setText(formatCount(post.comments));
                commentAdapter.addComment(finalC);
                rvComments.scrollToPosition(commentAdapter.getItemCount() - 1);
                etComment.setText("");
                ToastUtils.show(PostDetailActivity.this, "评论成功");
            });
        });
    }

    @Override protected void onPause() {
        super.onPause();
        if (vvPlayer != null && isPlaying) { vvPlayer.pause(); isPlaying = false; ivPlayPause.setImageResource(R.drawable.ic_play); }
        stopProgressUpdater();
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        stopProgressUpdater();
        handler.removeCallbacks(hideControlsRunnable);
        if (vvPlayer != null) vvPlayer.stopPlayback();
    }

    public static String formatCount(int count) {
        if (count >= 10000) return String.format("%.1fw", count / 10000.0);
        if (count >= 1000) return String.format("%.1fk", count / 1000.0);
        return String.valueOf(count);
    }
}
