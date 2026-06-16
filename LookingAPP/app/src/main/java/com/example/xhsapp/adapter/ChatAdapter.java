package com.example.xhsapp.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xhsapp.R;
import com.example.xhsapp.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH> {

    private static final int TYPE_USER = 0;
    private static final int TYPE_AI = 1;

    private Context ctx;
    private List<ChatMessage> messages = new ArrayList<>();

    public ChatAdapter(Context ctx, List<ChatMessage> messages) {
        this.ctx = ctx;
        this.messages = messages;
    }

    public void addMessage(ChatMessage msg) {
        messages.add(msg);
        notifyItemInserted(messages.size() - 1);
    }

    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUser ? TYPE_USER : TYPE_AI;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_chat_message, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ChatMessage msg = messages.get(position);

        holder.tvBubble.setText(msg.content);
        holder.tvTime.setText(msg.time);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.tvBubble.getLayoutParams();
        if (msg.isUser) {
            holder.tvBubble.setBackgroundResource(R.drawable.bg_chat_user);
            holder.tvBubble.setTextColor(ContextCompat.getColor(ctx, R.color.white));
            holder.tvTime.setGravity(Gravity.END);
        } else {
            holder.tvBubble.setBackgroundResource(R.drawable.bg_chat_ai);
            holder.tvBubble.setTextColor(ContextCompat.getColor(ctx, R.color.text_primary));
            holder.tvTime.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvBubble, tvTime;
        VH(View v) {
            super(v);
            tvBubble = v.findViewById(R.id.tv_bubble);
            tvTime = v.findViewById(R.id.tv_time);
        }
    }
}
