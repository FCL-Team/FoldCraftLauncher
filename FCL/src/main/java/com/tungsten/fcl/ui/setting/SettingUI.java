package com.tungsten.fcl.ui.setting;

import android.content.Context;
import android.view.View;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.manage.VersionSettingPage;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class SettingUI extends FCLMultiPageUI {

    public static final int PAGE_ID_SETTING_GAME = 15030;
    public static final int PAGE_ID_SETTING_LAUNCHER = 15031;
    public static final int PAGE_ID_SETTING_HELP = 15032;
    public static final int PAGE_ID_SETTING_ABOUT = 15034;

    private FCLUILayout container;
    public FCLTabLayout tabLayout;

    private InvalidationListener profileListener;

    public SettingUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.container);
        setupPages(container, tabLayout);

        // 切换 Profile 时刷新全局版本设置页（原 onStart 生命周期移除后的兜底）
        profileListener = observable -> {
            FCLPage page = getPage(0);
            if (page instanceof VersionSettingPage) {
                ((VersionSettingPage) page).loadVersion(Profiles.getSelectedProfile(), null);
            }
        };
        Profiles.selectedProfileProperty().addListener(profileListener);
        // UI 被 ViewPager 回收时注销监听（替代原 onDestroy 生命周期），防止泄漏
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Profiles.selectedProfileProperty().removeListener(profileListener);
            }
        });
    }

    @Override
    public int getPageCount() {
        return 4;
    }

    @Override
    public FCLPage createPage(int position) {
        switch (position) {
            case 1:
                return new LauncherSettingPage(getContext(), PAGE_ID_SETTING_LAUNCHER, R.layout.page_setting_launcher);
            case 2:
                return new HelpPage(getContext(), PAGE_ID_SETTING_HELP, R.layout.page_setting_help);
            case 3:
                return new AboutPage(getContext(), PAGE_ID_SETTING_ABOUT, R.layout.page_setting_about);
            default:
                return new VersionSettingPage(getContext(), PAGE_ID_SETTING_GAME, R.layout.page_version_setting, true);
        }
    }

    @Override
    public String[] getTabTitles() {
        return new String[]{
                getContext().getString(R.string.settings_type_global_manage),
                getContext().getString(R.string.settings_launcher),
                getContext().getString(R.string.help),
                getContext().getString(R.string.about)
        };
    }

    @Override
    protected void onPageCreated(FCLPage page) {
        if (page instanceof VersionSettingPage) {
            ((VersionSettingPage) page).loadVersion(Profiles.getSelectedProfile(), null);
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }
}
