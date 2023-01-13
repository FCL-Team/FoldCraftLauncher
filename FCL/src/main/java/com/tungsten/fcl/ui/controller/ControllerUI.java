package com.tungsten.fcl.ui.controller;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class ControllerUI extends FCLCommonUI implements View.OnClickListener {

    private ListView listView;

    private LinearLayoutCompat importController;
    private LinearLayoutCompat createController;

    private FCLLinearLayout infoLayout;

    public ControllerUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View view) {

    }
}
