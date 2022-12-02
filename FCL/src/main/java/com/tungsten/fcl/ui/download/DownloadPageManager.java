package com.tungsten.fcl.ui.download;

import android.content.Context;

import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIListener;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public class DownloadPageManager extends PageManager {

    public static final int PAGE_ID_DOWNLOAD_GAME = 15010;
    public static final int PAGE_ID_DOWNLOAD_MODPACK = 15011;
    public static final int PAGE_ID_DOWNLOAD_MOD = 15012;
    public static final int PAGE_ID_DOWNLOAD_RESOURCE_PACK = 15013;
    public static final int PAGE_ID_DOWNLOAD_SAVE = 15014;

    private static DownloadPageManager instance;

    public static DownloadPageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DownloadPageManager not initialized!");
        }
        return instance;
    }

    public DownloadPageManager(Context context, FCLUILayout parent, int defaultPageId, UIListener listener) {
        super(context, parent, defaultPageId, listener);
    }

    @Override
    public void init(UIListener listener) {

    }

    @Override
    public ArrayList<FCLCommonPage> getAllPages() {
        return null;
    }

}
