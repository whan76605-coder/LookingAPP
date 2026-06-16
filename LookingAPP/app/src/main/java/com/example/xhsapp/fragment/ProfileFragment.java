package com.example.xhsapp.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.xhsapp.R;
import com.example.xhsapp.activity.EditProfileActivity;
import com.example.xhsapp.activity.LoginActivity;
import com.example.xhsapp.activity.PostDetailActivity;
import com.example.xhsapp.activity.SettingsActivity;
import com.example.xhsapp.adapter.PostAdapter;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.model.Post;
import com.example.xhsapp.network.ApiService;
import com.example.xhsapp.utils.AppExecutors;
import com.example.xhsapp.utils.DataProvider;
import com.example.xhsapp.utils.SpUtils;
import com.example.xhsapp.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProfileFragment extends Fragment {

    private ImageView ivAvatar, ivSettings;
    private TextView tvNickname, tvBio, tvFollowCount, tvFansCount, tvLikeCount;
    private TextView tvEditProfile, tvMyMain;
    private TabLayout tabLayout;
    private RecyclerView rvContent;
    private PostAdapter postAdapter;
    private TextView tvDraftBadge;

    private View itemCollect, itemHistory, itemPublish, itemDraft, itemLike, itemNight, itemSettings, itemHelp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
        loadMyPosts();
    }

    private void initViews(View view) {
        ivAvatar = view.findViewById(R.id.iv_avatar);
        ivSettings = view.findViewById(R.id.iv_settings);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvBio = view.findViewById(R.id.tv_bio);
        tvFollowCount = view.findViewById(R.id.tv_follow_count);
        tvFansCount = view.findViewById(R.id.tv_fans_count);
        tvLikeCount = view.findViewById(R.id.tv_like_count);
        tvEditProfile = view.findViewById(R.id.tv_edit_profile);
        tvMyMain = view.findViewById(R.id.tv_my_main);
        tabLayout = view.findViewById(R.id.tab_layout);
        rvContent = view.findViewById(R.id.rv_content);

        itemCollect = view.findViewById(R.id.item_collect);
        itemHistory = view.findViewById(R.id.item_history);
        itemPublish = view.findViewById(R.id.item_publish);
        itemDraft = view.findViewById(R.id.item_draft);
        itemLike = view.findViewById(R.id.item_like);
        itemNight = view.findViewById(R.id.item_night);
        itemSettings = view.findViewById(R.id.item_settings);
        itemHelp = view.findViewById(R.id.item_help);

        ivSettings.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), SettingsActivity.class)));
        tvEditProfile.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), EditProfileActivity.class)));
        tvMyMain.setOnClickListener(v -> showMyMainDialog());

        tvFollowCount.setOnClickListener(v -> showFollowDialog("关注", true));
        tvFansCount.setOnClickListener(v -> showFollowDialog("粉丝", false));
        tvLikeCount.setOnClickListener(v -> showLikesDialog());

        itemCollect.setOnClickListener(v -> {
            tabLayout.selectTab(tabLayout.getTabAt(1));
            loadCollectedPosts();
        });
        itemHistory.setOnClickListener(v -> showBrowseHistory());
        itemPublish.setOnClickListener(v -> {
            tabLayout.selectTab(tabLayout.getTabAt(0));
            loadMyPosts();
        });
        itemDraft.setOnClickListener(v -> showDrafts());

        // 草稿箱角标
        tvDraftBadge = view.findViewById(R.id.tv_draft_badge);

        itemLike.setOnClickListener(v -> {
            tabLayout.selectTab(tabLayout.getTabAt(3));
            loadLikedPosts();
        });
        itemNight.setOnClickListener(v -> {
            if (getActivity() != null) {
                boolean dark = !SpUtils.isDarkMode(getContext());
                SpUtils.setDarkMode(getContext(), dark);
                AppCompatDelegate.setDefaultNightMode(
                        dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                getActivity().recreate();
            }
        });
        itemSettings.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), SettingsActivity.class)));
        itemHelp.setOnClickListener(v -> showHelpDialog());

        tabLayout.addTab(tabLayout.newTab().setText("发布"));
        tabLayout.addTab(tabLayout.newTab().setText("收藏"));
        tabLayout.addTab(tabLayout.newTab().setText("视频"));
        tabLayout.addTab(tabLayout.newTab().setText("赞过"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1: loadCollectedPosts(); break;
                    case 2: loadVideoPosts(); break;
                    case 3: loadLikedPosts(); break;
                    default: loadMyPosts(); break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        rvContent.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
    }

    private void loadProfile() {
        if (getContext() == null) return;
        boolean login = SpUtils.isLogin(getContext());
        if (login) {
            tvNickname.setText(SpUtils.getNickname(getContext()));
            tvBio.setText(SpUtils.getBio(getContext()));
            String avatar = SpUtils.getAvatar(getContext());
            if (!avatar.isEmpty()) {
                Glide.with(this).load(avatar).circleCrop()
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(ivAvatar);
            }
            ivAvatar.setOnClickListener(null);
        } else {
            tvNickname.setText("未登录");
            tvBio.setText("点击登录，发现更多精彩");
            ivAvatar.setOnClickListener(v ->
                    startActivity(new Intent(getActivity(), LoginActivity.class)));
        }

        int followCount = SpUtils.getFollowCount(getContext());
        tvFollowCount.setText(String.valueOf(followCount));
        tvFansCount.setText(String.valueOf(followCount));
        tvLikeCount.setText(String.valueOf(getTotalLikes()));
    }

    private int getTotalLikes() {
        if (getContext() == null) return 0;
        String myName = SpUtils.getNickname(getContext());
        List<Post> all = AppDatabase.getInstance(getContext()).postDao().getAllPosts();
        int total = 0;
        for (Post p : all) {
            if (p.authorName != null && p.authorName.equals(myName)) total += p.likes;
        }
        return total;
    }

    private void loadMyPosts() {
        if (getContext() == null) return;
        String myName = SpUtils.getNickname(getContext());
        ApiService.getAllPosts(getContext(), allPosts -> {
            List<Post> myPosts = new ArrayList<>();
            for (Post p : allPosts) {
                // 排除草稿
                if (p.authorName != null && p.authorName.equals(myName) && !p.isDraft) {
                    myPosts.add(p);
                }
            }
            if (myPosts.isEmpty()) {
                myPosts = new ArrayList<>(allPosts.subList(0,
                        Math.min(4, allPosts.size())));
            }
            List<Post> finalMyPosts = myPosts;
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> {
                    updateAdapter(finalMyPosts);
                    updateDraftBadge();
                });
        });
    }

    private void loadCollectedPosts() {
        if (getContext() == null) return;
        AppExecutors.get().diskIO().execute(() -> {
            List<Post> collected = AppDatabase.getInstance(getContext()).postDao().getCollectedPosts();
            if (collected.isEmpty()) {
                ApiService.getAllPosts(getContext(), all -> {
                    List<Post> c = all.subList(0, Math.min(4, all.size()));
                    if (getActivity() != null)
                        getActivity().runOnUiThread(() -> updateAdapter(new ArrayList<>(c)));
                });
                return;
            }
            List<Post> finalCollected = new ArrayList<>(collected);
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> updateAdapter(finalCollected));
        });
    }

    private void loadVideoPosts() {
        if (getContext() == null) return;
        ApiService.getAllPosts(getContext(), all -> {
            List<Post> videos = new ArrayList<>();
            for (Post p : all) {
                if (p.postType == 2) videos.add(p);
            }
            if (videos.isEmpty()) {
                videos = all.subList(0, Math.min(4, all.size()));
            }
            List<Post> finalVideos = new ArrayList<>(videos);
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> updateAdapter(finalVideos));
        });
    }

    private void loadLikedPosts() {
        if (getContext() == null) return;
        AppExecutors.get().diskIO().execute(() -> {
            List<Post> all = AppDatabase.getInstance(getContext()).postDao().getAllPosts();
            List<Post> liked = new ArrayList<>();
            for (Post p : all) if (p.isLiked) liked.add(p);
            if (liked.isEmpty()) {
                ApiService.getAllPosts(getContext(), allFromApi -> {
                    List<Post> c = allFromApi.subList(0, Math.min(3, allFromApi.size()));
                    if (getActivity() != null)
                        getActivity().runOnUiThread(() -> updateAdapter(new ArrayList<>(c)));
                });
                return;
            }
            List<Post> finalLiked = new ArrayList<>(liked);
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> updateAdapter(finalLiked));
        });
    }

    private void updateAdapter(List<Post> posts) {
        if (postAdapter == null) {
            postAdapter = new PostAdapter(getContext(), posts);
            postAdapter.setOnItemClickListener(post -> {
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_POST_ID, post.id);
                startActivityForResult(intent, 1000);
            });
            rvContent.setAdapter(postAdapter);
        } else {
            postAdapter.updateData(posts);
        }
        tvLikeCount.setText(String.valueOf(getTotalLikes()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            loadProfile();
            loadMyPosts();
        }
    }

    private void updateDraftBadge() {
        if (tvDraftBadge == null) return;
        AppExecutors.get().diskIO().execute(() -> {
            List<Post> drafts = AppDatabase.getInstance(getContext()).postDao().getDrafts();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (drafts.isEmpty()) {
                        tvDraftBadge.setVisibility(View.GONE);
                    } else {
                        tvDraftBadge.setVisibility(View.VISIBLE);
                        tvDraftBadge.setText(String.valueOf(drafts.size()));
                    }
                });
            }
        });
    }

    private void showBrowseHistory() {
        if (getContext() == null) return;
        String history = SpUtils.getBrowseHistory(getContext());
        if (history.isEmpty()) {
            ToastUtils.show(getContext(), "暂无浏览历史");
            return;
        }
        AppExecutors.get().diskIO().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            List<Post> historyPosts = new ArrayList<>();
            String[] ids = history.split(",");
            for (String idStr : ids) {
                try {
                    int pid = Integer.parseInt(idStr.trim());
                    Post p = db.postDao().getPostById(pid);
                    if (p != null) historyPosts.add(p);
                } catch (NumberFormatException ignored) {}
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (historyPosts.isEmpty()) {
                        ToastUtils.show(getContext(), "暂无浏览历史");
                        return;
                    }
                    new AlertDialog.Builder(requireContext())
                            .setTitle("浏览历史 (" + historyPosts.size() + ")")
                            .setMessage(formatPostListForDialog(historyPosts))
                            .setPositiveButton("关闭", null)
                            .setNeutralButton("清空", (d, w) -> {
                                SpUtils.clearBrowseHistory(getContext());
                                ToastUtils.show(getContext(), "已清空浏览历史");
                            })
                            .show();
                });
            }
        });
    }

    private void showDrafts() {
        if (getContext() == null) return;
        AppExecutors.get().diskIO().execute(() -> {
            List<Post> drafts = AppDatabase.getInstance(getContext()).postDao().getDrafts();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (drafts.isEmpty()) {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("草稿箱")
                                .setMessage("还没有保存的草稿\n\n写一半的笔记退出时选择「保存草稿」就会出现在这里")
                                .setPositiveButton("知道了", null)
                                .show();
                        return;
                    }

                    String[] items = new String[drafts.size()];
                    for (int i = 0; i < drafts.size(); i++) {
                        Post d = drafts.get(i);
                        String dt = d.title != null && !d.title.isEmpty() ? d.title : "无标题";
                        String display = (dt.length() > 20 ? dt.substring(0, 20) + "..." : dt);
                        items[i] = display + "    (" + (d.draftTime != null ? d.draftTime : "未保存") + ")";
                    }

                    new AlertDialog.Builder(requireContext())
                            .setTitle("草稿箱 (" + drafts.size() + ")")
                            .setItems(items, (dialog, which) -> {
                                Post draft = drafts.get(which);
                                Intent intent = new Intent(getActivity(), com.example.xhsapp.activity.PublishActivity.class);
                                intent.putExtra("draft_id", draft.id);
                                startActivity(intent);
                            })
                            .setPositiveButton("关闭", null)
                            .setNeutralButton("清空草稿", (d, w) -> {
                                AppExecutors.get().diskIO().execute(() -> {
                                    for (Post p : drafts) {
                                        AppDatabase.getInstance(getContext()).postDao().deletePost(p);
                                    }
                                });
                                ToastUtils.show(getContext(), "已清空草稿");
                                updateDraftBadge();
                            })
                            .show();
                });
            }
        });
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("帮助与反馈")
                .setMessage("常见问题\n\n" +
                        "1. 如何发布笔记？\n" +
                        "   点击首页底部中间的「+」按钮即可发布动态。\n\n" +
                        "2. 如何修改个人资料？\n" +
                        "   在「我的」页面点击「编辑资料」即可修改昵称和简介。\n\n" +
                        "3. 如何收藏内容？\n" +
                        "   在帖子详情页点击星标图标即可收藏。\n\n" +
                        "4. 如何开启夜间模式？\n" +
                        "   在「我的」页面点击「夜间模式」或到设置中开启。\n\n" +
                        "5. 搜索功能怎么用？\n" +
                        "   在首页搜索栏或分类页搜索框输入关键词即可搜索。\n\n" +
                        "如有其他问题，请联系客服。")
                .setPositiveButton("我知道了", null)
                .show();
    }

    private void showMyMainDialog() {
        if (getContext() == null) return;
        String myName = SpUtils.getNickname(getContext());
        String myBio = SpUtils.getBio(getContext());
        int follows = SpUtils.getFollowCount(getContext());
        new AlertDialog.Builder(requireContext())
                .setTitle("我的主页")
                .setMessage(
                        "昵称: " + myName + "\n" +
                        "简介: " + myBio + "\n" +
                        "关注: " + follows + "人\n" +
                        "粉丝: " + follows + "人\n" +
                        "获赞: " + getTotalLikes() + "\n\n" +
                        "主页功能即将上线，敬请期待!")
                .setPositiveButton("确定", null)
                .show();
    }

    private void showFollowDialog(String title, boolean isFollowing) {
        String[] names;
        String[] avatars;
        if (isFollowing) {
            Set<String> followed = SpUtils.getFollowedAuthors(getContext());
            if (followed.isEmpty()) {
                names = new String[]{"小熊旅行日记", "一只小月亮", "料理小当家", "旅行家小明"};
                avatars = new String[]{"https://i.pravatar.cc/100?img=1", "https://i.pravatar.cc/100?img=5",
                        "https://i.pravatar.cc/100?img=9", "https://i.pravatar.cc/100?img=12"};
            } else {
                names = followed.toArray(new String[0]);
                avatars = new String[names.length];
                for (int i = 0; i < names.length; i++) {
                    avatars[i] = "https://i.pravatar.cc/100?img=" + ((i % 6) + 1);
                }
            }
        } else {
            names = new String[]{"书虫阿宝", "健身达人Leo", "时尚博主Mia", "科技达人Kevin"};
            avatars = new String[]{"https://i.pravatar.cc/100?img=20", "https://i.pravatar.cc/100?img=32",
                    "https://i.pravatar.cc/100?img=3", "https://i.pravatar.cc/100?img=7"};
        }

        if (getContext() == null) return;
        showCustomFollowDialog(title, names, avatars);
    }

    private void showCustomFollowDialog(String title, String[] names, String[] avatars) {
        if (getContext() == null) return;

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        for (int i = 0; i < names.length; i++) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 12, 0, 12);

            ImageView avatar = new ImageView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(56, 56);
            lp.setMargins(0, 0, 16, 0);
            avatar.setLayoutParams(lp);

            Glide.with(this).load(avatars[i]).circleCrop()
                    .placeholder(R.drawable.ic_default_avatar)
                    .into(avatar);

            TextView name = new TextView(getContext());
            name.setText(names[i]);
            name.setTextSize(16);
            name.setTextColor(0xFF1A1A2E);
            name.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            name.setGravity(android.view.Gravity.CENTER_VERTICAL);

            row.addView(avatar);
            row.addView(name);
            layout.addView(row);
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(title + "（" + names.length + "人）")
                .setView(layout)
                .setPositiveButton("关闭", null)
                .show();
    }

    private void showLikesDialog() {
        if (getContext() == null) return;
        String myName = SpUtils.getNickname(getContext());
        AppExecutors.get().diskIO().execute(() -> {
            List<Post> all = AppDatabase.getInstance(getContext()).postDao().getAllPosts();
            List<Post> myPosts = new ArrayList<>();
            for (Post p : all) {
                if (p.authorName != null && p.authorName.equals(myName) && !p.isDraft) {
                    myPosts.add(p);
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(myPosts.size(), 10); i++) {
                Post p = myPosts.get(i);
                sb.append(p.title).append("\n")
                  .append("👍 ").append(p.likes)
                  .append("  💬 ").append(p.comments)
                  .append("  🔁 ").append(p.shares).append("\n\n");
            }
            String content = sb.length() == 0 ? "暂无作品" : sb.toString();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() ->
                    new AlertDialog.Builder(requireContext())
                            .setTitle("我的作品数据 (" + myPosts.size() + "篇)")
                            .setMessage(content)
                            .setPositiveButton("关闭", null)
                            .show()
                );
            }
        });
    }

    private String formatPostListForDialog(List<Post> posts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(posts.size(), 10); i++) {
            Post p = posts.get(i);
            sb.append(p.title).append("\n");
        }
        if (posts.size() > 10) sb.append("... 共").append(posts.size()).append("条");
        return sb.toString();
    }
}
