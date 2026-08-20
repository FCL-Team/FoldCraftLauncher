package com.tungsten.fcl.ui.controller;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class ControllerUI extends FCLMultiPageUI {

    public static final int PAGE_ID_CONTROLLER_MANAGER = 15040;
    public static final int PAGE_ID_CONTROLLER_REPO = 15041;

    public ControllerUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FCLUILayout container = findViewById(R.id.container);
        setupPages(container, null);
    }

    @Override
    public int getPageCount() {
        return 2;
    }

    @Override
    public FCLPage createPage(int position) {
        if (position == 1) {
            return new ControllerRepoPage(getContext(), PAGE_ID_CONTROLLER_REPO, R.layout.page_controller_repo);
        }
        return new ControllerManagePage(getContext(), PAGE_ID_CONTROLLER_MANAGER, R.layout.page_controller_manager);
    }

    @Override
    public void onBackPressed() {
        if (canReturn()) {
            dismissCurrentTempPage();
        } else if (getCurrentPagePosition() == 1) {
            // 仓库页返回管理页
            showPage(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }
}
