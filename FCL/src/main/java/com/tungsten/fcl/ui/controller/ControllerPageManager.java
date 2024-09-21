package com.tungsten.fcl.ui.controller;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIListener;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public class ControllerPageManager extends PageManager {

    public static final int PAGE_ID_CONTROLLER_MANAGER = 15040;
    public static final int PAGE_ID_CONTROLLER_REPO = 15041;

    private static ControllerPageManager instance;

    private ControllerManagePage controllerManagePage;
    private ControllerRepoPage controllerRepoPage;

    public static ControllerPageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ControllerPageManager not initialized!");
        }
        return instance;
    }

    public ControllerPageManager(Context context, FCLUILayout parent, int defaultPageId, UIListener listener) {
        super(context, parent, defaultPageId, listener);
        instance = this;
    }

    @Override
    public void init(UIListener listener) {
        controllerManagePage = new ControllerManagePage(getContext(), PAGE_ID_CONTROLLER_MANAGER, getParent(), R.layout.page_controller_manager);
        controllerRepoPage = new ControllerRepoPage(getContext(), PAGE_ID_CONTROLLER_REPO, getParent(), R.layout.page_controller_repo);

        if (listener != null) {
            listener.onLoad();
        }
    }

    @Override
    public ArrayList<FCLCommonPage> getAllPages() {
        ArrayList<FCLCommonPage> pages = new ArrayList<>();
        pages.add(controllerManagePage);
        pages.add(controllerRepoPage);
        return pages;
    }
}
