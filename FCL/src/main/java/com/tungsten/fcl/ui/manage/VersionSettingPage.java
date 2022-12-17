package com.tungsten.fcl.ui.manage;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatSpinner;

import com.tungsten.fcl.R;
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
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSeekBar;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class VersionSettingPage extends FCLCommonPage implements ManageUI.VersionLoadable {

    private final boolean globalSetting;

    private VersionSetting lastVersionSetting = null;
    private Profile profile;
    private WeakListenerHolder listenerHolder;
    private String versionId;

    private FCLLinearLayout settingTypeLayout;
    private FCLLinearLayout settingLayout;

    private FCLEditText txtWidth;
    private FCLEditText txtHeight;
    private FCLEditText txtJVMArgs;
    private FCLEditText txtGameArgs;
    private FCLEditText txtMetaspace;
    private FCLEditText txtServerIP;

    private FCLCheckBox chkAutoAllocate;
    private FCLCheckBox chkFullscreen;

    private FCLSeekBar allocateSeekbar;

    private FCLProgressBar memoryBar;

    private FCLSwitch specialSettingSwitch;
    private FCLSwitch isolateWorkingDirSwitch;
    private FCLSwitch beGestureSwitch;
    private FCLSwitch noGameCheckSwitch;
    private FCLSwitch noJVMCheckSwitch;

    private AppCompatSpinner javaSpinner;
    private AppCompatSpinner processPrioritySpinner;
    private AppCompatSpinner rendererSpinner;

    private FCLImageButton editIconButton;
    private FCLImageButton controllerButton;

    private FCLTextView memoryText;
    private FCLTextView memoryInfoText;
    private FCLTextView memoryAllocateText;

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
        settingTypeLayout = findViewById(R.id.special_setting_layout);
        settingLayout = findViewById(R.id.setting_layout);

        txtWidth = findViewById(R.id.edit_width);
        txtHeight = findViewById(R.id.edit_height);
        txtJVMArgs = findViewById(R.id.edit_jvm_args);
        txtGameArgs = findViewById(R.id.edit_minecraft_args);
        txtMetaspace = findViewById(R.id.edit_permgen_space);
        txtServerIP = findViewById(R.id.edit_server);

        chkAutoAllocate = findViewById(R.id.edit_auto_allocate);
        chkFullscreen = findViewById(R.id.edit_fullscreen);

        allocateSeekbar = findViewById(R.id.edit_memory);

        memoryBar = findViewById(R.id.memory_bar);

        specialSettingSwitch = findViewById(R.id.enable_per_instance_setting);
        isolateWorkingDirSwitch = findViewById(R.id.edit_game_dir);
        beGestureSwitch = findViewById(R.id.edit_controller_injector);
        noGameCheckSwitch = findViewById(R.id.edit_not_check_game);
        noJVMCheckSwitch = findViewById(R.id.edit_not_check_java);

        javaSpinner = findViewById(R.id.edit_java);
        processPrioritySpinner = findViewById(R.id.edit_process_priority);
        rendererSpinner = findViewById(R.id.edit_renderer);

        editIconButton = findViewById(R.id.edit_icon);
        controllerButton = findViewById(R.id.edit_controller);

        memoryText = findViewById(R.id.memory_text);
        memoryInfoText = findViewById(R.id.memory_info_text);
        memoryAllocateText = findViewById(R.id.memory_allocate_text);

        settingTypeLayout.setVisibility(globalSetting ? View.GONE : View.VISIBLE);

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
