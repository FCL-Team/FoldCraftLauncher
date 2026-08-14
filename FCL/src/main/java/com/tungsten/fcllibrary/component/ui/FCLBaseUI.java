package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.FCLActivity;

public abstract class FCLBaseUI {

    private static Runnable defaultBackEvent;

    private final Context context;
    private final FCLActivity activity;

    private View contentView;

    public FCLBaseUI(Context context) {
        this.context = context;
        this.activity = (FCLActivity) context;
    }

    public static void setDefaultBackEvent(Runnable defaultBackEvent) {
        FCLBaseUI.defaultBackEvent = defaultBackEvent;
    }

    public Context getContext() {
        return context;
    }

    public FCLActivity getActivity() {
        return activity;
    }

    public void setContentView(@LayoutRes int id) {
        contentView = LayoutInflater.from(context).inflate(id, null);
    }

    public View getContentView() {
        return contentView;
    }

    @NonNull
    public final <T extends View> T findViewById(int id) {
        return contentView.findViewById(id);
    }

    public abstract boolean isShowing();

    public abstract Task<?> refresh(Object... param);

    public void onCreate() {

    }



    public void onBackPressed() {
        if (defaultBackEvent != null && isShowing()) {
            Schedulers.androidUIThread().execute(defaultBackEvent);
        }
    }

    public void onPause() {

    }

    public void onResume() {

    }

}
