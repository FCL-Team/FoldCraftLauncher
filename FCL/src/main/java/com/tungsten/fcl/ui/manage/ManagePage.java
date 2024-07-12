package com.tungsten.fcl.ui.manage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ScrollView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.ProgressDialog;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.version.Versions;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;

public class ManagePage extends FCLCommonPage implements ManageUI.VersionLoadable, View.OnClickListener {

    private final BooleanProperty currentVersionUpgradable = new SimpleBooleanProperty();

    private ScrollView left;
    private ScrollView right;

    private FCLImageButton browseFCLLog;
    private FCLImageButton browseGame;
    private FCLImageButton browseMod;
    private FCLImageButton browseConfig;
    private FCLImageButton browseResourcepack;
    private FCLImageButton browseShaderPack;
    private FCLImageButton browseScreenshot;
    private FCLImageButton browseSave;
    private FCLImageButton browseLog;
    private FCLImageButton update;
    private FCLImageButton rename;
    private FCLImageButton duplicate;
    private FCLImageButton export;
    private FCLImageButton redownload;
    private FCLImageButton deleteLibs;
    private FCLImageButton deleteLogs;

    private FCLLinearLayout updateLayout;

    public ManagePage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
        create();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void loadVersion(Profile profile, String version) {
        currentVersionUpgradable.set(profile.getRepository().isModpack(version));
    }

    private void create() {
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        ThemeEngine.getInstance().registerEvent(left, () -> left.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() })));
        ThemeEngine.getInstance().registerEvent(right, () -> right.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() })));

        browseFCLLog = findViewById(R.id.browse_fcl_logs);
        browseGame = findViewById(R.id.browse_game_dir);
        browseMod = findViewById(R.id.browse_mods);
        browseConfig = findViewById(R.id.browse_config);
        browseResourcepack = findViewById(R.id.browse_resourcepacks);
        browseShaderPack = findViewById(R.id.browse_shader_packs);
        browseScreenshot = findViewById(R.id.browse_screenshots);
        browseSave = findViewById(R.id.browse_saves);
        browseLog = findViewById(R.id.browse_logs);
        update = findViewById(R.id.update);
        rename = findViewById(R.id.rename);
        duplicate = findViewById(R.id.duplicate);
        export = findViewById(R.id.export);
        redownload = findViewById(R.id.update_assets);
        deleteLibs = findViewById(R.id.delete_libs);
        deleteLogs = findViewById(R.id.delete_logs);
        browseFCLLog.setOnClickListener(this);
        browseGame.setOnClickListener(this);
        browseMod.setOnClickListener(this);
        browseConfig.setOnClickListener(this);
        browseResourcepack.setOnClickListener(this);
        browseShaderPack.setOnClickListener(this);
        browseScreenshot.setOnClickListener(this);
        browseSave.setOnClickListener(this);
        browseLog.setOnClickListener(this);
        update.setOnClickListener(this);
        rename.setOnClickListener(this);
        duplicate.setOnClickListener(this);
        export.setOnClickListener(this);
        redownload.setOnClickListener(this);
        deleteLibs.setOnClickListener(this);
        deleteLogs.setOnClickListener(this);

        updateLayout = findViewById(R.id.update_layout);
        updateLayout.visibilityProperty().bind(currentVersionUpgradable);
    }

    private void onBrowse(String dir) {
        FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
        builder.setLibMode(LibMode.FILE_BROWSER);
        dir = dir.startsWith("/") ? dir : new File(getProfile().getRepository().getRunDirectory(getVersion()), dir).getAbsolutePath();
        builder.setInitDir(dir);
        builder.create().browse(getActivity(), RequestCodes.BROWSE_DIR_CODE, null);
    }

    private void redownloadAssetIndex() {
        Versions.updateGameAssets(getContext(), getProfile(), getVersion());
    }

    private void clearLibraries() {
        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setMessage(String.format(getContext().getString(R.string.version_manage_remove_confirm), "libraries"));
        builder.setPositiveButton(() -> {
            ProgressDialog progress = new ProgressDialog(getContext());
            Task.runAsync(() -> {
                FileUtils.deleteDirectoryQuietly(new File(getProfile().getRepository().getBaseDirectory(), "libraries"));
            }).whenComplete(Schedulers.androidUIThread(), (e) -> {
                progress.dismiss();
            }).start();
        });
        builder.setNegativeButton(null);
        builder.create().show();
    }

    private void clearJunkFiles() {
        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setMessage(String.format(getContext().getString(R.string.version_manage_remove_confirm), "logs"));
        builder.setPositiveButton(() -> {
            ProgressDialog progress = new ProgressDialog(getContext());
            Task.runAsync(() -> {
                Versions.cleanVersion(getProfile(), getVersion());
            }).whenComplete(Schedulers.androidUIThread(), (e) -> {
                progress.dismiss();
            }).start();
        });
        builder.setNegativeButton(null);
        builder.create().show();
    }

    private void updateGame() {
        Versions.updateVersion(getContext(), getParent(), getProfile(), getVersion());
    }

    private void export() {
        Versions.exportVersion(getContext(), getParent(), getProfile(), getVersion());
    }

    private void rename() {
        Versions.renameVersion(getContext(), getProfile(), getVersion())
                .thenApply(newVersionName -> UIManager.getInstance().getManageUI().preferredVersionName = newVersionName);
    }

    private void duplicate() {
        Versions.duplicateVersion(getContext(), getProfile(), getVersion());
    }

    public Profile getProfile() {
        return UIManager.getInstance().getManageUI().getProfile();
    }

    public String getVersion() {
        return UIManager.getInstance().getManageUI().getVersion();
    }

    @Override
    public void onClick(View view) {
        if (view == browseFCLLog) {
            onBrowse(FCLPath.LOG_DIR);
        }
        if (view == browseGame) {
            onBrowse("");
        }
        if (view == browseMod) {
            onBrowse("mods");
        }
        if (view == browseConfig) {
            onBrowse("config");
        }
        if (view == browseResourcepack) {
            onBrowse("resourcepacks");
        }
        if (view == browseShaderPack) {
            onBrowse("shaderpacks");
        }
        if (view == browseScreenshot) {
            onBrowse("screenshots");
        }
        if (view == browseSave) {
            onBrowse("saves");
        }
        if (view == browseLog) {
            onBrowse("logs");
        }
        if (view == update) {
            updateGame();
        }
        if (view == rename) {
            rename();
        }
        if (view == duplicate) {
            duplicate();
        }
        if (view == export) {
            export();
        }
        if (view == redownload) {
            redownloadAssetIndex();
        }
        if (view == deleteLibs) {
            clearLibraries();
        }
        if (view == deleteLogs) {
            clearJunkFiles();
        }
    }
}
