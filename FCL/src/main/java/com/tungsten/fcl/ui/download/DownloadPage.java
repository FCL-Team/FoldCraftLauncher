package com.tungsten.fcl.ui.download;

import android.content.Context;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class DownloadPage extends FCLCommonPage {

    public DownloadPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }
}
