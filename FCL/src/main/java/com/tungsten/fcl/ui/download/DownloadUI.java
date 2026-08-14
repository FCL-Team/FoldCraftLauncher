package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.view.View;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.download.common.DownloadPage;
import com.tungsten.fcl.ui.download.modpack.ModpackDownloadPage;
import com.tungsten.fcl.ui.download.version.VersionInstallPage;
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.function.Consumer;

public class DownloadUI extends FCLMultiPageUI {

    public static final int PAGE_ID_DOWNLOAD_GAME = 15010;
    public static final int PAGE_ID_DOWNLOAD_MODPACK = 15011;
    public static final int PAGE_ID_DOWNLOAD_MOD = 15012;
    public static final int PAGE_ID_DOWNLOAD_RESOURCE_PACK = 15013;
    public static final int PAGE_ID_DOWNLOAD_WORLD = 15014;
    public static final int PAGE_ID_DOWNLOAD_SHADER_PACK = 15015;

    public FCLTabLayout tabLayout;
    public FCLUILayout container;

    private final Consumer<Profile> versionsListener = this::loadVersions;
    private Profile listenerProfile;
    private InvalidationListener selectedVersionListener;

    public DownloadUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.container);
        setupPages(container, tabLayout);

        Profiles.registerVersionsListener(versionsListener);

        // UI 被 ViewPager 回收时注销监听（替代原 onDestroy 生命周期），防止静态列表累积泄漏
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Profiles.unregisterVersionsListener(versionsListener);
                if (selectedVersionListener != null) {
                    listenerProfile.selectedVersionProperty().removeListener(selectedVersionListener);
                }
            }
        });
    }

    @Override
    public int getPageCount() {
        return 6;
    }

    @Override
    public FCLPage createPage(int position) {
        switch (position) {
            case 1:
                return new ModpackDownloadPage(getContext(), PAGE_ID_DOWNLOAD_MODPACK, R.layout.page_download);
            case 2:
                return new ModDownloadPage(getContext(), PAGE_ID_DOWNLOAD_MOD, R.layout.page_download);
            case 3:
                return new ResourcePackDownloadPage(getContext(), PAGE_ID_DOWNLOAD_RESOURCE_PACK, R.layout.page_download);
            case 4:
                return new DownloadPage(getContext(), PAGE_ID_DOWNLOAD_WORLD, R.layout.page_download, CurseForgeRemoteModRepository.WORLDS);
            case 5:
                return new ShaderPackDownloadPage(getContext(), PAGE_ID_DOWNLOAD_SHADER_PACK, R.layout.page_download);
            default:
                return new VersionInstallPage(getContext(), PAGE_ID_DOWNLOAD_GAME, R.layout.page_install_version);
        }
    }

    @Override
    public String[] getTabTitles() {
        return new String[]{
                getContext().getString(R.string.version_game),
                getContext().getString(R.string.modpack),
                getContext().getString(R.string.mods),
                getContext().getString(R.string.resourcepack),
                getContext().getString(R.string.world),
                getContext().getString(R.string.shaderpack)
        };
    }

    @Override
    protected void onPageCreated(FCLPage page) {
        if (page instanceof VersionLoadable) {
            ((VersionLoadable) page).loadVersion(Profiles.getSelectedProfile(), null);
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    private void loadVersions(Profile profile) {
        if (profile == Profiles.getSelectedProfile()) {
            forEachCreatedPage(page -> {
                if (page instanceof VersionLoadable) {
                    ((VersionLoadable) page).loadVersion(profile, null);
                }
            });
            // 先移除旧监听再添加，避免重复注册累积（引用旧 UI 实例导致泄漏）
            if (selectedVersionListener != null) {
                listenerProfile.selectedVersionProperty().removeListener(selectedVersionListener);
            }
            selectedVersionListener = observable -> forEachCreatedPage(page -> {
                if (page instanceof VersionLoadable) {
                    ((VersionLoadable) page).loadVersion(profile, null);
                }
            });
            listenerProfile = profile;
            profile.selectedVersionProperty().addListener(selectedVersionListener);
        }
    }
}
