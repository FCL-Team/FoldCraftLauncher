package com.tungsten.fcl.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.view.MenuView;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLImageView;

public class ControllerActivity extends FCLActivity {

    private GameMenu menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FCLImageView contentView = new FCLImageView(this);
        contentView.setBackground(ThemeEngine.getInstance().getTheme().getBackground(this));
        setContentView(contentView);

        // 3.7 主题色对齐：系统栏底色透明。全屏沉浸下边缘滑动短暂呼出系统栏时，
        // 不再露出 themes.xml 中静态写死的 default_theme_color（与用户主题色可能不一致）。
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        menu = new GameMenu();
        menu.setup(this, null);

        addContentView(menu.getLayout(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)) {
            DrawerLayout drawerLayout = (DrawerLayout) menu.getLayout();
            drawerLayout.openDrawer(GravityCompat.START, true);
            drawerLayout.openDrawer(GravityCompat.END, true);
        }
        return super.dispatchKeyEvent(event);
    }
}
