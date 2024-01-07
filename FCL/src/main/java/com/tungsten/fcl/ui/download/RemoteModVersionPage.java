package com.tungsten.fcl.ui.download;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDialog;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class RemoteModVersionPage extends FCLTempPage {

    private final List<RemoteMod.Version> list;
    private final Profile.ProfileVersion version;
    private final RemoteModVersionPage.DownloadCallback callback;
    private final DownloadPage downloadPage;

    private ListView listView;

    public RemoteModVersionPage(Context context, int id, FCLUILayout parent, int resId, List<RemoteMod.Version> list, Profile.ProfileVersion version, @Nullable RemoteModVersionPage.DownloadCallback callback, DownloadPage downloadPage) {
        super(context, id, parent, resId);
        this.list = list;
        this.version = version;
        this.callback = callback;
        this.downloadPage = downloadPage;
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = findViewById(R.id.list);
        ModVersionAdapter adapter = new ModVersionAdapter(getContext(), list, version -> {
            if (downloadPage.getId() == DownloadPageManager.PAGE_ID_DOWNLOAD_MOD) {
                RemoteModDownloadPage page = new RemoteModDownloadPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_download_addon, this.version, version, callback, this, downloadPage);
                DownloadPageManager.getInstance().showTempPage(page);
            } else {
                download(version);
            }
        });
        listView.setAdapter(adapter);
    }

    public void download(RemoteMod.Version file) {
        if (this.callback == null) {
            saveAs(file);
        } else {
            this.callback.download(version.getProfile(), version.getVersion(), file);
        }
    }

    public void saveAs(RemoteMod.Version file) {
        FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
        builder.setLibMode(LibMode.FOLDER_CHOOSER);
        builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
        builder.create().browse(getActivity(), RequestCodes.SELECT_DOWNLOAD_FOLDER_CODE, ((requestCode, resultCode, data) -> {
            if (requestCode == RequestCodes.SELECT_DOWNLOAD_FOLDER_CODE && resultCode == Activity.RESULT_OK && data != null) {
                String folder = FileBrowser.getSelectedFiles(data).get(0);
                if (folder == null)
                    return;
                TaskDialog dialog = new TaskDialog(getContext(), new TaskCancellationAction(AppCompatDialog::dismiss));
                dialog.setTitle(getContext().getString(R.string.message_downloading));
                Schedulers.androidUIThread().execute(() -> {
                    TaskExecutor executor = Task.composeAsync(() -> {
                        FileDownloadTask task = new FileDownloadTask(NetworkUtils.toURL(file.getFile().getUrl()), new File(folder, file.getFile().getFilename()), file.getFile().getIntegrityCheck());
                        task.setName(file.getName());
                        return task;
                    }).executor();
                    dialog.setExecutor(executor);
                    dialog.show();
                    executor.start();
                });
            }
        }));
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    public interface DownloadCallback {
        void download(Profile profile, @Nullable String version, RemoteMod.Version file);
    }
}
