package com.tungsten.fcl.ui.download;

import static com.tungsten.fclcore.util.Lang.tryCast;

import android.content.Context;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLBasePage;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class DownloadUI extends FCLMultiPageUI implements TabLayout.OnTabSelectedListener {

    private DownloadPageManager pageManager;

    private FCLTabLayout tabLayout;
    private FCLUILayout container;

    public DownloadUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.container);

        tabLayout.addOnTabSelectedListener(this);
        container.post(this::initPages);
    }

    @Override
    public void onStart() {
        super.onStart();
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
    public void onPause() {
        super.onPause();
        if (pageManager != null) {
            pageManager.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pageManager != null) {
            pageManager.onResume();
        }
    }

    @Override
    public void initPages() {
        pageManager = new DownloadPageManager(getContext(), container, DownloadPageManager.PAGE_ID_DOWNLOAD_GAME, null);

        Profiles.registerVersionsListener(this::loadVersions);
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
            profile.selectedVersionProperty().addListener(observable -> pageManager.loadVersion(profile, null));
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
}
