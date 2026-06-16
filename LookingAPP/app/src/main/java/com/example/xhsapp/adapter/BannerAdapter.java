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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.xhsapp.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerVH> {

    private Context context;
    private List<String> imageUrls;

    // Banner 文案（仅 4 条，对应原始图片，循环时要映射索引）
    private static final String[] TITLES = {"探索世界", "美食之旅", "生活美学", "时尚穿搭"};
    private static final String[] SUBTITLES = {"发现更多美好", "品味人间烟火", "享受生活每一刻", "穿出你的风格"};
    private static final String[] DESCS = {"个性化推荐你喜欢的内容", "来自美食家的精选推荐", "让你的生活更有品质", "潮流穿搭指南"};

    /** 原始图片数量（不含循环复制的前后两张） */
    private int realCount;

    public BannerAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.realCount = Math.max(imageUrls.size() - 2, 0); // 去掉首尾的复制
    }

    @NonNull
    @Override
    public BannerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        return new BannerVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerVH holder, int position) {
        Glide.with(context)
                .load(imageUrls.get(position))
                .placeholder(R.drawable.bg_banner_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(holder.ivBanner);

        // 循环模式下计算真实文案索引：position 1~N 对应原始 0~N-1
        // position 0（最后一张复制）→ 原始最后一张（N-1）
        // position N+1（第一张复制）→ 原始第一张（0）
        int realIdx;
        if (position == 0) {
            realIdx = realCount - 1; // 末尾复制 → 原始最后一张
        } else if (position == imageUrls.size() - 1) {
            realIdx = 0; // 开头复制 → 原始第一张
        } else {
            realIdx = position - 1; // 真实图片
        }

        if (realIdx >= 0 && realIdx < TITLES.length) {
            holder.tvTitle.setText(TITLES[realIdx]);
            holder.tvSubtitle.setText(SUBTITLES[realIdx]);
            holder.tvDesc.setText(DESCS[realIdx]);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    static class BannerVH extends RecyclerView.ViewHolder {
        ImageView ivBanner;
        TextView tvTitle, tvSubtitle, tvDesc;

        BannerVH(View view) {
            super(view);
            ivBanner = view.findViewById(R.id.iv_banner);
            tvTitle = view.findViewById(R.id.tv_banner_title);
            tvSubtitle = view.findViewById(R.id.tv_banner_subtitle);
            tvDesc = view.findViewById(R.id.tv_banner_desc);
        }
    }
}
