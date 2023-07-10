package com.tungsten.fcl.ui.manage;

import android.content.Context;

import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.List;

public class ModUpdatesPage extends FCLTempPage {

    public ModUpdatesPage(Context context, int id, FCLUILayout parent, int resId, ModManager modManager, List<LocalModFile.ModUpdate> list) {
        super(context, id, parent, resId);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }
}
