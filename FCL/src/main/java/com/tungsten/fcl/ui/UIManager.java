package com.tungsten.fcl.ui;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.account.AccountUI;
import com.tungsten.fcl.ui.download.DownloadUI;
import com.tungsten.fcl.ui.manage.ManageUI;
import com.tungsten.fcl.ui.main.MainUI;
import com.tungsten.fcl.ui.multiplayer.MultiplayerUI;
import com.tungsten.fcl.ui.setting.SettingUI;
import com.tungsten.fcl.ui.version.VersionUI;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.ui.FCLBaseUI;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.logging.Level;

public class UIManager {

    private static UIManager instance;

    public static UIManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UIManager not initialized!");
        }
        return instance;
    }

    private final Context context;
    private final FCLUILayout parent;

    private MainUI mainUI;
    private AccountUI accountUI;
    private VersionUI versionUI;
    private ManageUI manageUI;
    private DownloadUI downloadUI;
    private MultiplayerUI multiplayerUI;
    private SettingUI settingUI;

    private FCLBaseUI[] allUIs;
    private FCLBaseUI currentUI;

    private int loadedUI = 0;

    public UIManager(Context context, FCLUILayout parent) {
        this.context = context;
        this.parent = parent;
    }

    public void init(UIListener listener) {
        if (instance != null) {
            Logging.LOG.log(Level.WARNING, "UIManager already initialized!");
            return;
        }
        instance = this;

        mainUI = new MainUI(context, parent, R.layout.ui_main);
        accountUI = new AccountUI(context, parent, R.layout.ui_account);
        versionUI = new VersionUI(context, parent, R.layout.ui_version);
        manageUI = new ManageUI(context, parent, R.layout.ui_manage);
        downloadUI = new DownloadUI(context, parent, R.layout.ui_download);
        multiplayerUI = new MultiplayerUI(context, parent, R.layout.ui_multiplayer);
        settingUI = new SettingUI(context, parent, R.layout.ui_setting);

        allUIs = new FCLBaseUI[] {
                mainUI,
                accountUI,
                versionUI,
                manageUI,
                downloadUI,
                multiplayerUI,
                settingUI
        };

        for (FCLBaseUI ui : allUIs) {
            ((FCLCommonUI) ui).addLoadingCallback(() -> {
                loadedUI++;
                if (loadedUI == allUIs.length) {
                    listener.onLoad();
                }
            });
        }
    }

    public Context getContext() {
        return context;
    }

    public FCLUILayout getParent() {
        return parent;
    }

    public MainUI getMainUI() {
        return mainUI;
    }

    public AccountUI getAccountUI() {
        return accountUI;
    }

    public VersionUI getVersionUI() {
        return versionUI;
    }

    public ManageUI getManageUI() {
        return manageUI;
    }

    public DownloadUI getDownloadUI() {
        return downloadUI;
    }

    public MultiplayerUI getMultiplayerUI() {
        return multiplayerUI;
    }

    public SettingUI getSettingUI() {
        return settingUI;
    }

    public FCLBaseUI getCurrentUI() {
        return currentUI;
    }

    public void switchUI(FCLBaseUI ui) {
        for (FCLBaseUI baseUI : allUIs) {
            if (ui == baseUI) {
                if (currentUI != null) {
                    currentUI.onStop();
                }
                ui.onStart();
                currentUI = ui;
                break;
            }
        }
    }

    public void registerDefaultBackEvent(Runnable runnable) {
        FCLBaseUI.setDefaultBackEvent(runnable);
    }

    public void onBackPressed() {
        if (currentUI != null) {
            currentUI.onBackPressed();
        }
    }

    public void onPause() {
        for (FCLBaseUI baseUI : allUIs) {
            baseUI.onPause();
        }
    }

    public void onResume() {
        for (FCLBaseUI baseUI : allUIs) {
            baseUI.onResume();
        }
    }
}
