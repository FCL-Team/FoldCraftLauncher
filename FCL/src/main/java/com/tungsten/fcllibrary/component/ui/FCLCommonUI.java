package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.anim.DisplayAnimUtils;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public abstract class FCLCommonUI extends FCLBaseUI {

    private final FCLUILayout parent;

    private boolean init = false;

    public FCLCommonUI(Context context, FCLUILayout parent, @LayoutRes int id) {
        super(context);
        this.parent = parent;
        setContentView(id);
    }

    @Override
    public boolean isShowing() {
        View contentView = getContentView();
        if (contentView == null) return false;
        return contentView.getVisibility() == View.VISIBLE;
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
        // onCreate 延迟到首次 onStart 执行，确保子类字段已初始化完成
        // （父类构造器中调用可重写方法时，子类字段尚未初始化，会触发 NPE）
        if (!init) {
            onCreate();
            init = true;
        }
        DisplayAnimUtils.showViewWithAnim(getContentView(), R.anim.ui_show);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (init) {
            DisplayAnimUtils.hideViewWithAnim(getContentView(), R.anim.ui_hide);
        }
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
        parent.removeView(getContentView());
    }
}
