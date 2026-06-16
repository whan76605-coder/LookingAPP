package com.example.xhsapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xhsapp.R;
import com.example.xhsapp.activity.PostDetailActivity;
import com.example.xhsapp.activity.SearchActivity;
import com.example.xhsapp.adapter.CategoryAdapter;
import com.example.xhsapp.adapter.PostAdapter;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Category;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.utils.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private RecyclerView rvCategories, rvPosts;
    private ImageView ivSearch;
    private CategoryAdapter categoryAdapter;
    private PostAdapter postAdapter;
    private List<Category> categories;
    private String selectedCategory = "全部";
    private List<Post> allPosts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivSearch = view.findViewById(R.id.iv_search);
        rvCategories = view.findViewById(R.id.rv_categories);
        rvPosts = view.findViewById(R.id.rv_posts);

        ivSearch.setOnClickListener(v ->
            startActivity(new Intent(getActivity(), SearchActivity.class)));

        categories = DataProvider.getCategories();
        ApiService.getCategories(cats -> {
            if (cats != null && !cats.isEmpty()) categories = cats;
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> categoryAdapter.updateData(categories));
        });

        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new CategoryAdapter(getContext(), categories, cat -> {
            selectedCategory = cat.name;
            filterPosts();
        });
        rvCategories.setAdapter(categoryAdapter);

        rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        loadAllPosts();
    }

    private void loadAllPosts() {
        if (getContext() == null) return;
        ApiService.getAllPosts(getContext(), posts -> {
            allPosts = new ArrayList<>(posts);
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> filterPosts());
        });
    }

    private void filterPosts() {
        List<Post> filtered = new ArrayList<>();
        for (Post p : allPosts) {
            if (selectedCategory.equals("全部") || p.category.equals(selectedCategory)) {
                filtered.add(p);
            }
        }
        if (postAdapter == null) {
            postAdapter = new PostAdapter(getContext(), filtered);
            postAdapter.setOnItemClickListener(post -> {
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_POST_ID, post.id);
                startActivity(intent);
            });
            rvPosts.setAdapter(postAdapter);
        } else {
            postAdapter.updateData(filtered);
        }
    }
}
