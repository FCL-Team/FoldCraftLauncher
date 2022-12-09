package com.tungsten.fcl.ui.manage;

import android.content.Context;

import com.tungsten.fcl.setting.Profile;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class VersionSettingPage extends FCLCommonPage implements ManageUI.VersionLoadable {

    private final boolean globalSetting;

    public VersionSettingPage(Context context, int id, FCLUILayout parent, int resId, boolean globalSetting) {
        super(context, id, parent, resId);
        this.globalSetting = globalSetting;
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void loadVersion(Profile profile, String version) {

    }
}
