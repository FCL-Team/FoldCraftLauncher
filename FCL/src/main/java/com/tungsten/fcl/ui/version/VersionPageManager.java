package com.tungsten.fcl.ui.version;

import android.content.Context;

import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIListener;
import com.tungsten.fcl.ui.version.compose.ComposeVersionListPage;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public class VersionPageManager extends PageManager {

    public static final int PAGE_ID_VERSION_LIST = 15020;

    private static VersionPageManager instance;

    private FCLCommonPage versionListPage;

    public static VersionPageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("VersionPageManager not initialized!");
        }
        return instance;
    }

    public VersionPageManager(Context context, FCLUILayout parent, int defaultPageId, UIListener listener) {
        super(context, parent, defaultPageId, listener);
        instance = this;
    }

    @Override
    public void init(UIListener listener) {
        // 批 2：Compose 开关已固化，旧 View 页面（VersionListPage + page_version_list.xml）已删除。
        versionListPage = new ComposeVersionListPage(getContext(), PAGE_ID_VERSION_LIST, getParent());

        if (listener != null) {
            listener.onLoad();
        }
    }

    @Override
    public ArrayList<FCLCommonPage> getAllPages() {
        ArrayList<FCLCommonPage> pages = new ArrayList<>();
        pages.add(versionListPage);
        return pages;
    }
}
