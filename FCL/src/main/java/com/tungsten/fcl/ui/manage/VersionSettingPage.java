package com.tungsten.fcl.ui.manage;

import android.content.Context;

import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.VersionSetting;
import com.tungsten.fcl.util.WeakListenerHolder;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class VersionSettingPage extends FCLCommonPage implements ManageUI.VersionLoadable {

    private final boolean globalSetting;

    private VersionSetting lastVersionSetting = null;
    private Profile profile;
    private WeakListenerHolder listenerHolder;
    private String versionId;

    private FCLLinearLayout settingTypeLayout;

    private FCLEditText txtWidth;
    private FCLEditText txtHeight;
    private FCLEditText txtJVMArgs;
    private FCLEditText txtGameArgs;
    private FCLEditText txtMetaspace;
    private FCLEditText txtServerIP;

    private FCLCheckBox chkAutoAllocate;
    private FCLCheckBox chkFullscreen;

    private FCLSwitch noGameCheckPane;
    private FCLSwitch noJVMCheckPane;

    private final StringProperty selectedVersion = new SimpleStringProperty();
    private final BooleanProperty enableSpecificSettings = new SimpleBooleanProperty(false);
    private final IntegerProperty maxMemory = new SimpleIntegerProperty();
    //private final ObjectProperty<OperatingSystem.PhysicalMemoryStatus> memoryStatus = new SimpleObjectProperty<>(OperatingSystem.PhysicalMemoryStatus.INVALID);
    private final BooleanProperty modpack = new SimpleBooleanProperty();

    public VersionSettingPage(Context context, int id, FCLUILayout parent, int resId, boolean globalSetting) {
        super(context, id, parent, resId);
        this.globalSetting = globalSetting;
        create();
    }

    private void create() {
        if (!globalSetting) {
            
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void loadVersion(Profile profile, String version) {

    }
}
