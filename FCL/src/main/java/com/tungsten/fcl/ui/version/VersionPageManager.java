package com.tungsten.fcl.ui.version;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public class VersionPageManager extends PageManager {

    public static final int PAGE_ID_VERSION_LIST = 15020;

    private static VersionPageManager instance;

    private VersionListPage versionListPage;

    public static VersionPageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("VersionPageManager not initialized!");
        }
        return instance;
    }

    public VersionPageManager(Context context, FCLUILayout parent, int defaultPageId) {
        super(context, parent, defaultPageId);
        instance = this;
    }

    @Override
    public void init() {
        versionListPage = new VersionListPage(getContext(), PAGE_ID_VERSION_LIST, getParent(), R.layout.page_version_list);
    }

    @Override
    public ArrayList<FCLCommonPage> getAllPages() {
        ArrayList<FCLCommonPage> pages = new ArrayList<>();
        pages.add(versionListPage);
        return pages;
    }
}
