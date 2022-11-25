package com.tungsten.fcl.ui.download;

import android.content.Context;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class DownloadUI extends FCLCommonUI {
    public DownloadUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }
}
