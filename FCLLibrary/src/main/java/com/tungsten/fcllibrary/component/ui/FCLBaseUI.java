package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.tungsten.fcllibrary.component.FCLActivity;

public abstract class FCLBaseUI implements FCLUILifecycleCallbacks {

    private final Context context;
    private final FCLActivity activity;

    private View contentView;

    public FCLBaseUI(Context context) {
        this.context = context;
        this.activity = (FCLActivity) context;
    }

    public Context getContext() {
        return context;
    }

    public FCLActivity getActivity() {
        return activity;
    }

    public void setContentView(@LayoutRes int id) {
        contentView = LayoutInflater.from(getContext()).inflate(id, null);
    }

    public View getContentView() {
        return contentView;
    }

    public final <T extends View> T findViewById(int id) {
        return contentView == null ? null : contentView.findViewById(id);
    }

    public abstract boolean isShowing();

    public abstract void refresh();

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
