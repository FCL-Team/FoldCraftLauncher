package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.Pair.pair;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.util.FlowList;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.Pair;
import com.tungsten.fclcore.util.io.CSVTable;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class ModUpdatesPage extends FCLTempPage implements View.OnClickListener {

    private final ModListPage modListPage;
    private final ModManager modManager;
    private final FlowList<ModUpdateObject> objects;

    private ListView listView;
    private FCLButton export;
    private FCLButton update;
    private FCLButton updateWithout;
    private FCLButton cancel;

    public ModUpdatesPage(Context context, int id, FCLUILayout parent, int resId, ModListPage modListPage, ModManager modManager, List<LocalModFile.ModUpdate> list) {
        super(context, id, parent, resId);
        this.modListPage = modListPage;
        this.modManager = modManager;
        this.objects = new FlowList<>(list.stream().map(it -> new ModUpdateObject(getContext(), it)).collect(Collectors.toList()));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        listView = findViewById(R.id.list);
        ThemeEngine.getInstance().registerEvent(listView, () -> listView.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()})));
        export = findViewById(R.id.export);
        update = findViewById(R.id.update);
        updateWithout = findViewById(R.id.update_without);
        cancel = findViewById(R.id.cancel);
        export.setOnClickListener(this);
        update.setOnClickListener(this);
        updateWithout.setOnClickListener(this);
        cancel.setOnClickListener(this);
        updateWithout.setSelected(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        listView.setAdapter(new ModUpdateListAdapter(getContext(), objects));
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
        if (v == export) {
            exportList();
        }
        if (v == update) {
            updateMods(true);
        }
        if (v == updateWithout) {
            updateMods(false);
        }
        if (v == cancel) {
            ManagePageManager.getInstance().dismissCurrentTempPage();
        }
    }

    private void updateMods(boolean keepOldVersion) {
        ModUpdateTask task = new ModUpdateTask(
                modManager,
                objects.get().stream()
                        .filter(o -> o.enabled.getValue())
                        .map(object -> pair(object.data.getLocalMod(), object.data.getCandidates().get(0)))
                        .collect(Collectors.toList()), keepOldVersion);
        TaskDialog taskDialog = new TaskDialog(getContext(), TaskCancellationAction.NORMAL);
        taskDialog.setTitle(getContext().getString(R.string.mods_check_updates_update));
        TaskExecutor executor = task.whenComplete(Schedulers.androidUIThread(), exception -> {
            ManagePageManager.getInstance().dismissCurrentTempPage();
            modListPage.refresh();
            if (!task.getFailedMods().isEmpty()) {
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                builder.setCancelable(false);
                builder.setTitle(getContext().getString(R.string.install_failed));
                builder.setMessage(getContext().getString(R.string.mods_check_updates_failed) + "\n" + task.getFailedMods().stream().map(LocalModFile::getFileName).collect(Collectors.joining("\n")));
                builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                builder.create().show();
            }

            if (exception == null) {
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                builder.setCancelable(false);
                builder.setMessage(getContext().getString(R.string.install_success));
                builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                builder.create().show();
            }
        }).executor();
        taskDialog.setExecutor(executor);
        taskDialog.show();
        executor.start();
    }

    private void exportList() {
        Path path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FCL", "fcl-mod-update-list-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss")) + ".csv").toPath();

        TaskDialog taskDialog = new TaskDialog(getContext(), TaskCancellationAction.NORMAL);
        taskDialog.setTitle(getContext().getString(R.string.button_export));
        TaskExecutor executor = Task.runAsync(() -> {
            CSVTable csvTable = CSVTable.createEmpty();

            csvTable.set(0, 0, "Source File Name");
            csvTable.set(1, 0, "Current Version");
            csvTable.set(2, 0, "Target Version");
            csvTable.set(3, 0, "Update Source");

            for (int i = 0; i < objects.size(); i++) {
                csvTable.set(0, i + 1, objects.get().get(i).fileName.getValue());
                csvTable.set(1, i + 1, objects.get().get(i).currentVersion.getValue());
                csvTable.set(2, i + 1, objects.get().get(i).targetVersion.getValue());
                csvTable.set(3, i + 1, objects.get().get(i).source.getValue());
            }

            csvTable.write(Files.newOutputStream(path));
        }).whenComplete(Schedulers.androidUIThread(), exception -> {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
            if (exception == null) {
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                builder.setCancelable(false);
                builder.setTitle(getContext().getString(R.string.message_success));
                builder.setMessage(path.toString());
            } else {
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                builder.setCancelable(false);
                builder.setTitle(getContext().getString(R.string.message_error));
                builder.setMessage(exception.getMessage());
            }
            builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
            builder.create().show();
        }).executor();
        taskDialog.setExecutor(executor);
        taskDialog.show();
        executor.start();
    }

    public static final class ModUpdateObject {
        final LocalModFile.ModUpdate data;
        final MutableStateFlow<Boolean> enabled = StateFlowKt.MutableStateFlow(false);
        final MutableStateFlow<String> fileName = StateFlowKt.MutableStateFlow(null);
        final MutableStateFlow<String> currentVersion = StateFlowKt.MutableStateFlow(null);
        final MutableStateFlow<String> targetVersion = StateFlowKt.MutableStateFlow(null);
        final MutableStateFlow<String> source = StateFlowKt.MutableStateFlow(null);

        public ModUpdateObject(Context context, LocalModFile.ModUpdate data) {
            this.data = data;

            enabled.setValue(!data.getLocalMod().getModManager().isDisabled(data.getLocalMod().getFile()));
            fileName.setValue(data.getLocalMod().getFileName());
            currentVersion.setValue(data.getCurrentVersion().getVersion());
            targetVersion.setValue(data.getCandidates().get(0).getVersion());
            switch (data.getCurrentVersion().getSelf().getType()) {
                case CURSEFORGE:
                    source.setValue(context.getString(com.tungsten.fcl.R.string.mods_curseforge));
                    break;
                case MODRINTH:
                    source.setValue(context.getString(com.tungsten.fcl.R.string.mods_modrinth));
            }
        }

        public boolean isEnabled() {
            return enabled.getValue();
        }

        public MutableStateFlow<Boolean> enabledFlow() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled.setValue(enabled);
        }

        public String getFileName() {
            return fileName.getValue();
        }

        public MutableStateFlow<String> fileNameFlow() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName.setValue(fileName);
        }

        public String getCurrentVersion() {
            return currentVersion.getValue();
        }

        public MutableStateFlow<String> currentVersionFlow() {
            return currentVersion;
        }

        public void setCurrentVersion(String currentVersion) {
            this.currentVersion.setValue(currentVersion);
        }

        public String getTargetVersion() {
            return targetVersion.getValue();
        }

        public MutableStateFlow<String> targetVersionFlow() {
            return targetVersion;
        }

        public void setTargetVersion(String targetVersion) {
            this.targetVersion.setValue(targetVersion);
        }

        public String getSource() {
            return source.getValue();
        }

        public MutableStateFlow<String> sourceFlow() {
            return source;
        }

        public void setSource(String source) {
            this.source.setValue(source);
        }
    }

    public static class ModUpdateTask extends Task<Void> {
        private final Collection<Task<?>> dependents;
        private final List<LocalModFile> failedMods = new ArrayList<>();

        ModUpdateTask(ModManager modManager, List<Pair<LocalModFile, RemoteMod.Version>> mods, boolean keepOldVersion) {
            setStage("mods.check_updates.update");
            getProperties().put("total", mods.size());

            this.dependents = new ArrayList<>();
            for (Pair<LocalModFile, RemoteMod.Version> mod : mods) {
                LocalModFile local = mod.getKey();
                RemoteMod.Version remote = mod.getValue();
                boolean isDisabled = local.getModManager().isDisabled(local.getFile());

                dependents.add(Task
                        .runAsync(Schedulers.androidUIThread(), () -> local.setOld(true))
                        .thenComposeAsync(() -> {
                            String fileName = remote.getFile().getFilename();
                            if (isDisabled)
                                fileName += ModManager.DISABLED_EXTENSION;

                            FileDownloadTask task = new FileDownloadTask(
                                    new URL(remote.getFile().getUrl()),
                                    modManager.getModsDirectory().resolve(fileName).toFile());

                            task.setName(remote.getName());
                            return task;
                        })
                        .whenComplete(Schedulers.androidUIThread(), exception -> {
                            if (exception != null) {
                                // restore state if failed
                                local.setOld(false);
                                if (isDisabled)
                                    local.disable();
                                failedMods.add(local);
                            } else {
                                if (!keepOldVersion) {
                                    local.getFile().toFile().delete();
                                }
                            }
                        })
                        .withCounter("mods.check_updates.update"));
            }
        }

        public List<LocalModFile> getFailedMods() {
            return failedMods;
        }

        @Override
        public Collection<Task<?>> getDependents() {
            return dependents;
        }

        @Override
        public boolean doPreExecute() {
            return true;
        }

        @Override
        public void preExecute() {
            notifyPropertiesChanged();
        }

        @Override
        public boolean isRelyingOnDependents() {
            return false;
        }

        @Override
        public void execute() throws Exception {
            if (!isDependentsSucceeded())
                throw getException();
        }
    }
}
