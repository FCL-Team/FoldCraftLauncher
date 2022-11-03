package com.tungsten.fcl.ui;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.main.MainUI;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.ui.FCLBaseUI;
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

    private FCLBaseUI[] allUIs;
    private FCLBaseUI currentUI;

    public UIManager(Context context, FCLUILayout parent) {
        this.context = context;
        this.parent = parent;
    }

    public void init() {
        if (instance != null) {
            Logging.LOG.log(Level.WARNING, "UIManager already initialized!");
            return;
        }
        instance = this;

        mainUI = new MainUI(context, parent, R.layout.ui_main);

        allUIs = new FCLBaseUI[] {
                mainUI
        };
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
}
