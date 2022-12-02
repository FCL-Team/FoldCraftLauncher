package com.tungsten.fcllibrary.component.ui;

import android.content.Context;

import androidx.annotation.LayoutRes;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public abstract class FCLMultiPageUI extends FCLCommonUI implements FCLMultiPageUICallback {

    private int defaultPageId;

    public FCLMultiPageUI(Context context, FCLUILayout parent, @LayoutRes int id) {
        super(context, parent, id);
    }

    public void setDefaultPageId(int defaultPageId) {
        this.defaultPageId = defaultPageId;
    }

    public int getDefaultPageId() {
        return defaultPageId;
    }

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    @Override
    public abstract Task<?> refresh(Object... param);

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public abstract void initPages();

    @Override
    public abstract ArrayList<FCLBasePage> getAllPages();

    @Override
    public abstract FCLBasePage getPage(int id);
}
