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
import com.example.xhsapp.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MsgVH> {

    public interface OnItemClickListener {
        void onItemClick(Message message);
    }

    private Context context;
    private List<Message> messages;
    private OnItemClickListener listener;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public MsgVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MsgVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgVH holder, int position) {
        Message msg = messages.get(position);
        holder.tvTitle.setText(msg.title);
        holder.tvContent.setText(msg.content);
        holder.tvTime.setText(msg.time);

        // 根据消息类型显示不同图标
        if (msg.type == 4 && msg.avatar != null && !msg.avatar.isEmpty()) {
            Glide.with(context).load(msg.avatar)
                    .placeholder(R.drawable.ic_default_avatar)
                    .circleCrop().into(holder.ivIcon);
        } else {
            int[] icons = {R.drawable.ic_msg_system, R.drawable.ic_msg_like,
                    R.drawable.ic_msg_comment, R.drawable.ic_msg_fans, R.drawable.ic_default_avatar};
            holder.ivIcon.setImageResource(icons[Math.min(msg.type, icons.length - 1)]);
        }

        // 已读/未读状态
        holder.tvUnread.setVisibility(msg.isRead ? View.GONE : View.VISIBLE);
        // 未读时标题加粗
        holder.tvTitle.getPaint().setFakeBoldText(!msg.isRead);

        holder.itemView.setOnClickListener(v -> {
            if (!msg.isRead) {
                msg.isRead = true;
                holder.tvUnread.setVisibility(View.GONE);
                holder.tvTitle.getPaint().setFakeBoldText(false);
            }
            if (listener != null) listener.onItemClick(msg);
        });
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    static class MsgVH extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle, tvContent, tvTime, tvUnread;
        MsgVH(View v) {
            super(v);
            ivIcon = v.findViewById(R.id.iv_icon);
            tvTitle = v.findViewById(R.id.tv_title);
            tvContent = v.findViewById(R.id.tv_content);
            tvTime = v.findViewById(R.id.tv_time);
            tvUnread = v.findViewById(R.id.tv_unread);
        }
    }
}
