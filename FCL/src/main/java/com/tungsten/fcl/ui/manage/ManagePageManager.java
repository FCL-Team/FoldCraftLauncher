package com.tungsten.fcl.ui.manage;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIListener;
import com.tungsten.fcl.ui.download.DownloadPage;
import com.tungsten.fcl.ui.download.InstallVersionPage;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public class ManagePageManager extends PageManager {

    public static final int PAGE_ID_MANAGE_SETTING = 15000;
    public static final int PAGE_ID_MANAGE_INSTALL = 15001;
    public static final int PAGE_ID_MANAGE_MOD = 15002;
    public static final int PAGE_ID_MANAGE_WORLD = 15003;

    private static ManagePageManager instance;

    private VersionSettingPage versionSettingPage;
    private InstallerListPage installerListPage;
    private ModListPage modListPage;
    private WorldListPage worldListPage;

    public static ManagePageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ManagePageManager not initialized!");
        }
        return instance;
    }

    public ManagePageManager(Context context, FCLUILayout parent, int defaultPageId, UIListener listener) {
        super(context, parent, defaultPageId, listener);
        instance = this;
    }

    @Override
    public void init(UIListener listener) {
        versionSettingPage = new VersionSettingPage(getContext(), PAGE_ID_MANAGE_SETTING, getParent(), R.layout.page_version_setting);
        installerListPage = new InstallerListPage(getContext(), PAGE_ID_MANAGE_INSTALL, getParent(), R.layout.page_installer_list);
        modListPage = new ModListPage(getContext(), PAGE_ID_MANAGE_MOD, getParent(), R.layout.page_mod_list);
        worldListPage = new WorldListPage(getContext(), PAGE_ID_MANAGE_WORLD, getParent(), R.layout.page_world_list);

        if (listener != null) {
            listener.onLoad();
        }
    }

    @Override
    public ArrayList<FCLCommonPage> getAllPages() {
        ArrayList<FCLCommonPage> pages = new ArrayList<>();
        pages.add(versionSettingPage);
        pages.add(installerListPage);
        pages.add(modListPage);
        pages.add(worldListPage);
        return pages;
    }
}
