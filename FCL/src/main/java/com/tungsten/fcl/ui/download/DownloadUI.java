package com.tungsten.fcl.ui.download;

import static com.tungsten.fclcore.util.Lang.tryCast;

import android.content.Context;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLBasePage;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DownloadUI extends FCLMultiPageUI implements TabLayout.OnTabSelectedListener {

    private DownloadPageManager pageManager;

    public FCLTabLayout tabLayout;
    public FCLUILayout container;

    private final Consumer<Profile> versionsListener = this::loadVersions;
    private Profile listenerProfile;
    private InvalidationListener selectedVersionListener;

    public DownloadUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.container);

        tabLayout.addOnTabSelectedListener(this);
        initPages();

        // UI 被 ViewPager 回收时注销版本监听（替代原 onDestroy 生命周期），防止静态列表累积泄漏
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Profiles.unregisterVersionsListener(versionsListener);
                if (selectedVersionListener != null) {
                    listenerProfile.selectedVersionProperty().removeListener(selectedVersionListener);
                }
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
        pageManager = new DownloadPageManager(getContext(), container, DownloadPageManager.PAGE_ID_DOWNLOAD_GAME);

        Profiles.registerVersionsListener(versionsListener);
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

    private void loadVersions(Profile profile) {
        if (profile == Profiles.getSelectedProfile()) {
            pageManager.loadVersion(profile, null);
            // 先移除旧监听再添加，避免重复注册累积（引用旧 UI 实例导致泄漏）
            if (selectedVersionListener != null) {
                listenerProfile.selectedVersionProperty().removeListener(selectedVersionListener);
            }
            selectedVersionListener = observable -> pageManager.loadVersion(profile, null);
            listenerProfile = profile;
            profile.selectedVersionProperty().addListener(selectedVersionListener);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (pageManager != null) {
            switch (tab.getPosition()) {
                case 1:
                    pageManager.switchPage(DownloadPageManager.PAGE_ID_DOWNLOAD_MODPACK);
                    break;
                case 2:
                    pageManager.switchPage(DownloadPageManager.PAGE_ID_DOWNLOAD_MOD);
                    break;
                case 3:
                    pageManager.switchPage(DownloadPageManager.PAGE_ID_DOWNLOAD_RESOURCE_PACK);
                    break;
                case 4:
                    pageManager.switchPage(DownloadPageManager.PAGE_ID_DOWNLOAD_WORLD);
                    break;
                case 5:
                    pageManager.switchPage(DownloadPageManager.PAGE_ID_DOWNLOAD_SHADER_PACK);
                    break;
                default:
                    pageManager.switchPage(DownloadPageManager.PAGE_ID_DOWNLOAD_GAME);
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

    public DownloadPageManager getPageManager() {
        return pageManager;
    }
}
