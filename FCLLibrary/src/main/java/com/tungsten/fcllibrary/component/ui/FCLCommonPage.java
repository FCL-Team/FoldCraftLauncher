package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.anim.DisplayAnimUtils;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public abstract class FCLCommonPage extends FCLBasePage {

    private final FCLUILayout parent;

    private final ArrayList<FCLTempPage> allTempPages = new ArrayList<>();
    private FCLTempPage currentTempPage;

    public FCLCommonPage(Context context, int id, FCLUILayout parent, @LayoutRes int resId) {
        super(context, id);
        this.parent = parent;
        setContentView(resId, null);
        onCreate();
    }

    public FCLUILayout getParent() {
        return parent;
    }

    public ArrayList<FCLTempPage> getAllTempPages() {
        return allTempPages;
    }

    public void setCurrentTempPage(FCLTempPage currentTempPage) {
        this.currentTempPage = currentTempPage;
    }

    public FCLTempPage getCurrentTempPage() {
        return currentTempPage;
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
