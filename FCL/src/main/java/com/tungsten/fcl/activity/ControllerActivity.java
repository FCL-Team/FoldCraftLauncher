package com.tungsten.fcl.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLImageView;

public class ControllerActivity extends FCLActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FCLImageView contentView = new FCLImageView(this);
        contentView.setBackground(ThemeEngine.getInstance().getTheme().getBackground(this));
        setContentView(contentView);

        GameMenu menu = new GameMenu();
        menu.setup(this, null);

        addContentView(menu.getLayout(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
