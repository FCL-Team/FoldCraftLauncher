package com.tungsten.fcl.ui.setting;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIListener;
import com.tungsten.fcl.ui.manage.VersionSettingPage;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public class SettingPageManager extends PageManager {

    public static final int PAGE_ID_SETTING_GAME = 15030;
    public static final int PAGE_ID_SETTING_LAUNCHER = 15031;
    public static final int PAGE_ID_SETTING_HELP = 15032;
    public static final int PAGE_ID_SETTING_COMMUNITY = 15033;
    public static final int PAGE_ID_SETTING_ABOUT = 15034;

    private static SettingPageManager instance;

    private VersionSettingPage versionSettingPage;
    private LauncherSettingPage launcherSettingPage;
    private HelpPage helpPage;
    private CommunityPage communityPage;
    private AboutPage aboutPage;

    public static SettingPageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SettingPageManager not initialized!");
        }
        return instance;
    }

    public SettingPageManager(Context context, FCLUILayout parent, int defaultPageId, UIListener listener) {
        super(context, parent, defaultPageId, listener);
        instance = this;
    }

    @Override
    public void init(UIListener listener) {
        versionSettingPage = new VersionSettingPage(getContext(), PAGE_ID_SETTING_GAME, getParent(), R.layout.page_version_setting, true);
        launcherSettingPage = new LauncherSettingPage(getContext(), PAGE_ID_SETTING_LAUNCHER, getParent(), R.layout.page_launcher_setting);
        helpPage = new HelpPage(getContext(), PAGE_ID_SETTING_HELP, getParent(), R.layout.page_help);
        communityPage = new CommunityPage(getContext(), PAGE_ID_SETTING_COMMUNITY, getParent(), R.layout.page_community);
        aboutPage = new AboutPage(getContext(), PAGE_ID_SETTING_ABOUT, getParent(), R.layout.page_about);

        versionSettingPage.loadVersion(Profiles.getSelectedProfile(), null);

        if (listener != null) {
            listener.onLoad();
        }
    }

    @Override
    public ArrayList<FCLCommonPage> getAllPages() {
        ArrayList<FCLCommonPage> pages = new ArrayList<>();
        pages.add(versionSettingPage);
        pages.add(launcherSettingPage);
        pages.add(helpPage);
        pages.add(communityPage);
        pages.add(aboutPage);
        return pages;
    }
}
