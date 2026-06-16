package com.example.xhsapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xhsapp.R;
import com.example.xhsapp.activity.HotActivity;
import com.example.xhsapp.activity.PostDetailActivity;
import com.example.xhsapp.activity.ReadActivity;
import com.example.xhsapp.activity.SearchActivity;
import com.example.xhsapp.adapter.BannerAdapter;
import com.example.xhsapp.adapter.PostAdapter;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.network.HttpUtils;
import com.example.xhsapp.utils.AppExecutors;
import com.example.xhsapp.utils.DataProvider;
import com.example.xhsapp.utils.SpUtils;
import com.example.xhsapp.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {

    private SwipeRefreshLayout swipeRefresh;
    private ViewPager2 vpBanner;
    private TabLayout tabLayout;
    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private TextView tvSearch;
    private ImageView ivNotice, ivAi, ivRefresh;

    private List<Post> allPosts = new ArrayList<>();
    private Handler bannerHandler = new Handler(Looper.getMainLooper());
    private Runnable bannerRunnable;
    private int currentTab = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        loadBanner();
        loadPosts();
        checkNetwork();
    }

    private void initViews(View view) {
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        vpBanner = view.findViewById(R.id.vp_banner);
        tabLayout = view.findViewById(R.id.tab_layout);
        rvPosts = view.findViewById(R.id.rv_posts);
        tvSearch = view.findViewById(R.id.tv_search);
        ivNotice = view.findViewById(R.id.iv_notice);
        ivAi = view.findViewById(R.id.iv_ai);
        ivRefresh = view.findViewById(R.id.iv_refresh);

        tvSearch.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), SearchActivity.class)));
        ivNotice.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), com.example.xhsapp.activity.NoticeActivity.class)));
        ivAi.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), com.example.xhsapp.activity.ChatActivity.class)));

        ivRefresh.setOnClickListener(v -> shufflePosts());

        swipeRefresh.setColorSchemeResources(R.color.primary_purple);
        swipeRefresh.setOnRefreshListener(() ->
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                refreshPosts();
                swipeRefresh.setRefreshing(false);
            }, 1200)
        );

        tabLayout.addTab(tabLayout.newTab().setText("推荐"));
        tabLayout.addTab(tabLayout.newTab().setText("关注"));

        initFunctionEntries(view);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                if (currentTab == 0) displayPosts(allPosts);
                else loadFollowPosts();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        rvPosts.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
    }

    /** 通过 Activity 切换到分类 Tab */
    private void switchToCategoryTab() {
        if (getActivity() != null) {
            com.example.xhsapp.activity.MainActivity main =
                    (com.example.xhsapp.activity.MainActivity) getActivity();
            main.switchToCategory();
        }
    }

    private void initFunctionEntries(View view) {
        View hotEntry = view.findViewWithTag("hot");
        if (hotEntry != null) hotEntry.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), HotActivity.class)));

        View catEntry = view.findViewWithTag("category");
        if (catEntry != null) catEntry.setOnClickListener(v ->
                switchToCategoryTab());

        View discoverEntry = view.findViewWithTag("discover");
        if (discoverEntry != null) discoverEntry.setOnClickListener(v -> {
            shufflePosts();
            ToastUtils.show(getContext(), "发现更多精彩");
        });

        View videoEntry = view.findViewWithTag("video");
        if (videoEntry != null) videoEntry.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HotActivity.class);
            intent.putExtra("mode", "video");
            startActivity(intent);
        });

        View readEntry = view.findViewWithTag("read");
        if (readEntry != null) readEntry.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ReadActivity.class)));
    }

    private void loadBanner() {
        List<String> banners = DataProvider.getBannerImages();
        List<String> loopBanners = new ArrayList<>();
        loopBanners.add(banners.get(banners.size() - 1));
        loopBanners.addAll(banners);
        loopBanners.add(banners.get(0));

        BannerAdapter bannerAdapter = new BannerAdapter(getContext(), loopBanners);
        vpBanner.setAdapter(bannerAdapter);
        vpBanner.setCurrentItem(1, false);

        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int next = vpBanner.getCurrentItem() + 1;
                if (next >= loopBanners.size() - 1) {
                    vpBanner.setCurrentItem(loopBanners.size() - 1, true);
                    bannerHandler.postDelayed(() -> vpBanner.setCurrentItem(1, false), 500);
                } else {
                    vpBanner.setCurrentItem(next, true);
                }
                bannerHandler.postDelayed(this, 3500);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }

    private void loadPosts() {
        if (getContext() == null) return;
        ApiService.getAllPosts(getContext(), posts -> {
            allPosts = new ArrayList<>(posts);
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> displayPosts(allPosts));
        });
    }

    private void displayPosts(List<Post> posts) {
        setupPostAdapter(posts);
    }

    private void loadFollowPosts() {
        if (getContext() == null) return;
        Set<String> followedAuthors = SpUtils.getFollowedAuthors(getContext());
        if (followedAuthors.isEmpty()) {
            ToastUtils.show(getContext(), "你还没有关注任何人");
            List<Post> randomPosts = new ArrayList<>(allPosts);
            Collections.shuffle(randomPosts);
            displayPosts(randomPosts.subList(0, Math.min(6, randomPosts.size())));
            return;
        }

        List<Post> followPosts = new ArrayList<>();
        List<Post> source = allPosts;
        for (Post p : source) {
            if (followedAuthors.contains(p.authorName)) {
                followPosts.add(p);
            }
        }
        if (followPosts.isEmpty()) {
            ToastUtils.show(getContext(), "关注的人暂无新内容");
            followPosts = source.subList(0, Math.min(6, source.size()));
        }
        displayPosts(followPosts);
    }

    private void shufflePosts() {
        Collections.shuffle(allPosts);
        displayPosts(allPosts);
    }

    private void setupPostAdapter(List<Post> posts) {
        if (postAdapter == null) {
            postAdapter = new PostAdapter(getContext(), posts);
            postAdapter.setOnItemClickListener(post -> {
                SpUtils.addBrowseHistory(getContext(), String.valueOf(post.id));
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_POST_ID, post.id);
                startActivity(intent);
            });
            rvPosts.setAdapter(postAdapter);
        } else {
            postAdapter.updateData(posts);
        }
    }

    private void checkNetwork() {
        HttpUtils.checkNetwork(new HttpUtils.Callback2() {
            @Override public void onSuccess(String response) {}
            @Override public void onFailure(String error) {
                if (getContext() != null)
                    ToastUtils.show(getContext(), "网络连接失败，显示缓存内容");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bannerHandler.removeCallbacks(bannerRunnable);
    }

    public void refreshPosts() {
        loadPosts();
    }
}
