package com.example.xhsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xhsapp.R;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentVH> {

    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        notifyItemInserted(comments.size() - 1);
    }

    @NonNull
    @Override
    public CommentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentVH holder, int position) {
        Comment c = comments.get(position);
        Glide.with(context).load(c.authorAvatar)
                .placeholder(R.drawable.ic_default_avatar)
                .circleCrop().into(holder.ivAvatar);
        holder.tvName.setText(c.authorName);
        holder.tvContent.setText(c.content);
        holder.tvTime.setText(c.createTime);
        holder.tvLikes.setText(String.valueOf(c.likes));

        holder.ivLike.setOnClickListener(v -> {
            c.likes++;
            holder.tvLikes.setText(String.valueOf(c.likes));
            // 持久化到数据库
            if (c.id > 0) {
                new Thread(() ->
                    AppDatabase.getInstance(context).commentDao().updateCommentLikes(c.id, c.likes)
                ).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    static class CommentVH extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivLike;
        TextView tvName, tvContent, tvTime, tvLikes;

        CommentVH(View view) {
            super(view);
            ivAvatar = view.findViewById(R.id.iv_avatar);
            ivLike = view.findViewById(R.id.iv_like);
            tvName = view.findViewById(R.id.tv_name);
            tvContent = view.findViewById(R.id.tv_content);
            tvTime = view.findViewById(R.id.tv_time);
            tvLikes = view.findViewById(R.id.tv_likes);
        }
    }
}
