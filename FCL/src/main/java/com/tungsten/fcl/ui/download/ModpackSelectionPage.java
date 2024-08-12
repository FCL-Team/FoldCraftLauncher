package com.tungsten.fcl.ui.download;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.ui.manage.ManagePageManager;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.mod.server.ServerModpackManifest;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.GetTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ModpackSelectionPage extends FCLTempPage implements View.OnClickListener {

    private final Profile profile;
    private final String updateVersion;

    private FCLLinearLayout local;
    private FCLLinearLayout remote;

    public ModpackSelectionPage(Context context, int id, FCLUILayout parent, int resId, Profile profile, String updateVersion) {
        super(context, id, parent, resId);
        this.profile = profile;
        this.updateVersion = updateVersion;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        local = findViewById(R.id.local);
        remote = findViewById(R.id.remote);

        local.setOnClickListener(this);
        remote.setOnClickListener(this);
    }

    private void onChooseLocalFile() {
        FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
        builder.setLibMode(LibMode.FILE_CHOOSER);
        builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".zip");
        suffix.add(".mrpack");
        builder.setSuffix(suffix);
        builder.create().browse(getActivity(), RequestCodes.SELECT_MODPACK_CODE, ((requestCode, resultCode, data) -> {
            if (requestCode == RequestCodes.SELECT_MODPACK_CODE && resultCode == Activity.RESULT_OK && data != null) {
                String path = FileBrowser.getSelectedFiles(data).get(0);
                Uri uri = Uri.parse(path);
                if (AndroidUtils.isDocUri(uri)) {
                    path = AndroidUtils.copyFileToDir(getActivity(), uri, new File(FCLPath.CACHE_DIR));
                }
                if (path == null)
                    return;
                File selectedFile = new File(path);
                Schedulers.androidUIThread().execute(() -> {
                    LocalModpackPage page = new LocalModpackPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_modpack, profile, updateVersion, selectedFile);
                    if (updateVersion == null) {
                        DownloadPageManager.getInstance().dismissCurrentTempPage();
                        DownloadPageManager.getInstance().showTempPage(page);
                    } else {
                        ManagePageManager.getInstance().dismissCurrentTempPage();
                        ManagePageManager.getInstance().showTempPage(page);
                    }
                });
            }
        }));
    }

    private void onChooseRemoteFile() {
        ModpackUrlDialog dialog = new ModpackUrlDialog(getContext(), urlString -> {
            try {
                URL url = new URL(urlString);
                if (urlString.endsWith("server-manifest.json")) {
                    // if urlString ends with .json, we assume that the url is server-manifest.json
                    TaskDialog taskDialog = new TaskDialog(getContext(), new TaskCancellationAction(AppCompatDialog::dismiss));
                    taskDialog.setTitle(getContext().getString(R.string.message_downloading));
                    TaskExecutor executor = new GetTask(url).whenComplete(Schedulers.androidUIThread(), (result, e) -> {
                        ServerModpackManifest manifest = JsonUtils.fromMaybeMalformedJson(result, ServerModpackManifest.class);
                        if (manifest == null) {
                            Toast.makeText(getContext(), getContext().getString(R.string.modpack_type_server_malformed), Toast.LENGTH_SHORT).show();
                        } else if (e == null) {
                            RemoteModpackPage page = new RemoteModpackPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_modpack, profile, updateVersion, manifest);
                            if (updateVersion == null) {
                                DownloadPageManager.getInstance().dismissCurrentTempPage();
                                DownloadPageManager.getInstance().showTempPage(page);
                            } else {
                                ManagePageManager.getInstance().dismissCurrentTempPage();
                                ManagePageManager.getInstance().showTempPage(page);
                            }
                        } else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).executor();
                    taskDialog.setExecutor(executor);
                    taskDialog.show();
                    executor.start();
                } else {
                    // otherwise we still consider the file as modpack zip file
                    // since casually the url may not ends with ".zip"
                    Path modpack = Files.createTempFile("modpack", ".zip");

                    TaskDialog taskDialog = new TaskDialog(getContext(), new TaskCancellationAction(AppCompatDialog::dismiss));
                    taskDialog.setTitle(getContext().getString(R.string.message_downloading));
                    TaskExecutor executor = new FileDownloadTask(url, modpack.toFile(), null)
                            .whenComplete(Schedulers.androidUIThread(), e -> {
                                if (e == null) {
                                    LocalModpackPage page = new LocalModpackPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_modpack, profile, updateVersion, modpack.toFile());
                                    if (updateVersion == null) {
                                        DownloadPageManager.getInstance().dismissCurrentTempPage();
                                        DownloadPageManager.getInstance().showTempPage(page);
                                    } else {
                                        ManagePageManager.getInstance().dismissCurrentTempPage();
                                        ManagePageManager.getInstance().showTempPage(page);
                                    }
                                } else {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).executor();
                    taskDialog.setExecutor(executor);
                    taskDialog.show();
                    executor.start();
                }
            } catch (IOException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onClick(View v) {
        if (v == local) {
            onChooseLocalFile();
        }
        if (v == remote) {
            onChooseRemoteFile();
        }
    }
}
