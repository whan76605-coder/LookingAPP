package com.example.xhsapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xhsapp.R;
import com.example.xhsapp.adapter.MessageAdapter;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Message;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.utils.AppExecutors;
import com.example.xhsapp.utils.ToastUtils;

import java.util.List;

public class MessageFragment extends Fragment {

    private RecyclerView rvMessages;
    private MessageAdapter adapter;
    private View emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMessages = view.findViewById(R.id.rv_messages);
        emptyView = view.findViewById(R.id.tv_empty_messages);

        if (getContext() == null) return;

        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        loadMessages();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMessages();
    }

    private void loadMessages() {
        if (getContext() == null) return;
        ApiService.getAllMessages(getContext(), messages -> {
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                if (messages.isEmpty()) {
                    rvMessages.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    rvMessages.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    adapter = new MessageAdapter(getContext(), messages);
                    adapter.setOnItemClickListener(msg -> handleMessageClick(msg));
                    rvMessages.setAdapter(adapter);
                }
            });
        });
    }

    private void handleMessageClick(Message msg) {
        if (getContext() == null) return;

        AppExecutors.get().diskIO().execute(() -> {
            AppDatabase.getInstance(getContext()).messageDao().markAsRead(msg.id);
            // 通知 MainActivity 实时更新红点
            if (getActivity() instanceof com.example.xhsapp.activity.MainActivity) {
                ((com.example.xhsapp.activity.MainActivity) getActivity()).updateMessageBadge();
            }
        });

        switch (msg.type) {
            case 0:
                new AlertDialog.Builder(requireContext())
                        .setTitle("系统通知")
                        .setMessage("【系统通知】\n\n" + msg.content + "\n\n这是来自系统的通知消息。")
                        .setPositiveButton("知道了", null)
                        .show();
                break;
            case 1:
                ToastUtils.show(getContext(), "点赞通知：" + msg.content);
                break;
            case 2:
                ToastUtils.show(getContext(), "评论通知：" + msg.content);
                break;
            case 3:
                ToastUtils.show(getContext(), "你有了新粉丝！");
                break;
            case 4:
                new AlertDialog.Builder(requireContext())
                        .setTitle("私信 - " + msg.title)
                        .setMessage(msg.content)
                        .setPositiveButton("回复", (d, w) ->
                                ToastUtils.show(getContext(), "私信回复功能开发中"))
                        .setNegativeButton("关闭", null)
                        .show();
                break;
        }
    }
}
