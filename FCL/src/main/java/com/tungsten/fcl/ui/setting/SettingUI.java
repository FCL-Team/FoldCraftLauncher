package com.tungsten.fcl.ui.setting;

import static com.tungsten.fclcore.util.Lang.tryCast;

import android.content.Context;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.manage.VersionSettingPage;
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

    public SettingUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FCLTabLayout tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.container);

        tabLayout.addOnTabSelectedListener(this);
        container.post(this::initPages);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (pageManager != null) {
            ((VersionSettingPage) pageManager.getPageById(SettingPageManager.PAGE_ID_SETTING_GAME)).loadVersion(Profiles.getSelectedProfile(), null);
        }
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
        pageManager = new SettingPageManager(getContext(), container, SettingPageManager.PAGE_ID_SETTING_GAME, null);
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
