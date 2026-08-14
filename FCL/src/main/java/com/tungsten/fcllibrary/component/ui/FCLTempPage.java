package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public abstract class FCLTempPage extends FCLBasePage {

    private final FCLUILayout parent;

    public FCLTempPage(Context context, int id, FCLUILayout parent, @LayoutRes int resId) {
        super(context, id);
        this.parent = parent;
        setContentView(resId);
        onCreate();
    }

    public FCLUILayout getParent() {
        return parent;
    }

    @Override
    public boolean isShowing() {
        return getContentView().getVisibility() == View.VISIBLE;
    }

    @Override
    public abstract Task<?> refresh(Object... param);

    @Override
    public void onCreate() {
        super.onCreate();
        getContentView().setVisibility(View.GONE);
        parent.addView(getContentView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
