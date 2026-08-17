package com.tungsten.fcl.ui.download.common;

import android.content.Context;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDialog;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.download.DownloadUI;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.ui.FCLPage;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class RemoteModVersionPage extends FCLPage {

    private final Profile.ProfileVersion version;
    private final RemoteModVersionPage.DownloadCallback callback;

    public RemoteModVersionPage(Context context, int id, int resId, List<RemoteMod.Version> list, Profile.ProfileVersion version, @Nullable RemoteModVersionPage.DownloadCallback callback, DownloadPage downloadPage) {
        super(context, id, resId);
        this.version = version;
        this.callback = callback;

        // 原 onStart 逻辑：页面构造即初始化列表
        ListView listView = findViewById(R.id.list);
        ModVersionAdapter adapter = new ModVersionAdapter(getContext(), list, modVersion -> {
            if (downloadPage.getPageId() == DownloadUI.PAGE_ID_DOWNLOAD_MOD) {
                RemoteModDownloadPage page = new RemoteModDownloadPage(getContext(), FCLPage.PAGE_ID_TEMP, R.layout.page_download_addon, this.version, modVersion, callback, this, downloadPage);
                UIManager.getInstance().getDownloadUI().showTempPage(page);
            } else {
                download(modVersion);
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
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, null, true, files -> {
            if (files == null) return;
            String folder = files.get(0);
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
        });
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    public interface DownloadCallback {
        void download(Profile profile, @Nullable String version, RemoteMod.Version file);
    }
}
