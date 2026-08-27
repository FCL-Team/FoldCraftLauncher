package com.tungsten.fcl.ui.manage;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.util.WeakListenerHolder;
import com.tungsten.fclcore.event.EventBus;
import com.tungsten.fclcore.event.EventPriority;
import com.tungsten.fclcore.event.RefreshedVersionsEvent;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.game.GameRepository;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.Optional;

public class ManageUI extends FCLMultiPageUI {

    public static final int PAGE_ID_MANAGE_MANAGE = 15000;
    public static final int PAGE_ID_MANAGE_SETTING = 15001;
    public static final int PAGE_ID_MANAGE_INSTALL = 15002;
    public static final int PAGE_ID_MANAGE_MOD = 15003;
    public static final int PAGE_ID_MANAGE_WORLD = 15004;

    private final ObjectProperty<Profile.ProfileVersion> version = new SimpleObjectProperty<>();
    private final WeakListenerHolder listenerHolder = new WeakListenerHolder();
    public String preferredVersionName = null;
    public FCLTabLayout tabLayout;

    /**
     * 切换 Profile 时重新加载版本设置（页面保留时不经过 onSelect/setVersion 的兜底）
     */
    private final Runnable profileListener = () -> {
        Profile profile = Profiles.getSelectedProfile();
        setVersion(profile.getSelectedVersion(), profile);
    };

    public ManageUI(Context context, int id) {
        super(context, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tabLayout = findViewById(R.id.tab_layout);
        FCLUILayout container = findViewById(R.id.container);
        setupPages(container, tabLayout);

        listenerHolder.add(EventBus.EVENT_BUS.channel(RefreshedVersionsEvent.class).registerWeak(event -> checkSelectedVersion(), EventPriority.HIGHEST));

        // 切换 Profile 时刷新版本设置页（基于 StateFlow），UI 被 ViewPager 回收时注销监听
        Profiles.addSelectedProfileListener(profileListener);
        getContentView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {
                // 页面切走再切回时恢复监听并立即刷新（后台可能已切换 Profile）
                Profiles.removeSelectedProfileListener(profileListener);
                Profiles.addSelectedProfileListener(profileListener);
                profileListener.run();
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {
                Profiles.removeSelectedProfileListener(profileListener);
            }
        });
    }

    @Override
    public int getPageCount() {
        return 5;
    }

    @Override
    public FCLPage createPage(int position) {
        return switch (position) {
            case 1 ->
                    new ManagePage(getContext(), PAGE_ID_MANAGE_MANAGE);
            case 2 ->
                    new InstallerListPage(getContext(), PAGE_ID_MANAGE_INSTALL);
            case 3 -> new ModListPage(getContext(), PAGE_ID_MANAGE_MOD);
            case 4 ->
                    new WorldListPage(getContext(), PAGE_ID_MANAGE_WORLD);
            default ->
                    new VersionSettingPage(getContext(), PAGE_ID_MANAGE_SETTING, false);
        };
    }

    @Override
    public String[] getTabTitles() {
        return new String[]{
                getContext().getString(R.string.settings_game),
                getContext().getString(R.string.manage),
                getContext().getString(R.string.settings_tabs_installers),
                getContext().getString(R.string.mods_manage),
                getContext().getString(R.string.world_manage)
        };
    }

    @Override
    protected void onPageCreated(FCLPage page) {
        // 未 setVersion 时跳过（版本校验由 RefreshedVersionsEvent 事件兜底）
        if (page instanceof VersionLoadable && getProfile() != null) {
            ((VersionLoadable) page).loadVersion(getProfile(), getVersion());
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    private void checkSelectedVersion() {
        Schedulers.androidUIThread().execute(() -> {
            if (this.version.get() == null) return;
            GameRepository repository = this.version.get().getProfile().getRepository();
            if (!repository.hasVersion(this.version.get().getVersion())) {
                if (preferredVersionName != null) {
                    loadVersion(preferredVersionName, this.version.get().getProfile());
                } else if (isShowing()) {
                    MainActivity.getInstance().refreshMenuView(null);
                    MainActivity.getInstance().binding.home.setSelected(true);
                }
            }
        });
    }

    public void setVersion(String version, Profile profile) {
        this.version.set(new Profile.ProfileVersion(profile, version));
        // 分发版本到已创建页面
        forEachCreatedPage(page -> {
            if (page instanceof VersionLoadable) {
                ((VersionLoadable) page).loadVersion(profile, version);
            }
        });
    }

    public void loadVersion(String version, Profile profile) {
        setVersion(version, profile);
        preferredVersionName = version;

        dismissAllTempPages();
        forEachCreatedPage(page -> {
            if (page instanceof VersionLoadable) {
                ((VersionLoadable) page).loadVersion(profile, version);
            }
        });
    }

    /**
     * 游戏目录变更时刷新模组/世界列表（原 ManagePageManager.onRunDirectoryChange）
     */
    public void onRunDirectoryChange(Profile profile, String version) {
        FCLPage modPage = getPage(3);
        if (modPage instanceof VersionLoadable) {
            ((VersionLoadable) modPage).loadVersion(profile, version);
        }
        FCLPage worldPage = getPage(4);
        if (worldPage instanceof VersionLoadable) {
            ((VersionLoadable) worldPage).loadVersion(profile, version);
        }
    }

    public Profile getProfile() {
        return Optional.ofNullable(version.get()).map(Profile.ProfileVersion::getProfile).orElse(null);
    }

    public String getVersion() {
        return Optional.ofNullable(version.get()).map(Profile.ProfileVersion::getVersion).orElse(null);
    }

    public interface VersionLoadable {
        void loadVersion(Profile profile, String version);
    }
}
