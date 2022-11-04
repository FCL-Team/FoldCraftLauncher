package com.tungsten.fcl.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.ConfigHolder;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLDynamicIsland;
import com.tungsten.fcllibrary.component.view.FCLTabLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.IOException;
import java.util.logging.Level;

public class MainActivity extends FCLActivity implements TabLayout.OnTabSelectedListener {

    public ConstraintLayout background;
    private FCLDynamicIsland titleView;

    private UIManager uiManager;
    public FCLUILayout uiLayout;

    private FCLTabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background = findViewById(R.id.background);
        background.setBackground(ThemeEngine.getInstance().getTheme().getBackground(this));

        titleView = findViewById(R.id.title);

        uiLayout = findViewById(R.id.ui_layout);
        uiLayout.post(() -> {
            uiManager = new UIManager(this, uiLayout);
            uiManager.init();

            tabLayout = findViewById(R.id.tab_layout);
            tabLayout.addOnTabSelectedListener(this);
            tabLayout.selectTab(tabLayout.getTabAt(3));
            ThemeEngine.getInstance().registerEvent(tabLayout, () -> {
                tabLayout.setBackgroundColor(ThemeEngine.getInstance().getTheme().getColor());
            });
        });

        try {
            ConfigHolder.init();
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                titleView.setTextWithAnim(getString(R.string.account));
                uiManager.switchUI(uiManager.getAccountUI());
                break;
            case 1:
                titleView.setTextWithAnim(getString(R.string.version));
                uiManager.switchUI(uiManager.getVersionUI());
                break;
            case 2:
                titleView.setTextWithAnim(getString(R.string.install));
                uiManager.switchUI(uiManager.getInstallUI());
                break;
            case 4:
                titleView.setTextWithAnim(getString(R.string.download));
                uiManager.switchUI(uiManager.getDownloadUI());
                break;
            case 5:
                titleView.setTextWithAnim(getString(R.string.multiplayer));
                uiManager.switchUI(uiManager.getMultiplayerUI());
                break;
            case 6:
                titleView.setTextWithAnim(getString(R.string.setting));
                uiManager.switchUI(uiManager.getSettingUI());
                break;
            default:
                titleView.setTextWithAnim(getString(R.string.app_name));
                uiManager.switchUI(uiManager.getMainUI());
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // Ignore
    }

    @Override
    public void onBackPressed() {
        if (uiManager.getCurrentUI() == uiManager.getMainUI()) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
        else {
            tabLayout.selectTab(tabLayout.getTabAt(3));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (uiManager != null) {
            uiManager.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (uiManager != null) {
            uiManager.onResume();
        }
    }
}