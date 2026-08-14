package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.FCLActivity;

/**
 * 页面基类：只承载 contentView 与业务逻辑，无生命周期方法。
 *
 * 页面由 ViewPager2 / 覆盖层挂载，随创建/销毁（不保留状态），
 * 构造器即完成全部初始化（setContentView + onCreate）。
 */
public abstract class FCLPage {

    /** 临时页统一 id（临时页不参与页面注册表，仅作构造参数） */
    public static final int PAGE_ID_TEMP = -10000;

    private final Context context;
    private final FCLActivity activity;
    private final int id;

    private View contentView;

    public FCLPage(Context context, int id, @LayoutRes int resId) {
        this.context = context;
        this.activity = (FCLActivity) context;
        this.id = id;
        setContentView(resId);
        onCreate();
    }

    public Context getContext() {
        return context;
    }

    public FCLActivity getActivity() {
        return activity;
    }

    public int getId() {
        return id;
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

    public boolean isShowing() {
        return contentView != null && contentView.isShown();
    }

    /** 页面创建时初始化（findViewById、绑定监听等），在构造器内执行 */
    public void onCreate() {

    }

    public abstract Task<?> refresh(Object... param);
}
