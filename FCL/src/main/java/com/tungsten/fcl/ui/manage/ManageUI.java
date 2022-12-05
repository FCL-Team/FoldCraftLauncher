package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.Lang.tryCast;

import android.content.Context;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLBasePage;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManageUI extends FCLMultiPageUI implements TabLayout.OnTabSelectedListener {

    private ManagePageManager pageManager;

    private FCLTabLayout tabLayout;
    private FCLUILayout container;

    public ManageUI(Context context, FCLUILayout parent, int id) {
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
        pageManager = new ManagePageManager(getContext(), container, ManagePageManager.PAGE_ID_MANAGE_SETTING, null);
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
                    pageManager.switchPage(ManagePageManager.PAGE_ID_MANAGE_INSTALL);
                    break;
                case 2:
                    pageManager.switchPage(ManagePageManager.PAGE_ID_MANAGE_MOD);
                    break;
                case 3:
                    pageManager.switchPage(ManagePageManager.PAGE_ID_MANAGE_WORLD);
                    break;
                default:
                    pageManager.switchPage(ManagePageManager.PAGE_ID_MANAGE_SETTING);
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
