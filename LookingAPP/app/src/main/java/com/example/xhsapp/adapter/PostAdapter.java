package com.example.xhsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.xhsapp.R;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostVH> {

    private Context context;
    private List<Post> posts;
    private OnItemClickListener listener;
    private boolean showCover = true;
    private int lastAnimatedPosition = -1;

    private static final int[] FAKE_HEIGHTS = {
            180, 220, 200, 240, 170, 210, 230, 190, 250, 200,
            180, 220, 200, 240, 170, 210, 230, 190, 250, 200
    };

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public PostAdapter(Context context, List<Post> posts, boolean showCover) {
        this.context = context;
        this.posts = posts;
        this.showCover = showCover;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    public void updateData(List<Post> newPosts) {
        this.posts = newPosts;
        lastAnimatedPosition = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostVH holder, int position) {
        Post post = posts.get(position);

        boolean isRemoteImage = post.imageUrl != null &&
                (post.imageUrl.startsWith("http") || post.imageUrl.startsWith("https"));

        if (post.postType == 1) {
            // 纯文案
            holder.ivCover.setVisibility(View.GONE);
            holder.tvTextPlaceholder.setVisibility(View.VISIBLE);
            holder.tvVideoTag.setVisibility(View.GONE);
            String preview = post.content != null && post.content.length() > 10
                    ? post.content.substring(0, 10) + "…" : (post.content != null ? post.content : "");
            holder.tvTextPlaceholder.setText("📝\n" + preview);
        } else if (post.postType == 2) {
            // 视频
            holder.tvTextPlaceholder.setVisibility(View.GONE);
            holder.ivCover.setVisibility(View.VISIBLE);
            holder.tvVideoTag.setVisibility(View.VISIBLE);
            if (post.videoResource != null && !post.videoResource.isEmpty()) {
                loadLocalVideoCover(holder, post);
            } else {
                loadCover(holder, post);
            }
        } else {
            // 图文
            holder.tvTextPlaceholder.setVisibility(View.GONE);
            holder.tvVideoTag.setVisibility(View.GONE);
            if (showCover && isRemoteImage) {
                holder.ivCover.setVisibility(View.VISIBLE);
                loadCover(holder, post);
            } else if (showCover) {
                holder.ivCover.setVisibility(View.VISIBLE);
                loadCover(holder, post);
            } else {
                holder.ivCover.setVisibility(View.GONE);
            }
        }

        if (showCover && holder.ivCover.getVisibility() == View.VISIBLE && post.postType != 1) {
            int heightIndex = Math.abs(post.id) % FAKE_HEIGHTS.length;
            ViewGroup.LayoutParams lp = holder.ivCover.getLayoutParams();
            int targetHeight = (int) (FAKE_HEIGHTS[heightIndex] * context.getResources().getDisplayMetrics().density);
            if (lp.height != targetHeight) {
                lp.height = targetHeight;
                holder.ivCover.setLayoutParams(lp);
            }
        }

        Glide.with(context)
                .load(post.authorAvatar)
                .placeholder(R.drawable.ic_default_avatar)
                .circleCrop()
                .into(holder.ivAvatar);

        holder.tvTitle.setText(post.title);
        holder.tvAuthor.setText(post.authorName);
        holder.tvLikes.setText(formatCount(post.likes));

        String catDisplay = post.category != null ? post.category : "";
        if (post.postType == 2) catDisplay = "🎬 " + catDisplay;
        holder.tvCategory.setText(catDisplay);

        holder.ivLike.setImageResource(post.isLiked ?
                R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(post);
        });

        holder.ivLike.setOnClickListener(v -> {
            post.isLiked = !post.isLiked;
            post.likes = post.isLiked ? post.likes + 1 : post.likes - 1;
            holder.ivLike.setImageResource(post.isLiked ?
                    R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
            holder.tvLikes.setText(formatCount(post.likes));
            AppDatabase.getInstance(context).postDao().updateLike(post.id, post.isLiked, post.likes);
        });

        if (position > lastAnimatedPosition) {
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.item_slide_up);
            holder.itemView.startAnimation(anim);
            if (position == getItemCount() - 1) {
                lastAnimatedPosition = getItemCount() - 1;
            }
        }
    }

    private void loadCover(PostVH holder, Post post) {
        Glide.with(context)
                .load(post.imageUrl)
                .placeholder(R.drawable.bg_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.ivCover);
    }

    /** 从 raw 资源加载视频的缩略图 */
    private void loadLocalVideoCover(PostVH holder, Post post) {
        try {
            int resId = context.getResources().getIdentifier(
                    post.videoResource, "raw", context.getPackageName());
            if (resId != 0) {
                Glide.with(context)
                        .load(resId)
                        .placeholder(R.drawable.bg_placeholder)
                        .into(holder.ivCover);
            } else {
                // raw resource ID not found, fall back to imageUrl
                loadCover(holder, post);
            }
        } catch (Exception e) {
            loadCover(holder, post);
        }
    }

    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    public static String formatCount(int count) {
        if (count >= 10000) return String.format("%.1fw", count / 10000.0);
        if (count >= 1000) return String.format("%.1fk", count / 1000.0);
        return String.valueOf(count);
    }

    static class PostVH extends RecyclerView.ViewHolder {
        ImageView ivCover, ivAvatar, ivLike;
        TextView tvTitle, tvAuthor, tvLikes, tvCategory;
        TextView tvTextPlaceholder, tvVideoTag;

        PostVH(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivLike = itemView.findViewById(R.id.iv_like);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvLikes = itemView.findViewById(R.id.tv_likes);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvTextPlaceholder = itemView.findViewById(R.id.tv_text_placeholder);
            tvVideoTag = itemView.findViewById(R.id.tv_video_tag);
        }
    }
}
