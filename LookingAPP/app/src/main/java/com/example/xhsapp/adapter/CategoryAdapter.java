package com.example.xhsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xhsapp.R;
import com.example.xhsapp.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatVH> {

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    private Context context;
    private List<Category> categories;
    private OnCategoryClickListener listener;
    private int selectedPos = 0;

    public CategoryAdapter(Context context, List<Category> categories,
                           OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    public void updateData(List<Category> newCategories) {
        this.categories = newCategories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_category_left, parent, false);
        return new CatVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CatVH holder, int position) {
        Category cat = categories.get(position);
        holder.tvName.setText(cat.name);
        boolean selected = position == selectedPos;
        holder.tvName.setTextColor(ContextCompat.getColor(context,
                selected ? R.color.primary_purple : R.color.text_secondary));
        holder.tvName.setBackgroundResource(selected ?
                R.drawable.bg_category_selected : android.R.color.transparent);
        holder.itemView.setOnClickListener(v -> {
            int old = selectedPos;
            selectedPos = holder.getAdapterPosition();
            notifyItemChanged(old);
            notifyItemChanged(selectedPos);
            if (listener != null) listener.onCategoryClick(cat);
        });
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    static class CatVH extends RecyclerView.ViewHolder {
        TextView tvName;
        CatVH(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_category_name);
        }
    }
}
