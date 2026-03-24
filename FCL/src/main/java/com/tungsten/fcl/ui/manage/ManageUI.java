package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.Lang.tryCast;

import android.content.Context;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.controller.ControllerPageManager;
import com.tungsten.fcl.util.WeakListenerHolder;
import com.tungsten.fclcore.event.EventBus;
import com.tungsten.fclcore.event.EventPriority;
import com.tungsten.fclcore.event.RefreshedVersionsEvent;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.game.GameRepository;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLBasePage;
import com.tungsten.fcllibrary.component.ui.FCLMultiPageUI;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManageUI extends FCLMultiPageUI implements TabLayout.OnTabSelectedListener {

    private ManagePageManager pageManager;

    private FCLUILayout container;
    private Runnable runnable;

    private final ObjectProperty<Profile.ProfileVersion> version = new SimpleObjectProperty<>();
    private final WeakListenerHolder listenerHolder = new WeakListenerHolder();
    public String preferredVersionName = null;
    public FCLTabLayout tabLayout;

    public ManageUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tabLayout = findViewById(R.id.tab_layout);
        container = findViewById(R.id.container);

        tabLayout.addOnTabSelectedListener(this);
        initPages();
        listenerHolder.add(EventBus.EVENT_BUS.channel(RefreshedVersionsEvent.class).registerWeak(event -> checkSelectedVersion(), EventPriority.HIGHEST));
    }

    @Override
    public void onStart() {
        super.onStart();
        addLoadingCallback(()->{
            // If we jumped to game list page and deleted this version
            // and back to this page, we should return to main page.
            if (!getProfile().getRepository().isLoaded() ||
                    !getProfile().getRepository().hasVersion(getVersion())) {
                Schedulers.androidUIThread().execute(() -> {
                    if (isShowing()) {
                        MainActivity.getInstance().refreshMenuView(null);
                        MainActivity.getInstance().binding.home.setSelected(true);
                    }
                });
                return;
            }
            loadVersion(getVersion(), getProfile());
        });
    }

    @Override
    public void onBackPressed() {
        if (pageManager != null && pageManager.canReturn()) {
            pageManager.dismissCurrentTempPage();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pageManager != null) {
            pageManager.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pageManager != null) {
            pageManager.onResume();
        }
    }

    @Override
    public void initPages() {
        pageManager = new ManagePageManager(getContext(), container, ManagePageManager.PAGE_ID_MANAGE_SETTING, null);
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override
    public ArrayList<FCLBasePage> getAllPages() {
        return pageManager == null ? null : (ArrayList<FCLBasePage>) pageManager.getAllPages().stream().map(it -> tryCast(it, FCLBasePage.class)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    @Override
    public FCLBasePage getPage(int id) {
        return pageManager == null ? null : pageManager.getPageById(id);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (pageManager != null) {
            switch (tab.getPosition()) {
                case 1:
                    pageManager.switchPage(ManagePageManager.PAGE_ID_MANAGE_MANAGE);
                    break;
                case 2:
                    pageManager.switchPage(ManagePageManager.PAGE_ID_MANAGE_INSTALL);
                    break;
                case 3:
                    pageManager.switchPage(ManagePageManager.PAGE_ID_MANAGE_MOD);
                    break;
                case 4:
                    pageManager.switchPage(ManagePageManager.PAGE_ID_MANAGE_WORLD);
                    break;
                default:
                    pageManager.switchPage(ManagePageManager.PAGE_ID_MANAGE_SETTING);
                    break;
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
    }

    public void loadVersion(String version, Profile profile) {
        // If we jumped to game list page and deleted this version
        // and back to this page, we should return to main page.
        if (this.version.get() != null && (!getProfile().getRepository().isLoaded() ||
                !getProfile().getRepository().hasVersion(version))) {
            Schedulers.androidUIThread().execute(() -> {
                if (isShowing()) {
                    MainActivity.getInstance().refreshMenuView(null);
                    MainActivity.getInstance().binding.home.setSelected(true);
                }
            });
            return;
        }

        setVersion(version, profile);
        preferredVersionName = version;

        pageManager.dismissAllTempPages();
        pageManager.loadVersion(profile, version);
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

    public ManagePageManager getPageManager() {
        return pageManager;
    }

    public void checkPageManager(Runnable runnable) {
        this.runnable = runnable;
        if (pageManager != null) {
            runnable.run();
        }
    }
}
