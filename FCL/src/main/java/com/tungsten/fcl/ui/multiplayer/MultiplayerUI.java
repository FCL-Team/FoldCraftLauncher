package com.tungsten.fcl.ui.multiplayer;

import android.content.Context;

import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class MultiplayerUI extends FCLCommonUI {
    public MultiplayerUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }
}
