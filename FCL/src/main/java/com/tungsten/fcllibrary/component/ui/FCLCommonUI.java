package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.tungsten.fclcore.task.Task;

public abstract class FCLCommonUI extends FCLBaseUI {

    public FCLCommonUI(Context context, @LayoutRes int id) {
        super(context);
        setContentView(id);
    }

    @Override
    public boolean isShowing() {
        View contentView = getContentView();
        if (contentView == null) return false;
        return contentView.isShown();
    }

    @Override
    public abstract Task<?> refresh(Object... param);

    @Override
    public void onCreate() {
        super.onCreate();
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

}
