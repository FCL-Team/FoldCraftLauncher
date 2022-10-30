package com.tungsten.fcl.activity;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.view.FCLTitleView;

public class MainActivity extends FCLActivity implements TabLayout.OnTabSelectedListener {

    private FCLTitleView titleView;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleView = findViewById(R.id.title);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.selectTab(tabLayout.getTabAt(3));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                titleView.setTitle(getString(R.string.account));
                break;
            case 1:
                titleView.setTitle(getString(R.string.version));
                break;
            case 2:
                titleView.setTitle(getString(R.string.install));
                break;
            case 4:
                titleView.setTitle(getString(R.string.download));
                break;
            case 5:
                titleView.setTitle(getString(R.string.multiplayer));
                break;
            case 6:
                titleView.setTitle(getString(R.string.setting));
                break;
            default:
                titleView.setTitle(getString(R.string.app_name));
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
}