package com.tungsten.fcl.ui.setting;

import static com.tungsten.fclcore.util.Lang.tryCast;

import android.content.Context;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.manage.VersionSettingPage;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLBasePage;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class SettingUI extends FCLMultiPageUI implements TabLayout.OnTabSelectedListener {

    private SettingPageManager pageManager;

    private FCLUILayout container;
    public FCLTabLayout tabLayout;

    private InvalidationListener profileListener;

    public SettingUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.container);

        tabLayout.addOnTabSelectedListener(this);
        initPages();

        // 切换 Profile 时刷新全局版本设置页（原 onStart 生命周期移除后的兜底）
        profileListener = observable -> {
            if (pageManager != null) {
                FCLBasePage page = pageManager.getPageById(SettingPageManager.PAGE_ID_SETTING_GAME);
                if (page instanceof VersionSettingPage) {
                    ((VersionSettingPage) page).loadVersion(Profiles.getSelectedProfile(), null);
                }
            }
        };
        Profiles.selectedProfileProperty().addListener(profileListener);
        // UI 被 ViewPager 回收时注销监听（替代原 onDestroy 生命周期），防止泄漏
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Profiles.selectedProfileProperty().removeListener(profileListener);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pageManager != null && pageManager.canReturn()) {
            pageManager.dismissCurrentTempPage();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void initPages() {
        pageManager = new SettingPageManager(getContext(), container, SettingPageManager.PAGE_ID_SETTING_GAME);
    }

    @Override
    public ArrayList<FCLBasePage> getAllPages() {
        return pageManager == null ? null : (ArrayList<FCLBasePage>) pageManager.getAllPages().stream().map(it -> tryCast(it, FCLBasePage.class)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    @Override
    public FCLBasePage getPage(int id) {
        return pageManager == null ? null : pageManager.getPageById(id);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (pageManager != null) {
            switch (tab.getPosition()) {
                case 1:
                    pageManager.switchPage(SettingPageManager.PAGE_ID_SETTING_LAUNCHER);
                    break;
                case 2:
                    pageManager.switchPage(SettingPageManager.PAGE_ID_SETTING_HELP);
                    break;
                case 3:
                    pageManager.switchPage(SettingPageManager.PAGE_ID_SETTING_ABOUT);
                    break;
                default:
                    pageManager.switchPage(SettingPageManager.PAGE_ID_SETTING_GAME);
                    ((VersionSettingPage) pageManager.getPageById(SettingPageManager.PAGE_ID_SETTING_GAME)).loadVersion(Profiles.getSelectedProfile(), null);
                    break;
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
