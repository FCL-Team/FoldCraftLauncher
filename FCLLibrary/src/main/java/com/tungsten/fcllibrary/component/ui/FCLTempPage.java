package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.anim.DisplayAnimUtils;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public abstract class FCLTempPage extends FCLBasePage {

    private final FCLUILayout parent;

    public FCLTempPage(Context context, int id, boolean canReturn, FCLUILayout parent, @LayoutRes int resId) {
        super(context, id, canReturn);
        this.parent = parent;
        setContentView(resId, this::onCreate);
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

    @Override
    public void onStart() {
        super.onStart();
        DisplayAnimUtils.showViewWithAnim(getContentView(), R.anim.page_show);
    }

    @Override
    public void onStop() {
        super.onStop();
        DisplayAnimUtils.hideViewWithAnim(getContentView(), R.anim.page_hide);
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
        parent.removeView(getContentView());
    }

    public abstract void onRestart();

    public void dismiss() {
        onStop();
        Handler handler = new Handler();
        handler.postDelayed(this::onDestroy, 800);
    }
}
