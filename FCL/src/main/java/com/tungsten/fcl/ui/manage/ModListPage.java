package com.tungsten.fcl.ui.manage;

import android.content.Context;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class ModListPage extends FCLCommonPage {
    public ModListPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }
}
