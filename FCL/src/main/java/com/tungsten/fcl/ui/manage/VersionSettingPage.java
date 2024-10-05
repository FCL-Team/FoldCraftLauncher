package com.tungsten.fcl.ui.manage;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.SelectControllerDialog;
import com.tungsten.fcl.game.FCLGameRepository;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.VersionSetting;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fcl.util.WeakListenerHolder;
import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.event.Event;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.game.JavaVersion;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.platform.MemoryUtils;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSeekBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class VersionSettingPage extends FCLCommonPage implements ManageUI.VersionLoadable, View.OnClickListener {

    private final boolean globalSetting;

    private VersionSetting lastVersionSetting = null;
    private Profile profile;
    private WeakListenerHolder listenerHolder;
    private String versionId;

    private FCLEditText txtJVMArgs;
    private FCLEditText txtGameArgs;
    private FCLEditText txtMetaspace;
    private FCLEditText txtServerIP;

    private FCLCheckBox chkAutoAllocate;

    private FCLImageView iconView;

    private FCLSeekBar allocateSeekbar;
    private FCLSeekBar scaleFactorSeekbar;

    private FCLSwitch isolateWorkingDirSwitch;
    private FCLSwitch beGestureSwitch;
    private FCLSwitch vulkanDriverSystemSwitch;
    private FCLSwitch noGameCheckSwitch;
    private FCLSwitch noJVMCheckSwitch;

    private FCLSpinner<String> javaSpinner;
    private FCLSpinner<FCLConfig.Renderer> rendererSpinner;

    private FCLImageButton editIconButton;
    private FCLImageButton deleteIconButton;
    private FCLImageButton controllerButton;

    private final InvalidationListener specificSettingsListener;
    private final StringProperty selectedVersion = new SimpleStringProperty();
    private final BooleanProperty enableSpecificSettings = new SimpleBooleanProperty(false);
    private final IntegerProperty maxMemory = new SimpleIntegerProperty();
    private final IntegerProperty usedMemory = new SimpleIntegerProperty(0);
    private final BooleanProperty modpack = new SimpleBooleanProperty();

    public VersionSettingPage(Context context, int id, FCLUILayout parent, int resId, boolean globalSetting) {
        super(context, id, parent, resId);
        this.globalSetting = globalSetting;
        create();
        specificSettingsListener = any -> enableSpecificSettings.set(!lastVersionSetting.isUsesGlobal());
    }

    private void create() {
        FCLLinearLayout settingTypeLayout = findViewById(R.id.special_setting_layout);
        FCLLinearLayout settingLayout = findViewById(R.id.setting_layout);

        txtJVMArgs = findViewById(R.id.edit_jvm_args);
        txtGameArgs = findViewById(R.id.edit_minecraft_args);
        txtMetaspace = findViewById(R.id.edit_permgen_space);
        txtServerIP = findViewById(R.id.edit_server);

        chkAutoAllocate = findViewById(R.id.edit_auto_allocate);

        iconView = findViewById(R.id.icon);

        allocateSeekbar = findViewById(R.id.edit_memory);
        scaleFactorSeekbar = findViewById(R.id.edit_scale_factor);

        FCLSwitch specialSettingSwitch = findViewById(R.id.enable_per_instance_setting);
        specialSettingSwitch.addCheckedChangeListener();
        isolateWorkingDirSwitch = findViewById(R.id.edit_game_dir);
        beGestureSwitch = findViewById(R.id.edit_controller_injector);
        vulkanDriverSystemSwitch = findViewById(R.id.vulkan_driver_system);
        noGameCheckSwitch = findViewById(R.id.edit_not_check_game);
        noJVMCheckSwitch = findViewById(R.id.edit_not_check_java);

        isolateWorkingDirSwitch.disableProperty().bind(modpack);

        javaSpinner = findViewById(R.id.edit_java);
        rendererSpinner = findViewById(R.id.edit_renderer);

        FCLTextView scaleFactorText = findViewById(R.id.scale_factor_text);

        scaleFactorSeekbar.addProgressListener();
        scaleFactorText.stringProperty().bind(Bindings.createStringBinding(() -> (int) (lastVersionSetting.getScaleFactor() * 100) + " %", scaleFactorSeekbar.percentProgressProperty()));

        // add spinner data
        ArrayList<String> javaVersionDataList = new ArrayList<>();
        javaVersionDataList.add(JavaVersion.JAVA_AUTO.getVersionName());
        javaVersionDataList.add(JavaVersion.JAVA_8.getVersionName());
        javaVersionDataList.add(JavaVersion.JAVA_11.getVersionName());
        javaVersionDataList.add(JavaVersion.JAVA_17.getVersionName());
        javaVersionDataList.add(JavaVersion.JAVA_21.getVersionName());
        javaSpinner.setDataList(javaVersionDataList);

        ArrayList<FCLConfig.Renderer> rendererDataList = new ArrayList<>();
        rendererDataList.add(FCLConfig.Renderer.RENDERER_GL4ES);
        rendererDataList.add(FCLConfig.Renderer.RENDERER_VIRGL);
        rendererDataList.add(FCLConfig.Renderer.RENDERER_LTW);
        rendererDataList.add(FCLConfig.Renderer.RENDERER_VGPU);
        rendererDataList.add(FCLConfig.Renderer.RENDERER_ZINK);
        rendererDataList.add(FCLConfig.Renderer.RENDERER_FREEDRENO);
        rendererSpinner.setDataList(rendererDataList);

        // add spinner text
        ArrayList<String> javaVersionList = new ArrayList<>();
        javaVersionList.add(getContext().getString(R.string.settings_game_java_version_auto));
        javaVersionList.add("JRE 8");
        javaVersionList.add("JRE 11");
        javaVersionList.add("JRE 17");
        javaVersionList.add("JRE 21");
        ArrayAdapter<String> javaAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, javaVersionList);
        javaAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        javaSpinner.setAdapter(javaAdapter);

        ArrayList<String> rendererList = new ArrayList<>();
        rendererList.add(getContext().getString(R.string.settings_fcl_renderer_gl4es));
        rendererList.add(getContext().getString(R.string.settings_fcl_renderer_virgl));
        rendererList.add(getContext().getString(R.string.settings_fcl_renderer_ltw));
        rendererList.add(getContext().getString(R.string.settings_fcl_renderer_vgpu));
        rendererList.add(getContext().getString(R.string.settings_fcl_renderer_zink));
        rendererList.add(getContext().getString(R.string.settings_fcl_renderer_freedreno));
        ArrayAdapter<String> rendererAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, rendererList);
        rendererAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        rendererSpinner.setAdapter(rendererAdapter);

        editIconButton = findViewById(R.id.edit_icon);
        deleteIconButton = findViewById(R.id.delete_icon);
        controllerButton = findViewById(R.id.edit_controller);

        editIconButton.setOnClickListener(this);
        deleteIconButton.setOnClickListener(this);
        controllerButton.setOnClickListener(this);

        FCLProgressBar memoryBar = findViewById(R.id.memory_bar);

        FCLTextView memoryStateText = findViewById(R.id.memory_state);
        FCLTextView memoryText = findViewById(R.id.memory_text);
        FCLTextView memoryInfoText = findViewById(R.id.memory_info_text);
        FCLTextView memoryAllocateText = findViewById(R.id.memory_allocate_text);

        memoryStateText.stringProperty().bind(Bindings.createStringBinding(() -> {
            if (chkAutoAllocate.isChecked()) {
                return getContext().getString(R.string.settings_memory_lower_bound);
            } else {
                return getContext().getString(R.string.settings_memory);
            }
        }, chkAutoAllocate.checkProperty()));

        allocateSeekbar.setMax(MemoryUtils.getTotalDeviceMemory(getContext()));
        memoryBar.setMax(MemoryUtils.getTotalDeviceMemory(getContext()));

        allocateSeekbar.addProgressListener();
        allocateSeekbar.progressProperty().bindBidirectional(maxMemory);

        memoryText.stringProperty().bind(Bindings.createStringBinding(() -> allocateSeekbar.progressProperty().intValue() + " MB", allocateSeekbar.progressProperty()));
        memoryText.setOnClickListener(v->{
            EditDialog dialog = new EditDialog(getContext(), s -> {
                if (s.matches("\\d+(\\.\\d+)?$")) {
                   allocateSeekbar.setProgress(Integer.parseInt(s));
                }
            });
            dialog.getEditText().setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
            dialog.show();
        });

        memoryBar.firstProgressProperty().bind(usedMemory);
        memoryBar.secondProgressProperty().bind(Bindings.createIntegerBinding(() -> {
            int allocate = (int) (FCLGameRepository.getAllocatedMemory(maxMemory.intValue() * 1024L * 1024L, MemoryUtils.getFreeDeviceMemory(getContext()) * 1024L * 1024L, chkAutoAllocate.isChecked()) / 1024. / 1024);
            return usedMemory.intValue() + (chkAutoAllocate.isChecked() ? allocate : maxMemory.intValue());
        }, usedMemory, maxMemory, chkAutoAllocate.checkProperty()));

        memoryInfoText.stringProperty().bind(Bindings.createStringBinding(() -> AndroidUtils.getLocalizedText(getContext(), "settings_memory_used_per_total", MemoryUtils.getUsedDeviceMemory(getContext()) / 1024., MemoryUtils.getTotalDeviceMemory(getContext()) / 1024.), usedMemory));

        memoryAllocateText.stringProperty().bind(Bindings.createStringBinding(() -> {
            long maxMemory = Lang.parseInt(this.maxMemory.get(), 0) * 1024L * 1024L;
            return AndroidUtils.getLocalizedText(getContext(), maxMemory / 1024. / 1024. > MemoryUtils.getFreeDeviceMemory(getContext())
                            ? (chkAutoAllocate.isChecked() ? "settings_memory_allocate_auto_exceeded" : "settings_memory_allocate_manual_exceeded")
                            : (chkAutoAllocate.isChecked() ? "settings_memory_allocate_auto" : "settings_memory_allocate_manual"),
                    maxMemory / 1024. / 1024. / 1024.,
                    FCLGameRepository.getAllocatedMemory(maxMemory, MemoryUtils.getFreeDeviceMemory(getContext()) * 1024L * 1024L, chkAutoAllocate.isChecked()) / 1024. / 1024. / 1024.,
                    MemoryUtils.getFreeDeviceMemory(getContext()) / 1024.);
        }, usedMemory, maxMemory, chkAutoAllocate.checkProperty()));

        settingTypeLayout.setVisibility(globalSetting ? View.GONE : View.VISIBLE);

        if (!globalSetting) {
            specialSettingSwitch.disableProperty().bind(modpack);
            specialSettingSwitch.checkProperty().bindBidirectional(enableSpecificSettings);
            settingLayout.visibilityProperty().bind(enableSpecificSettings);
        }

        enableSpecificSettings.addListener((a, b, newValue) -> {
            if (versionId == null) return;

            // do not call versionSettings.setUsesGlobal(true/false)
            // because versionSettings can be the global one.
            // global versionSettings.usesGlobal is always true.
            if (newValue)
                profile.getRepository().specializeVersionSetting(versionId);
            else
                profile.getRepository().globalizeVersionSetting(versionId);

            Schedulers.androidUIThread().execute(() -> loadVersion(profile, versionId));
        });
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        usedMemory.set(MemoryUtils.getUsedDeviceMemory(getContext()));
    }

    @Override
    public void loadVersion(Profile profile, String versionId) {
        this.profile = profile;
        this.versionId = versionId;
        this.listenerHolder = new WeakListenerHolder();

        if (versionId == null) {
            enableSpecificSettings.set(true);
            listenerHolder.add(FXUtils.onWeakChangeAndOperate(profile.selectedVersionProperty(), this.selectedVersion::setValue));
        }

        VersionSetting versionSetting = profile.getVersionSetting(versionId);
        versionSetting.checkController();

        modpack.set(versionId != null && profile.getRepository().isModpack(versionId));
        usedMemory.set(MemoryUtils.getUsedDeviceMemory(getContext()));

        InvalidationListener listener = observable -> ManagePageManager.getInstance().onRunDirectoryChange(profile, versionId);

        // unbind data fields
        if (lastVersionSetting != null) {
            lastVersionSetting.isolateGameDirProperty().removeListener(listener);
            FXUtils.unbind(txtJVMArgs, lastVersionSetting.javaArgsProperty());
            FXUtils.unbind(txtGameArgs, lastVersionSetting.minecraftArgsProperty());
            FXUtils.unbind(txtMetaspace, lastVersionSetting.permSizeProperty());
            FXUtils.unbind(txtServerIP, lastVersionSetting.serverIpProperty());
            FXUtils.unbindBoolean(chkAutoAllocate, lastVersionSetting.autoMemoryProperty());
            FXUtils.unbindBoolean(isolateWorkingDirSwitch, lastVersionSetting.isolateGameDirProperty());
            FXUtils.unbindBoolean(noGameCheckSwitch, lastVersionSetting.notCheckGameProperty());
            FXUtils.unbindBoolean(noJVMCheckSwitch, lastVersionSetting.notCheckJVMProperty());
            FXUtils.unbindBoolean(beGestureSwitch, lastVersionSetting.beGestureProperty());
            FXUtils.unbindBoolean(vulkanDriverSystemSwitch, lastVersionSetting.VKDriverSystemProperty());
            FXUtils.unbindSelection(javaSpinner, lastVersionSetting.javaProperty());
            FXUtils.unbindSelection(rendererSpinner, lastVersionSetting.rendererProperty());
            scaleFactorSeekbar.percentProgressProperty().unbindBidirectional(lastVersionSetting.scaleFactorProperty());
            maxMemory.unbindBidirectional(lastVersionSetting.maxMemoryProperty());

            lastVersionSetting.usesGlobalProperty().removeListener(specificSettingsListener);
        }

        // bind new data fields
        if (getId() == ManagePageManager.PAGE_ID_MANAGE_SETTING) {
            versionSetting.isolateGameDirProperty().addListener(listener);
        }
        FXUtils.bindString(txtJVMArgs, versionSetting.javaArgsProperty());
        FXUtils.bindString(txtGameArgs, versionSetting.minecraftArgsProperty());
        FXUtils.bindString(txtMetaspace, versionSetting.permSizeProperty());
        FXUtils.bindString(txtServerIP, versionSetting.serverIpProperty());
        FXUtils.bindBoolean(chkAutoAllocate, versionSetting.autoMemoryProperty());
        FXUtils.bindBoolean(isolateWorkingDirSwitch, versionSetting.isolateGameDirProperty());
        FXUtils.bindBoolean(noGameCheckSwitch, versionSetting.notCheckGameProperty());
        FXUtils.bindBoolean(noJVMCheckSwitch, versionSetting.notCheckJVMProperty());
        FXUtils.bindBoolean(beGestureSwitch, versionSetting.beGestureProperty());
        FXUtils.bindBoolean(vulkanDriverSystemSwitch, versionSetting.VKDriverSystemProperty());
        FXUtils.bindSelection(javaSpinner, versionSetting.javaProperty());
        FXUtils.bindSelection(rendererSpinner, versionSetting.rendererProperty());
        scaleFactorSeekbar.percentProgressProperty().bindBidirectional(versionSetting.scaleFactorProperty());
        maxMemory.bindBidirectional(versionSetting.maxMemoryProperty());

        versionSetting.usesGlobalProperty().addListener(specificSettingsListener);
        if (versionId != null)
            enableSpecificSettings.set(!versionSetting.isUsesGlobal());

        lastVersionSetting = versionSetting;

        loadIcon();
    }

    private void onExploreIcon() {
        if (versionId == null)
            return;

        FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".png");
        builder.setLibMode(LibMode.FILE_CHOOSER);
        builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
        builder.setTitle(getContext().getString(R.string.settings_icon));
        builder.setSuffix(suffix);
        builder.create().browse(getActivity(), RequestCodes.SELECT_VERSION_ICON_CODE, (requestCode, resultCode, data) -> {
            if (requestCode == RequestCodes.SELECT_VERSION_ICON_CODE && resultCode == Activity.RESULT_OK && data != null) {
                if (FileBrowser.getSelectedFiles(data).size() == 0)
                    return;

                String path = FileBrowser.getSelectedFiles(data).get(0);
                Uri uri = Uri.parse(path);
                if (AndroidUtils.isDocUri(uri)) {
                    path = AndroidUtils.copyFileToDir(getActivity(), uri, new File(FCLPath.CACHE_DIR));
                }
                if (path == null)
                    return;

                File selectedFile = new File(path);
                File iconFile = profile.getRepository().getVersionIconFile(versionId);
                try {
                    FileUtils.copyFile(selectedFile, iconFile);

                    profile.getRepository().onVersionIconChanged.fireEvent(new Event(this));
                    loadIcon();
                } catch (IOException e) {
                    Logging.LOG.log(Level.SEVERE, "Failed to copy icon file from " + selectedFile + " to " + iconFile, e);
                }
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void onDeleteIcon() {
        if (versionId == null)
            return;

        File iconFile = profile.getRepository().getVersionIconFile(versionId);
        if (iconFile.exists())
            iconFile.delete();
        profile.getRepository().onVersionIconChanged.fireEvent(new Event(this));
        loadIcon();
    }

    private void loadIcon() {
        if (versionId == null) {
            return;
        }

        iconView.setImageDrawable(profile.getRepository().getVersionIconImage(versionId));
    }

    @Override
    public void onClick(View view) {
        if (view == editIconButton) {
            onExploreIcon();
        }
        if (view == deleteIconButton) {
            onDeleteIcon();
        }
        if (view == controllerButton) {
            SelectControllerDialog dialog = new SelectControllerDialog(getContext(), lastVersionSetting.getController(), controller -> lastVersionSetting.setController(controller.getId()));
            dialog.show();
        }
    }
}
