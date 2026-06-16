package com.example.xhsapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.xhsapp.R;
import com.example.xhsapp.database.AppDatabase;
import com.example.xhsapp.fragment.CategoryFragment;
import com.example.xhsapp.fragment.HomeFragment;
import com.example.xhsapp.fragment.MessageFragment;
import com.example.xhsapp.fragment.ProfileFragment;
import com.example.xhsapp.utils.AppExecutors;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.badge.BadgeDrawable;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PUBLISH = 1001;

    private BottomNavigationView bottomNav;
    private FloatingActionButton fabPublish;

    private HomeFragment homeFragment;
    private CategoryFragment categoryFragment;
    private MessageFragment messageFragment;
    private ProfileFragment profileFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);
        fabPublish = findViewById(R.id.fab_publish);

        initFragments();
        initBottomNav();

        fabPublish.setOnClickListener(v -> {
            Intent intent = new Intent(this, PublishActivity.class);
            startActivityForResult(intent, REQUEST_PUBLISH);
        });

        checkUnreadMessages();
    }

    private void initFragments() {
        homeFragment = new HomeFragment();
        categoryFragment = new CategoryFragment();
        messageFragment = new MessageFragment();
        profileFragment = new ProfileFragment();

        activeFragment = homeFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, profileFragment, "profile").hide(profileFragment)
                .add(R.id.fragment_container, messageFragment, "message").hide(messageFragment)
                .add(R.id.fragment_container, categoryFragment, "category").hide(categoryFragment)
                .add(R.id.fragment_container, homeFragment, "home")
                .commit();
    }

    /** 分类按钮点击后切换到分类Tab */
    public void switchToCategory() {
        bottomNav.setSelectedItemId(R.id.nav_category);
        switchFragment(categoryFragment);
    }

    private void initBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                switchFragment(homeFragment);
            } else if (id == R.id.nav_category) {
                switchFragment(categoryFragment);
            } else if (id == R.id.nav_message) {
                updateMessageBadge();
                switchFragment(messageFragment);
            } else if (id == R.id.nav_profile) {
                switchFragment(profileFragment);
            }
            return true;
        });
    }

    private void switchFragment(Fragment target) {
        if (activeFragment == target) return;
        getSupportFragmentManager().beginTransaction()
                .hide(activeFragment)
                .show(target)
                .commit();
        activeFragment = target;
    }

    /** 检查未读消息数量，更新底部导航红点 */
    private void checkUnreadMessages() {
        AppExecutors.get().diskIO().execute(() -> {
            int count = AppDatabase.getInstance(this).messageDao().getUnreadCount();
            runOnUiThread(() -> {
                BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_message);
                if (count > 0) {
                    badge.setVisible(true);
                    badge.setNumber(count);
                } else {
                    badge.setVisible(false);
                }
            });
        });
    }

    /** 更新消息红点（根据数据库实际未读数） */
    public void updateMessageBadge() {
        AppExecutors.get().diskIO().execute(() -> {
            int count = AppDatabase.getInstance(this).messageDao().getUnreadCount();
            runOnUiThread(() -> {
                BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_message);
                if (count > 0) {
                    badge.setVisible(true);
                    badge.setNumber(count);
                } else {
                    badge.setVisible(false);
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PUBLISH && resultCode == RESULT_OK) {
            bottomNav.setSelectedItemId(R.id.nav_home);
            homeFragment.refreshPosts();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUnreadMessages();
    }
}
